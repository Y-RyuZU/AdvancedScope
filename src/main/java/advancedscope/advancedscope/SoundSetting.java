package advancedscope.advancedscope;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundSetting {
    public Sound sound;
    public float volume;
    public float pitch;
    public int tick = 0;

    public SoundSetting(String param) {
        String[] splited = param.split("-");
        sound = Sound.valueOf(splited[0]);
        volume = Float.parseFloat(splited[1]);
        pitch = Float.parseFloat(splited[2]);
        tick = Integer.parseInt(splited[3]);
    }

    public SoundSetting() {

    }

    public void playSound(Location loc) {
        loc.getWorld().playSound(loc , sound , volume , pitch);
    }

    public void playSound(Player p) {
        p.getWorld().playSound(p.getLocation() , sound , volume , pitch);
    }
}
