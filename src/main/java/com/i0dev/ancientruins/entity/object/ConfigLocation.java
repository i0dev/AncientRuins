package com.i0dev.ancientruins.entity.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

@Data
@AllArgsConstructor
public class ConfigLocation {

    int x, y, z;
    String world;

    @Override
    public String toString() {
        return x + "~" + y + "~" + z + "~" + world;
    }

    public static ConfigLocation fromString(String s) {
        String[] split = s.split("~");
        return new ConfigLocation(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), split[3]);
    }

    public static ConfigLocation fromLocation(Location location) {
        return new ConfigLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName());
    }

    public Location asBukkitLocation() {
        return new Location(org.bukkit.Bukkit.getWorld(world), x, y, z);
    }

}
