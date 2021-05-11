package me.kkiomen.main.help;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class EntityLocation {
    private Location entityLocation;

    public EntityLocation(LivingEntity entity) {
        this.entityLocation = entity.getLocation();
    }

    public Integer getDistance(LivingEntity entity) {
        Location dloc;
        // Integer xdiff, zdiff;
        Integer xdiffi, zdiffi;
        dloc = entity.getLocation();
        if (dloc.getWorld() != entityLocation.getWorld())
            return null; // Different world, different result!


        xdiffi = dloc.getBlockX() - entityLocation.getBlockX();
        // xdiffi = xdiff.intValue();
        if (xdiffi < 0) {
            xdiffi = ~xdiffi + 1;
        }
        zdiffi = dloc.getBlockZ() - entityLocation.getBlockZ();
        // zdiffi = zdiff.intValue();
        if (zdiffi < 0) {
            zdiffi = ~zdiffi + 1;
        }

        return zdiffi + xdiffi;
    }
}
