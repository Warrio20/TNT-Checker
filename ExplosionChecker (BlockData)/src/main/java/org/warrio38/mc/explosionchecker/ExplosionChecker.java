package org.warrio38.mc.explosionchecker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.warrio38.mc.explosionchecker.events.BlockExplosionEvents;

public final class ExplosionChecker extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new BlockExplosionEvents(this), this);
    }
}
