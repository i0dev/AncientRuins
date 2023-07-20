package com.i0dev.ancientruins.entity.object;

import com.i0dev.ancientruins.util.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
@AllArgsConstructor
public class ConfigItem {

    public Material material;
    public String displayName;
    public List<String> lore;
    public boolean glow;


    public ItemStack getItemStack(int amount) {
        return new ItemBuilder(material)
                .name(displayName)
                .lore(lore)
                .addPDCValue("ancientruins-key", "true")
                .amount(amount)
                .addGlow(glow);
    }

}
