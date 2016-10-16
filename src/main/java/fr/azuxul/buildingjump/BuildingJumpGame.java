package fr.azuxul.buildingjump;

import fr.azuxul.buildingjump.clock.BuildingJumpClock;
import fr.azuxul.buildingjump.jump.JumpManager;
import fr.azuxul.buildingjump.loader.LoaderPlayer;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Game;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Game instance of BuildingJump plugin
 *
 * @author Azuxul
 * @version 1.0
 */
public class BuildingJumpGame extends Game<PlayerBuildingJump> {

    private final Plugin plugin;
    private final Server server;
    private final Configuration configuration;
    private final JumpManager jumpManager;
    private final LoaderPlayer loaderPlayer;
    private final BuildingJumpClock buildingJumpClock;

    public BuildingJumpGame(JavaPlugin javaPlugin) {

        super("bulding_jump", "BuildingJump", "", PlayerBuildingJump.class);

        this.plugin = javaPlugin;
        this.server = javaPlugin.getServer();
        this.configuration = new Configuration(SamaGamesAPI.get().getGameManager().getGameProperties().getConfigs());
        this.jumpManager = new JumpManager(this);
        this.loaderPlayer = new LoaderPlayer(this);
        this.buildingJumpClock = new BuildingJumpClock(this);
    }

    public BuildingJumpClock getBuildingJumpClock() {
        return buildingJumpClock;
    }

    public LoaderPlayer getLoaderPlayer() {
        return loaderPlayer;
    }

    public JumpManager getJumpManager() {
        return jumpManager;
    }

    public Server getServer() {
        return server;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public PlayerBuildingJump getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }
}
