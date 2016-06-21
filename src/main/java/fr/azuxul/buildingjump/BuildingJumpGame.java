package fr.azuxul.buildingjump;

import fr.azuxul.buildingjump.player.BuildingJumpPlayer;
import net.samagames.api.games.Game;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Game instance of BuildingJump plugin
 *
 * @author Azuxul
 * @version 1.0
 */
public class BuildingJumpGame extends Game<BuildingJumpPlayer> {

    private final Plugin plugin;
    private final Server server;

    public BuildingJumpGame(JavaPlugin javaPlugin) {

        super("bulding_jump", "BuildingJump", "", BuildingJumpPlayer.class);

        this.plugin = javaPlugin;
        this.server = javaPlugin.getServer();
    }
}
