package com.i0dev.ancientruins.cmd;

import com.i0dev.ancientruins.AncientRuinsPlugin;
import com.i0dev.ancientruins.entity.MConf;
import com.i0dev.ancientruins.entity.object.ConfigLocation;
import com.i0dev.ancientruins.util.Cuboid;
import com.i0dev.ancientruins.util.Utils;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;

public class CmdAncientRuinsSetDefaultBeamLocation extends AncientRuinsCommand {

    public CmdAncientRuinsSetDefaultBeamLocation() {
        this.setVisibility(Visibility.SECRET);
        this.addRequirements(RequirementIsPlayer.get());
    }

    @Override
    public void perform() {
        MConf.get().setDefaultCrystalBeamLocation(ConfigLocation.fromLocation(me.getLocation()));
        MConf.get().changed();
        msg("&aSet default beam location");
    }

}
