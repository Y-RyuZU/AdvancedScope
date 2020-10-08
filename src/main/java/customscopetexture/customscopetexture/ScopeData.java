package customscopetexture.customscopetexture;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ScopeData {
    public Material material = null;
    public int damage = 0;

    public ScopeData(String stringmaterial , int d) {
        material = Material.getMaterial(stringmaterial);
        damage = d;
    }

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
