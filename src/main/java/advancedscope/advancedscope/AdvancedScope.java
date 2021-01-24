package advancedscope.advancedscope;

import com.shampaggon.crackshot.CSUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class AdvancedScope extends JavaPlugin implements Listener {
    private static AdvancedScope plugin;
    public static Plugin getPlugin() {
        return plugin;
    }
    public static CSUtility CS = new CSUtility();
    public static ScopeSystem SS = new ScopeSystem();
    public static ScopeFile SF = new ScopeFile();


    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info(ChatColor.GREEN + "AdvancedScopeがEnableになりました");
        Bukkit.getServer().getPluginManager().registerEvents(SS, this);
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        SF.LoadAllScopes();
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.GREEN + "AdvancedScopeがDisableになりました");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ads")) {
            if(args.length <= 0) {
                sender.sendMessage(ChatColor.RED + "/" + label + " [reload]");
                return true;
            }
            if(args[0].equals("reload") || args[0].equals("r")) {
                SF.LoadAllScopes();
                sender.sendMessage("§aリロード完了");
            }
        }
        return true;
    }
}
