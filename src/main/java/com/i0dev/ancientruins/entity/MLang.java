package com.i0dev.ancientruins.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;

@EditorName("config")
public class MLang extends Entity<MLang> {

    protected static transient MLang i;

    public static MLang get() {
        return i;
    }

    public String prefix = "&8[&cAncient&4Ruins&8]&7";

    @Override
    public MLang load(MLang that) {
        super.load(that);
        return this;
    }
}
