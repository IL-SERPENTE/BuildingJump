package fr.azuxul.buildingjump.event;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.player.BuildingJumpPlayer;
import fr.azuxul.buildingjump.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Player events
 *
 * @author Azuxul
 * @version 1.0
 */
public class PlayerEvent implements Listener {

    private final BuildingJumpGame buildingJumpGame;

    public PlayerEvent(BuildingJumpGame buildingJumpGame) {
        this.buildingJumpGame = buildingJumpGame;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        BuildingJumpPlayer buildingJumpPlayer = buildingJumpGame.getPlayer(player.getUniqueId());

        buildingJumpPlayer.setState(PlayerState.HUB);
        player.teleport(buildingJumpGame.getConfiguration().getHubLocation());
    }
}
