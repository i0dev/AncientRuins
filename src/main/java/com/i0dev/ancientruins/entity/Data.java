package com.i0dev.ancientruins.entity;

import com.i0dev.ancientruins.entity.object.ConfigLocation;
import com.i0dev.ancientruins.entity.object.PortalFrame;
import com.massivecraft.massivecore.store.Entity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Data extends Entity<Data> {

    protected static Data i;

    public static Data get() {
        return i;
    }

    public boolean portalOpen = false;
    public long portalOpenAtTime = 0;
    List<PortalFrame> portalFrames = new ArrayList<>();
    List<Integer> chatMessagesToAnnounce = new ArrayList<>();
    boolean announcedDecayStarted = false;

    public void createPortalFrame(Location location) {
        PortalFrame portalFrame = new PortalFrame(ConfigLocation.fromLocation(location), null, MConf.get().getDefaultCrystalBeamLocation());
        portalFrames.add(portalFrame);
    }

    public PortalFrame getPortalFrameByLocation(Location location) {
        for (PortalFrame portalFrame : portalFrames) {
            if (portalFrame.getLocation().equals(ConfigLocation.fromLocation(location))) {
                return portalFrame;
            }
        }
        return null;
    }

    public void removePortalFrameByLocation(Location location) {
        for (PortalFrame portalFrame : portalFrames) {
            if (portalFrame.getLocation().equals(ConfigLocation.fromLocation(location))) {
                portalFrames.remove(portalFrame);
                return;
            }
        }
    }

    public void removePortalFrame(PortalFrame portalFrame) {
        portalFrames.remove(portalFrame);
    }


    @Override
    public Data load(Data that) {
        this.portalOpen = that.portalOpen;
        this.portalOpenAtTime = that.portalOpenAtTime;
        this.portalFrames = that.portalFrames;

        return this;
    }

}


