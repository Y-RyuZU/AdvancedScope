package customscopetexture.customscopetexture;

import com.shampaggon.crackshot.CSUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class CustomScopeTexture extends JavaPlugin implements Listener {
    private static CustomScopeTexture plugin;
    public static Plugin getPlugin() {
        return plugin;
    }
    public static CSUtility CS = new CSUtility();
    public static ScopeSystem SS = new ScopeSystem();


    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info(ChatColor.GREEN + "CustomScopeTextureがEnableになりました");
        Bukkit.getServer().getPluginManager().registerEvents(SS, this);
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();
        List<String> ScopeSettings =  this.getConfig().getStringList("WeaponTitle-Material-Damage");
        for(String ScopeSetting : ScopeSettings) {
            String[] ScopeSettingData = ScopeSetting.split("-");
            SS.Scopes.put(ScopeSettingData[0] , new ScopeData(ScopeSettingData[1] , Integer.parseInt(ScopeSettingData[2])));
        }
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.GREEN + "CustomScopeTextureがDisableになりました");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("cst")) {
            if(args.length <= 0) {
                sender.sendMessage(ChatColor.RED + "/" + label + " [reload]");
                return true;
            }
            if(args[0].equals("reload") || args[0].equals("r")) {
                List<String> ScopeSettings =  this.getConfig().getStringList("WeaponTitle-Material-Damage");
                for(String ScopeSetting : ScopeSettings) {
                    String[] ScopeSettingData = ScopeSetting.split("-");
                    SS.Scopes.put(ScopeSettingData[0] , new ScopeData(ScopeSettingData[1] , Integer.parseInt(ScopeSettingData[2])));
                }
                sender.sendMessage("§aリロード完了");
            }
        }
        return true;
    }
}
