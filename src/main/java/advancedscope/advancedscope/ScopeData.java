package advancedscope.advancedscope;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScopeData {
    public String WeaponTitle = null;
    public Material material = null;
    public int damage = 0;
    public boolean Contains = false;
    public String RightColor = null;
    public String LeftColor = null;
    public String Message = null;
    public int ScopeOnCoolTime = 0;
    public int ScopeOffCoolTime = 0;
    public int CanShotDelay = 0;
    public int HoldGunTime = 0;
    public List<SoundSetting> ShotCancelSound = new ArrayList<>();
    public List<SoundSetting> HoldingGunSound = new ArrayList<>();

    public ItemStack getItemStack() {
        ItemStack scope = new ItemStack(material);
        scope.setDurability((short) damage);
        ItemMeta scopemeta = scope.getItemMeta();
        List<ItemFlag> ItemHideFlags = Arrays.asList(ItemFlag.HIDE_ATTRIBUTES , ItemFlag.HIDE_DESTROYS ,ItemFlag.HIDE_ENCHANTS , ItemFlag.HIDE_PLACED_ON , ItemFlag.HIDE_POTION_EFFECTS , ItemFlag.HIDE_UNBREAKABLE);
        for(ItemFlag flag : ItemHideFlags) {
            scopemeta.addItemFlags(flag);
        }
        scopemeta.setUnbreakable(true);
        scopemeta.setDisplayName("§aスコープ中");
        scope.setItemMeta(scopemeta);
        return scope;
    }
}
