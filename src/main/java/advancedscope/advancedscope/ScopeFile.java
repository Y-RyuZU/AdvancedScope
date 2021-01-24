package advancedscope.advancedscope;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ScopeFile {
    public HashMap<String , ScopeData> Scopes = new HashMap<>();

    private void LoadScope(File f) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(f);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String name : config.getConfigurationSection("").getKeys(false)) {
            ScopeData SD = new ScopeData();
            SD.WeaponTitle = name;
            SD.Contains = config.getBoolean(name + ".Contains" , false);
            SD.HoldGunTime = config.getInt(name + ".HoldGunTime" , 0);
            List<String> HGS = config.getStringList(name + ".HoldingGunSound");
            SD.CanShotDelay = config.getInt(name + ".CanShotDelay" , 0);
            List<String> SCS = config.getStringList(name + ".ShotCancelSound");
            SD.ScopeOnCoolTime = config.getInt(name + ".ScopeOnCoolTime" , 0);
            SD.ScopeOffCoolTime = config.getInt(name + ".ScopeOffCoolTime" , 0);
            SD.Message = config.getString(name + ".HondingTimeEndMessage" , "§6CT終了！").replace("&" , "§");
            SD.RightColor = config.getString(name + ".HoldingRightBarColor" , "§7").replace("&" , "§");
            SD.LeftColor = config.getString(name + ".HoldingLeftBarColor" , "§6").replace("&" , "§");
            if(config.getString(name + ".Texture") != null) {
                String[] texture = config.getString(name + ".Texture").split("-");
                SD.material = Material.getMaterial(texture[0]);
                SD.damage = Integer.parseInt(texture[1]);
            }
            for(String d : SCS) {
                SoundSetting SS = new SoundSetting();
                String[] data = d.split("-");
                SS.sound = Sound.valueOf(data[0]);
                SS.volume = Float.parseFloat(data[1]);
                SS.pitch = Float.parseFloat(data[2]);
                SD.ShotCancelSound.add(SS);
            }
            for(String d : HGS) {
                SoundSetting SS = new SoundSetting();
                String[] data = d.split("-");
                SS.sound = Sound.valueOf(data[0]);
                SS.volume = Float.parseFloat(data[1]);
                SS.pitch = Float.parseFloat(data[2]);
                SD.HoldingGunSound.add(SS);
            }
            Scopes.put(name , SD);
        }
    }

    public void LoadAllScopes() {
        Scopes.clear();
        File ScopeDirectory = new File(AdvancedScope.getPlugin().getDataFolder(), "scopes");
        if (!ScopeDirectory.exists()) ScopeDirectory.mkdirs();
        File[] RoomFiles = ScopeDirectory.listFiles();
        for (File f : RoomFiles) {
            LoadScope(f);
        }
    }
}
