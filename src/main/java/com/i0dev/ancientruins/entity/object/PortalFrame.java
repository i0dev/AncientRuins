package com.i0dev.ancientruins.entity.object;

import com.i0dev.ancientruins.entity.Data;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PortalFrame {

    public ConfigLocation location;
    public UUID entityUUID;
    public ConfigLocation beamLocation;


    public static PortalFrame getPortalFrameByLocation(Location location) {
        return Data.get().getPortalFrames().stream().filter(portalFrame -> portalFrame.getLocation().equals(ConfigLocation.fromLocation(location))).findFirst().orElse(null);
    }

}

