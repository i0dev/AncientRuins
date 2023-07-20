package com.i0dev.ancientruins.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class DataColl extends Coll<Data> {
    private static DataColl i = new DataColl();

    public static DataColl get() {
        return DataColl.i;
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setActive(final boolean active) {
        super.setActive(active);
        if (!active) return;
        Data.i = this.get(MassiveCore.INSTANCE, true);
    }
}
