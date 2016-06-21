package fr.azuxul.buildingjump;

import fr.azuxul.buildingjump.event.PlayerEvent;
import net.samagames.api.SamaGamesAPI;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class of BuildingJump plugin
 *
 * @author Azuxul
 * @version 1.0
 */
public class BuildingJumpPlugin extends JavaPlugin {

    private static BuildingJumpGame buildingJumpGame;

    @Override
    public void onEnable() {

        SamaGamesAPI samaGamesAPI = SamaGamesAPI.get();

        synchronized (this) {
            buildingJumpGame = new BuildingJumpGame(this);
        }

        samaGamesAPI.getGameManager().registerGame(buildingJumpGame);
        samaGamesAPI.getGameManager().getGameProperties();

        getServer().getPluginManager().registerEvents(new PlayerEvent(buildingJumpGame), this);

        getServer().getOnlinePlayers().forEach(player -> player.kickPlayer(ChatColor.RED + "Game loading"));
    }

    public static BuildingJumpGame getBuildingJumpGame() {
        return buildingJumpGame;
    }
}
