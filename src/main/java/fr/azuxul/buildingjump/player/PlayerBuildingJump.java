package fr.azuxul.buildingjump.player;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.BuildingJumpPlugin;
import net.samagames.api.games.GamePlayer;
import org.bukkit.entity.Player;

/**
 * Game player class of BuildingJump
 *
 * @author Azuxul
 * @version 1.0
 */
public class PlayerBuildingJump extends GamePlayer {

    private final BuildingJumpGame buildingJumpGame;
    private PlayerState state;

    public PlayerBuildingJump(Player player) {
        super(player);

        this.buildingJumpGame = BuildingJumpPlugin.getBuildingJumpGame();
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void sendToBuild() {
        buildingJumpGame.getPlayerInHub().remove(this);

        if(!buildingJumpGame.getPlayerInBuildAndTest().contains(this))
            buildingJumpGame.getPlayerInBuildAndTest().add(this);


    }
}
