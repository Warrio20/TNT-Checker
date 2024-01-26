package org.warrio38.mc.explosionchecker.events;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.warrio38.mc.explosionchecker.ExplosionChecker;

public class BlockExplosionEvents implements Listener {
    private final ExplosionChecker plugin;
    private final NamespacedKey placedKey;
    public BlockExplosionEvents(ExplosionChecker plugin){
        this.plugin = plugin;
        this.placedKey = new NamespacedKey(plugin,"wasPlaced");
    };
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent event){
        Block block = event.getBlock();
        PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);
        customBlockData.set(placedKey, PersistentDataType.BOOLEAN, true);
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event){
        if(event.getCause().equals(HangingBreakEvent.RemoveCause.EXPLOSION)){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityExplosion(EntityExplodeEvent event){
        event.blockList().removeIf((Block block) -> {
            PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);
            return customBlockData.get(placedKey, PersistentDataType.BOOLEAN) != null;
        });
    }
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event){
        event.blockList().removeIf((Block block) -> {
            PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);
            boolean shouldntBlow = customBlockData.get(placedKey, PersistentDataType.BOOLEAN) != null;
            return shouldntBlow;
        });
    }
    @EventHandler
    public void onSaplingGrow(StructureGrowEvent event){
        Block block = event.getLocation().getBlock();
        PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);
        boolean wasPlaced = customBlockData.get(placedKey, PersistentDataType.BOOLEAN) != null;
        if(wasPlaced){
            customBlockData.remove(placedKey);
        }
    }
    @EventHandler
    public void onBreakBlock(BlockBreakEvent event){
        Block block = event.getBlock();
        PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);
        boolean wasPlaced = customBlockData.get(placedKey, PersistentDataType.BOOLEAN) != null;
        if(wasPlaced){
            customBlockData.remove(placedKey);
        }
    }
}
