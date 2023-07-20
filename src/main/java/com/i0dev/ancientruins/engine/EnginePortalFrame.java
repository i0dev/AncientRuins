package com.i0dev.ancientruins.engine;

import com.i0dev.ancientruins.AncientRuinsPlugin;
import com.i0dev.ancientruins.entity.Data;
import com.i0dev.ancientruins.entity.MConf;
import com.i0dev.ancientruins.entity.object.PortalFrame;
import com.i0dev.ancientruins.util.ItemBuilder;
import com.i0dev.ancientruins.util.Utils;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MetadataSimple;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.util.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class EnginePortalFrame extends Engine {

    private static EnginePortalFrame i = new EnginePortalFrame();

    public static EnginePortalFrame get() {
        return i;
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        ItemStack itemInHand = e.getItemInHand();

        if (ItemBuilder.getPDCValue(itemInHand, "ancientruinsportal") == null) return;

        Data.get().createPortalFrame(e.getBlockPlaced().getLocation());
        Data.get().changed();
        e.getPlayer().sendMessage("Portal frame location added.");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        PortalFrame portalFrame = Data.get().getPortalFrameByLocation(e.getBlock().getLocation());
        if (portalFrame != null) {
            Data.get().removePortalFrame(portalFrame);
            Data.get().changed();
            e.getPlayer().sendMessage("Portal frame location removed.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteractEvent(PlayerInteractEvent e) {
        Action action = e.getAction();
        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        ItemStack itemInHand = e.getItem();
        Block block = e.getClickedBlock();
        if (block == null) return;
        Location location = block.getLocation();
        Player player = e.getPlayer();

        if (ItemBuilder.getPDCValue(itemInHand, "ancientruins-key") == null) return;
        e.setCancelled(true);

        PortalFrame configPortalFrame = Data.get().getPortalFrameByLocation(location);
        if (configPortalFrame == null) {
            player.sendMessage("This is not a ancient ruins portal frame. You can only use this key on ancient ruins portal frames.");
            return;
        }

        EndPortalFrame portalFrame = (EndPortalFrame) block.getBlockData();
        if (portalFrame.hasEye()) {
            player.sendMessage("This portal frame already has an eye.");
            return;
        }
        portalFrame.setEye(true);
        block.setBlockData(portalFrame);


        EnderCrystal crystal = (EnderCrystal) location.getWorld().spawnEntity(location.clone().add(0.5, 1, 0.5), EntityType.ENDER_CRYSTAL);
        location.getWorld().playSound(location, Sound.BLOCK_END_PORTAL_SPAWN, 1, 1);
        location.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, location.clone().add(0.5, 1, 0.5), 1);
        crystal.setShowingBottom(false);
        crystal.setPersistent(true);
        crystal.setInvulnerable(true);
        crystal.setGlowing(true);
        crystal.setCustomNameVisible(false);
        crystal.setCustomName(Utils.color("&cAncient Ruins Portal Crystal"));
        AncientRuinsPlugin.get().getPinkTeam().addEntry(crystal.getUniqueId().toString());

        configPortalFrame.setEntityUUID(crystal.getUniqueId());
        Data.get().changed();

        checkPortalCompleteness();

        itemInHand.setAmount(itemInHand.getAmount() - 1);
        e.getPlayer().updateInventory();
    }


    public void checkPortalCompleteness() {
        Data data = Data.get();
        AtomicBoolean missing = new AtomicBoolean(false);
        List<PortalFrame> toRemove = new ArrayList<>();
        data.getPortalFrames().forEach((portalFrame) -> {
            Block block = portalFrame.getLocation().asBukkitLocation().getBlock();
            if (!block.getType().equals(Material.END_PORTAL_FRAME)) {
                toRemove.add(portalFrame);
                return;
            }
            EndPortalFrame entityPortalFrame = (EndPortalFrame) block.getBlockData();
            if (!entityPortalFrame.hasEye()) {
                missing.set(true);
            }

        });
        toRemove.forEach(data::removePortalFrame);
        if (toRemove.size() > 0)
            data.changed();

        if (missing.get()) return;

        openPortal();
    }

    public void openPortal() {
        Data data = Data.get();
        data.getPortalFrames().forEach(portalFrame -> {
            EnderCrystal crystal = (EnderCrystal) Bukkit.getEntity(portalFrame.getEntityUUID());
            if (crystal == null) return;
            crystal.setBeamTarget(portalFrame.getBeamLocation().asBukkitLocation());
        });

        Bukkit.getServer().broadcastMessage("Portal charging up, opening in 10 seconds!");
        Bukkit.getScheduler().runTaskLater(AncientRuinsPlugin.get(), () -> {
            Bukkit.getServer().broadcastMessage("Portal is now open for 1 hour 30 minutes!");
            data.setPortalOpen(true);
            data.setPortalOpenAtTime(System.currentTimeMillis());
            data.setChatMessagesToAnnounce(MConf.get().getChatAnnounceSecondsRemaining());
            data.changed();

            MConf.get().getEntryPortal().getAllCuboidLocations().forEach(location -> {
                if (location.getBlock().getType() == Material.AIR) {
                    location.getBlock().setType(Material.NETHER_PORTAL);
                    Orientable orientable = (Orientable) location.getBlock().getBlockData();
                    orientable.setAxis(MConf.get().getNetherPortalBlockAxis());
                    location.getBlock().setBlockData(orientable);
                }
            });

            MConf.get().getReturnPortal().getAllCuboidLocations().forEach(location -> {
                if (location.getBlock().getType() == Material.AIR) {
                    location.getBlock().setType(Material.NETHER_PORTAL);
                    Orientable orientable = (Orientable) location.getBlock().getBlockData();
                    orientable.setAxis(MConf.get().getNetherPortalBlockAxis());
                    location.getBlock().setBlockData(orientable);
                }
            });

        }, MConf.get().getPortalOpenDelayTicks());
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getTo() == null) return;
        if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ() || e.getFrom().getY() != e.getTo().getY()) {
            if (MConf.get().getEntryPortal().contains(e.getTo())) {
                // Entry portal
                handlePortal(e.getPlayer(), true);
            } else if (MConf.get().getReturnPortal().contains(e.getTo())) {
                // Return portal
                handlePortal(e.getPlayer(), false);
            }
        }
    }

    private void handlePortal(Player player, boolean entry) {
        if (!Data.get().isPortalOpen()) {
            if (entry)
                player.sendMessage("The portal is not open yet.");
            else
                player.sendMessage("The portal is closed sorry!");
            return;
        }

        List<String> commands;
        if (entry)
            commands = MConf.get().getCommandsOnEnterEntryPortal();
        else
            commands = MConf.get().getCommandsOnEnterReturnPortal();

        Utils.runCommands(commands, player);

    }
}
