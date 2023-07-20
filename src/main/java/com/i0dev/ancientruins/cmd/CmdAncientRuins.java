package com.i0dev.ancientruins.cmd;

import com.i0dev.ancientruins.AncientRuinsPlugin;
import com.i0dev.ancientruins.Perm;
import com.i0dev.ancientruins.entity.MConf;
import com.massivecraft.massivecore.command.MassiveCommandVersion;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

import java.util.List;

public class CmdAncientRuins extends AncientRuinsCommand {

    private static CmdAncientRuins i = new CmdAncientRuins();

    public static CmdAncientRuins get() {
        return i;
    }

    public CmdAncientRuinsGive cmdAncientRuinsGive = new CmdAncientRuinsGive();
    public CmdAncientRuinsSet cmdAncientRuinsSet = new CmdAncientRuinsSet();
    public MassiveCommandVersion cmdFactionsVersion = new MassiveCommandVersion(AncientRuinsPlugin.get()).setAliases("v", "version").addRequirements(RequirementHasPerm.get(Perm.VERSION));

    @Override
    public List<String> getAliases() {
        return MConf.get().aliases;
    }

}
