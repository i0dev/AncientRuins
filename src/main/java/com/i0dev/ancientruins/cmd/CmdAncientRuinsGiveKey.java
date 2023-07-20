package com.i0dev.ancientruins.cmd;

import com.i0dev.ancientruins.entity.MConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;

public class CmdAncientRuinsGiveKey extends AncientRuinsCommand {

    public CmdAncientRuinsGiveKey() {
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(TypeInteger.get(), "amount", "1");
        this.setVisibility(Visibility.SECRET);
    }

    @Override
    public void perform() throws MassiveException {
        Player player = this.readArg();
        int amount = this.readArg(1);

        player.getInventory().addItem(MConf.get().getKeyItem().getItemStack(amount));
        msg("gave %s key", player.getName());
    }
}
