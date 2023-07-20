package com.i0dev.ancientruins.entity;

import com.i0dev.ancientruins.AncientRuinsPlugin;
import com.i0dev.ancientruins.entity.object.ConfigItem;
import com.i0dev.ancientruins.entity.object.ConfigLocation;
import com.i0dev.ancientruins.util.Cuboid;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

@Getter
@EditorName("config")
@Setter
public class MConf extends Entity<MConf> {

    protected static transient MConf i;

    public static MConf get() {
        return i;
    }

    public List<String> aliases = MUtil.list("ancientruins");

    public ConfigItem keyItem = new ConfigItem(
            Material.ENDER_EYE,
            "&cAncient Ruins Key",
            MUtil.list("&7Use this key to open the ruins."),
            true
    );

    String entryPortal = new Cuboid(0, 0, 0, 0, 0, 0, Bukkit.getWorld("world")).serialize();

    public Cuboid getEntryPortal() {
        return Cuboid.deserialize(AncientRuinsPlugin.get(), entryPortal);
    }

    public void setEntryPortal(Cuboid entryPortal) {
        this.entryPortal = entryPortal.serialize();
    }

    String returnPortal = new Cuboid(0, 0, 0, 0, 0, 0, Bukkit.getWorld("world")).serialize();

    public Cuboid getReturnPortal() {
        return Cuboid.deserialize(AncientRuinsPlugin.get(), returnPortal);
    }

    public void setReturnPortal(Cuboid returnPortal) {
        this.returnPortal = returnPortal.serialize();
    }

    List<String> commandsOnEnterEntryPortal = MUtil.list("say %player% has entered the entry portal!");
    List<String> commandsOnEnterReturnPortal = MUtil.list("say %player% has entered the return portal!");

    long portalOpenLengthMillis = 5400000; // 1 hour 30 minutes

    ConfigLocation defaultCrystalBeamLocation = new ConfigLocation(0, 0, 0, "world");
    Axis netherPortalBlockAxis = Axis.Z;
    ChatColor crystalBeamColor = ChatColor.LIGHT_PURPLE;
    long portalOpenDelayTicks = 20 * 10; // 10 seconds

    List<Integer> chatAnnounceSecondsRemaining = MUtil.list(1200, 600, 300, 150, 60, 30, 10, 5, 4, 3, 2, 1);
    String chatAnnouncementMessageForAllPlayersOnline = "&cThe ancient ruins portal will close in %time% seconds!";

    long secondsTillPortalCloseStartDecaying = 600; // 10 minutes

    ConfigLocation centerDecayRadiusLocation = new ConfigLocation(0, 0, 0, "testradius");
    int decayRadius = 50;
    boolean fallingBlockDecay = true;
    int everyXBlocksSpawnFallBlock = 50;

    @Override
    public MConf load(MConf that) {
        super.load(that);
        return this;
    }

}
