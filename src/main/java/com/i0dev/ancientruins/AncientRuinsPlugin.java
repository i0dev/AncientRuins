package com.i0dev.ancientruins;

import com.i0dev.ancientruins.entity.DataColl;
import com.i0dev.ancientruins.entity.MConf;
import com.i0dev.ancientruins.entity.MConfColl;
import com.i0dev.ancientruins.entity.MLangColl;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.collections.MassiveList;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class AncientRuinsPlugin extends MassivePlugin {

    private static AncientRuinsPlugin i;

    public AncientRuinsPlugin() {
        AncientRuinsPlugin.i = this;
    }

    public static AncientRuinsPlugin get() {
        return i;
    }

    @Override
    public void onEnableInner() {
        this.activateAuto();
    }


    @Override
    public List<Class<?>> getClassesActiveColls() {
        return new MassiveList<>(
                MConfColl.class,
                MLangColl.class,
                DataColl.class
        );
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        
        return p instanceof WorldEditPlugin ? (WorldEditPlugin) p : null;
    }

    Team pinkTeam;
    long startTime = 0;

    @Override
    public void onEnablePost() {
        super.onEnablePost();
        startTime = System.currentTimeMillis();
        Scoreboard pinkBoard = Bukkit.getScoreboardManager().getMainScoreboard();
        pinkTeam = pinkBoard.getTeam("pink_glow_for_ancient_ruins_portal");
        if (pinkTeam == null) pinkTeam = pinkBoard.registerNewTeam("pink_glow_for_ancient_ruins_portal");
        pinkTeam.setColor(MConf.get().getCrystalBeamColor());
    }

}