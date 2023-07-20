package com.i0dev.ancientruins.cmd;

import com.i0dev.ancientruins.util.ItemBuilder;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdAncientRuinsGivePortalFrame extends AncientRuinsCommand {

    public CmdAncientRuinsGivePortalFrame() {
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(TypeInteger.get(), "amount", "1");
        this.setVisibility(Visibility.SECRET);
    }

    @Override
    public void perform() throws MassiveException {
        Player player = this.readArg();
        int amount = this.readArg(1);

        ItemStack item = new ItemBuilder(Material.END_PORTAL_FRAME)
                .name("&aAncient Ruins Portal Frame")
                .addGlow(true)
                .addPDCValue("ancientruinsportal", "true")
                .amount(amount)
                .lore(MUtil.list("&7Place this portal to create an ancient ruins portal frame location."));

        player.getInventory().addItem(item);
        player.sendMessage("You have been given " + amount + " Ancient Ruins Portal(s).");
        player.sendMessage("You can place this portal to create an ancient ruins portal frame location.");
        player.sendMessage("break the portal frame to remove it.");
    }
}
