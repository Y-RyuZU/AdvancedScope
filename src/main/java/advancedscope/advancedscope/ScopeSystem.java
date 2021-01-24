package advancedscope.advancedscope;

import com.shampaggon.crackshot.events.WeaponPrepareShootEvent;
import com.shampaggon.crackshot.events.WeaponScopeEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ScopeSystem implements Listener {
    public HashMap<UUID, ItemStack> KeepOffHandItems = new HashMap<>();
    public HashMap<UUID, BukkitRunnable> CanShotDelay = new HashMap<>();
    public HashMap<UUID, BukkitRunnable> HoldGun = new HashMap<>();
    public HashMap<UUID, BukkitRunnable> ScopeOffCoolTime = new HashMap<>();
    public HashMap<UUID, BukkitRunnable> ScopeOnCoolTime = new HashMap<>();

    @EventHandler
    public void ScopeTextureSet(WeaponScopeEvent e) {
        if(getWeaponTitle(e.getWeaponTitle()) == null) {
            return;
        }
        if(getWeaponTitle(e.getWeaponTitle()).material == null) {
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
            e.getPlayer().getInventory().setItemInOffHand(getWeaponTitle(e.getWeaponTitle()).getItemStack());
        }
    }

    @EventHandler
    public void CancelShot(WeaponPrepareShootEvent e) {
        Player p = e.getPlayer();
        ScopeData SD = getWeaponTitle(e.getWeaponTitle());
        if (SD == null) {
            return;
        }
        if(CanShotDelay.get(p.getUniqueId()) != null) {
            for(SoundSetting SS : SD.ShotCancelSound) {
                SS.playSound(e.getPlayer());
            }
            e.setCancelled(true);
        }
        if(HoldGun.get(p.getUniqueId()) != null) {
            for(SoundSetting SS : SD.HoldingGunSound) {
                SS.playSound(e.getPlayer());
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void DelayCancel(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        if(CanShotDelay.get(p.getUniqueId()) != null) {
            CanShotDelay.get(p.getUniqueId()).cancel();
            CanShotDelay.remove(p.getUniqueId());
        }
        String title = AdvancedScope.CS.getWeaponTitle(p.getInventory().getItem(e.getNewSlot()));
        ScopeData SD = getWeaponTitle(title);
        if (SD == null) {
            if(HoldGun.get(p.getUniqueId()) != null) {
                HoldGun.get(p.getUniqueId()).cancel();
                HoldGun.remove(p.getUniqueId());
            }
        }
    }

    @EventHandler
    public void HoldGun(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        String title = AdvancedScope.CS.getWeaponTitle(p.getInventory().getItem(e.getNewSlot()));
        if (title == null) {
            return;
        }
        ScopeData SD = getWeaponTitle(title);
        if (SD == null) {
            return;
        }
        if(SD.HoldGunTime > 0) {
            if(HoldGun.get(p.getUniqueId()) != null) {
                HoldGun.get(p.getUniqueId()).cancel();
                HoldGun.remove(e.getPlayer().getUniqueId());
            }
            BukkitRunnable CoolTime = new BukkitRunnable() {
                int t = 0;
                @Override
                public void run() {
                    if(t >= SD.HoldGunTime) {
                        HoldGun.remove(p.getUniqueId());
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR , TextComponent.fromLegacyText(SD.Message));
                        cancel();
                    } else {
                        String bar = SD.LeftColor + StringUtils.repeat("|" , SD.HoldGunTime - t) + SD.RightColor +  StringUtils.repeat("|" , t);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR , TextComponent.fromLegacyText(bar));
                    }
                    t++;
                }
            };
            CoolTime.runTaskTimer(AdvancedScope.getPlugin(), 0 , 1);
            HoldGun.put(p.getUniqueId(), CoolTime);
        }
    }

    @EventHandler
    public void ScopeSetDelay(WeaponScopeEvent e) {
        ScopeData SD = getWeaponTitle(e.getWeaponTitle());
        Player p = e.getPlayer();
        if (SD == null) {
            return;
        }
        if (!e.isZoomIn()) {
            if(CanShotDelay.get(p.getUniqueId()) != null) {
                CanShotDelay.get(p.getUniqueId()).cancel();
                CanShotDelay.remove(p.getUniqueId());
            }
            if (SD.ScopeOffCoolTime > 0) {
                if (ScopeOffCoolTime.get(p.getUniqueId()) == null) {
                    BukkitRunnable CT = new BukkitRunnable() {
                        @Override
                        public void run() {
                            ScopeOffCoolTime.remove(p.getUniqueId());
                            e.setCancelled(false);
                            cancel();
                        }
                    };
                    CT.runTaskLater(AdvancedScope.getPlugin(), SD.ScopeOffCoolTime);
                    ScopeOffCoolTime.put(p.getUniqueId(), CT);
                    e.setCancelled(true);
                } else {
                    e.setCancelled(true);
                }
            }
        } else {
            if (SD.ScopeOnCoolTime > 0) {
                if (ScopeOnCoolTime.get(p.getUniqueId()) == null) {
                    BukkitRunnable CT = new BukkitRunnable() {
                        @Override
                        public void run() {
                            ScopeOnCoolTime.remove(p.getUniqueId());
                            if (SD.CanShotDelay > 0) {
                                BukkitRunnable CoolTime = new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        CanShotDelay.remove(p.getUniqueId());
                                        cancel();
                                    }
                                };
                                CoolTime.runTaskLater(AdvancedScope.getPlugin(), SD.CanShotDelay);
                                CanShotDelay.put(p.getUniqueId(), CoolTime);
                            }
                            e.setCancelled(false);
                            cancel();
                        }
                    };
                    CT.runTaskLater(AdvancedScope.getPlugin(), SD.ScopeOnCoolTime);
                    ScopeOnCoolTime.put(p.getUniqueId(), CT);
                    e.setCancelled(true);
                } else {
                    e.setCancelled(true);
                }
            } else {
                if (SD.CanShotDelay > 0) {
                    BukkitRunnable CoolTime = new BukkitRunnable() {
                        @Override
                        public void run() {
                            CanShotDelay.remove(p.getUniqueId());
                            cancel();
                        }
                    };
                    CoolTime.runTaskLater(AdvancedScope.getPlugin(), SD.CanShotDelay);
                    CanShotDelay.put(p.getUniqueId(), CoolTime);
                }
            }
        }
    }

    @EventHandler
    public void onChengeItems(PlayerSwapHandItemsEvent e) {
        ItemStack item = e.getOffHandItem();
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

    private ScopeData getWeaponTitle(String WT) {
        ScopeData data = null;
        if(WT == null) {
            return null;
        }
        for(String t : AdvancedScope.SF.Scopes.keySet()) {
            ScopeData SD = AdvancedScope.SF.Scopes.get(t);
            if(SD.Contains) {
                if(WT.contains(t)) {
                    data = SD;
                    break;
                }
            } else {
                if(WT.equals(t)) {
                    data = SD;
                    break;
                }
            }
        }
        return data;
    }
}
