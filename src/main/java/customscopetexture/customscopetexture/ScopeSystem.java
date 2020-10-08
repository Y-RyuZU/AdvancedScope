package customscopetexture.customscopetexture;

import com.shampaggon.crackshot.events.WeaponScopeEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class ScopeSystem implements Listener {
    public HashMap<String , ScopeData> Scopes = new HashMap<>();
    public HashMap<UUID, ItemStack> KeepOffHandItems = new HashMap<>();

    @EventHandler
    public void ScopeSet(WeaponScopeEvent e) {
        if(!Scopes.containsKey(e.getWeaponTitle())) {
            return;
        }
        ItemStack item = e.getPlayer().getInventory().getItemInOffHand();
        if(!e.isZoomIn()) {
            if (item == null || item.getType() == Material.AIR) {
                return;
            }
            if (item.getItemMeta() == null) {
                return;
            }
            if (item.getItemMeta().getDisplayName() == null) {
                return;
            }
            if (item.getItemMeta().getDisplayName() == null) {
                return;
            }
            if (!item.getItemMeta().getDisplayName().equals("§aスコープ中")) {
                return;
            }
            e.getPlayer().getInventory().setItemInOffHand(KeepOffHandItems.get(e.getPlayer().getUniqueId()));
            KeepOffHandItems.put(e.getPlayer().getUniqueId() , null);
        } else {
            KeepOffHandItems.put(e.getPlayer().getUniqueId() , item);
            e.getPlayer().getInventory().setItemInOffHand(Scopes.get(e.getWeaponTitle()).getItemStack());
        }
    }

    @EventHandler
    public void OnPlayerAnimation(PlayerAnimationEvent e) {
        e.setCancelled(true);
        ItemStack item = e.getPlayer().getInventory().getItemInOffHand();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        if (item.getItemMeta() == null) {
            return;
        }
        if (item.getItemMeta().getDisplayName() == null) {
            return;
        }
        if (item.getItemMeta().getDisplayName() == null) {
            return;
        }
        if (!item.getItemMeta().getDisplayName().equals("§aスコープ中")) {
            return;
        }
        e.setCancelled(true);
    }
}
