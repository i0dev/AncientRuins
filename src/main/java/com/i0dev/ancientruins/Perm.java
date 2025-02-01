package com.i0dev.ancientruins;

import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.util.PermissionUtil;
import org.bukkit.permissions.Permissible;

public enum Perm implements Identified {

    BASECOMMAND,

    GIVE,
    GIVE_KEY,
    GIVE_PORTAL_FRAME,
    SET,
    SET_ENTRY,
    SET_RETURN,
    SET_DEFAULT_BEAM_LOCATION,

    VERSION;

    private final String id;

    Perm() {
        this.id = PermissionUtil.createPermissionId(AncientRuinsPlugin.get(), this);
    }

    @Override
    public String getId() {
        return id;
    }

    public boolean has(Permissible permissible, boolean verboose) {
        return PermissionUtil.hasPermission(permissible, this, verboose);
    }

    public boolean has(Permissible permissible) {
        return PermissionUtil.hasPermission(permissible, this);
    }

}
