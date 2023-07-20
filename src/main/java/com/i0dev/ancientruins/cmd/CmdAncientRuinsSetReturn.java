package com.i0dev.ancientruins.cmd;

import com.i0dev.ancientruins.AncientRuinsPlugin;
import com.i0dev.ancientruins.entity.MConf;
import com.i0dev.ancientruins.util.Cuboid;
import com.i0dev.ancientruins.util.Utils;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;

public class CmdAncientRuinsSetReturn extends AncientRuinsCommand {

    public CmdAncientRuinsSetReturn() {
        this.setVisibility(Visibility.SECRET);
        this.addRequirements(RequirementIsPlayer.get());
    }

    @Override
    public void perform() {
        LocalSession session = AncientRuinsPlugin.get().getWorldEdit().getSession(me);

        if (session == null) {
            msg(Utils.prefixAndColor("%prefix% &cPlease make a selection with the worldedit wand!"));
            return;
        }
        Region selection = null;
        try {
            selection = AncientRuinsPlugin.get().getWorldEdit().getSession(me).getSelection();
        } catch (Exception e) {
            msg(Utils.prefixAndColor("%prefix% &cPlease make a selection with the worldedit wand!"));
        }
        if (selection == null) {
            msg(Utils.prefixAndColor("%prefix% &cPlease make a selection with the worldedit wand!"));
            return;
        }

        MConf.get().setReturnPortal(new Cuboid(selection.getMinimumPoint(), selection.getMaximumPoint(), Bukkit.getWorld(selection.getWorld().getName())));
        MConf.get().changed();
        msg("&aSet return point");
    }
}
