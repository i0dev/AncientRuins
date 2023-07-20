package com.i0dev.ancientruins.task;

import com.i0dev.ancientruins.AncientRuinsPlugin;
import com.i0dev.ancientruins.entity.Data;
import com.i0dev.ancientruins.entity.MConf;
import com.i0dev.ancientruins.entity.object.PortalFrame;
import com.i0dev.ancientruins.util.Utils;
import com.massivecraft.massivecore.ModuloRepeatTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.FallingBlock;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;

import java.util.ArrayList;
import java.util.List;

public class TaskPortal extends ModuloRepeatTask {

    private static final TaskPortal i = new TaskPortal();

    public static TaskPortal get() {
        return i;
    }

    @Override
    public long getDelayMillis() {
        return 1000L; // 1 second
    }

    @Override
    public void invoke(long l) {
        Data data = Data.get();
        if (!data.isPortalOpen()) return;
        long secondsTillClose = (data.getPortalOpenAtTime() + MConf.get().getPortalOpenLengthMillis() - System.currentTimeMillis()) / 1000;
        if (secondsTillClose < MConf.get().getSecondsTillPortalCloseStartDecaying()) {
            if (!data.isAnnouncedDecayStarted()) {
                Bukkit.broadcastMessage(Utils.color("&cAncient Ruins Portal will close in " + secondsTillClose + " seconds, the world will start decaying now"));
                data.setAnnouncedDecayStarted(true);
                data.changed();
            }
            doWorldDecay();
        }
        List<Integer> chatToRemove = new ArrayList<>();
        for (Integer integer : data.getChatMessagesToAnnounce()) {
            if (secondsTillClose <= integer) {
                Bukkit.broadcastMessage(Utils.color(MConf.get().getChatAnnouncementMessageForAllPlayersOnline()
                        .replace("%time%", String.valueOf(integer))
                ));
                chatToRemove.add(integer);
            }
        }
        chatToRemove.forEach(data.getChatMessagesToAnnounce()::remove);

        if (data.getPortalOpenAtTime() + MConf.get().getPortalOpenLengthMillis() < System.currentTimeMillis()) {
            closePortal();
        }
    }


    public void closePortal() {
        Bukkit.broadcastMessage("Ancient Ruins Portal is now closed");
        List<PortalFrame> toClear = new ArrayList<>();
        Data data = Data.get();
        data.getPortalFrames().forEach(portalFrame -> {
            EnderCrystal enderCrystal = (EnderCrystal) Bukkit.getEntity(portalFrame.getEntityUUID());
            if (enderCrystal != null) {
                enderCrystal.setBeamTarget(null);
                enderCrystal.remove();
            }
            Block block = portalFrame.getLocation().asBukkitLocation().getBlock();
            EndPortalFrame endPortalFrame = (EndPortalFrame) block.getBlockData();
            endPortalFrame.setEye(false);
            block.setBlockData(endPortalFrame);
            toClear.add(portalFrame);
        });
        toClear.forEach(portalFrame -> portalFrame.setEntityUUID(null));

        data.setPortalOpen(false);
        data.setPortalOpenAtTime(0);
        data.setChatMessagesToAnnounce(new ArrayList<>());
        data.setAnnouncedDecayStarted(false);
        data.changed();

        MConf.get().getEntryPortal().getAllCuboidLocations().forEach(location -> {
            if (location.getBlock().getType() == Material.NETHER_PORTAL) {
                location.getBlock().setType(Material.AIR);
            }
        });

        MConf.get().getReturnPortal().getAllCuboidLocations().forEach(location -> {
            if (location.getBlock().getType() == Material.NETHER_PORTAL) {
                location.getBlock().setType(Material.AIR);
            }
        });


        // TODO: paste back the original world
    }

    int counter = 0;

    public void doWorldDecay() {
        double percentRemaining = getPercentOfRadiusRemaining() * 100;
        int outerRadius = MConf.get().getDecayRadius();
        int innerRadius = (int) (outerRadius * (percentRemaining / 100));
        for (Location loc : calculateBlocksBetweenCircles(MConf.get().getCenterDecayRadiusLocation().asBukkitLocation(), innerRadius, outerRadius)) {
            BlockData blockData = loc.getBlock().getBlockData().clone();
            if (blockData.getMaterial().isAir()) continue;
            loc.getBlock().setType(Material.AIR);
            // falling block now:
//            if (MConf.get().isFallingBlockDecay()) {
//                if (counter > MConf.get().getEveryXBlocksSpawnFallBlock()) { // limiter
//                    if (!blockData.getMaterial().isSolid()) continue;
//                    if (System.currentTimeMillis() - AncientRuinsPlugin.get().getStartTime() > 1000 * 75) { // don't spawn entities until 75 seconds after server start
//                        FallingBlock fallingBlock = loc.getWorld().spawnFallingBlock(loc, blockData);
//                        fallingBlock.setDropItem(false);
//                        fallingBlock.setHurtEntities(false);
//                        fallingBlock.setInvulnerable(true);
//                    }
//                    counter = 0;
//                } else {
//                    counter++;
//                }
//            }
        }
    }

    public static List<Location> calculateBlocksBetweenCircles(Location center, int innerRadius, int outerRadius) {
        List<Location> outerCircle = circle(center, outerRadius, -64, false, false, 0);
        List<Location> innerCircle = circle(center, innerRadius, -64, false, false, 0);

        List<Location> locations = new ArrayList<>();
        outerCircle.stream()
                .filter(element -> !innerCircle.contains(element))
                .forEach(locations::add);

        locations.forEach(location -> {
            for (int i = -64; i < 319; i++) {
                Location loc = location.clone();
                loc.setY(i);
                if (!loc.getBlock().getType().isAir()) {
                    locations.add(loc);
                }
            }
        });

        System.out.println(locations.size());
        System.out.println("inner size: " + innerCircle.size());
        System.out.println("outer size: " + outerCircle.size());

        return locations;
    }

    public static List<Location> circle(Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y) {
        List<Location> circleblocks = new ArrayList<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx + r; x++)
            for (int z = cz - r; z <= cz + r; z++)
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        Location l = new Location(loc.getWorld(), x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }

        return circleblocks;
    }


    // Once time to close portal reaches 10 minutes until close,
    // start decaying the world around the portal, linearly
    public double getPercentOfRadiusRemaining() {
        Data data = Data.get();
        long secondsTillClose = (data.getPortalOpenAtTime() + MConf.get().getPortalOpenLengthMillis() - System.currentTimeMillis()) / 1000;
        double percentRemaining = 1;
        if (secondsTillClose < MConf.get().getSecondsTillPortalCloseStartDecaying()) {
            percentRemaining = (double) secondsTillClose / MConf.get().getSecondsTillPortalCloseStartDecaying();
        }
        return percentRemaining;
    }

}
