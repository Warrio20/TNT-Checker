package org.warrio38.mc.explosionchecker.events;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;
public class BlockExplosionEvents implements Listener {
    private final CoreProtectAPI api = getCoreProtect();
    private CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }
        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (CoreProtect.isEnabled() == false) {
            return null;
        }
        if (CoreProtect.APIVersion() < 9) {
            return null;
        }
        return CoreProtect;
    }
    @EventHandler
    public void onHangingBreak(HangingBreakEvent event){
        if(event.getCause().equals(HangingBreakEvent.RemoveCause.EXPLOSION)){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityExplosion(EntityExplodeEvent event){
        if (api == null){
            return;
        }
        event.blockList().removeIf(this::Remover);
    }
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event){
        if (api == null){
            return;
        }
        event.blockList().removeIf(this::Remover);
    }
    public boolean Remover(Block block){
        boolean shouldBreak = false;
        for(String[] el : api.blockLookup(block,-1)){
            String action = api.parseResult(el).getActionString();
            if(action.equals("break")){
                break;
            }
            if(action.equals("place")){
                shouldBreak = true;
                break;
            }
        }
        return shouldBreak;
    }
}