package fr.azuxul.buildingjump.player;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.BuildingJumpPlugin;
import fr.azuxul.buildingjump.jump.Jump;
import fr.azuxul.buildingjump.jump.JumpLoader;
import net.samagames.api.games.GamePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashSet;

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

        Jump jump = JumpLoader.loadJumpFromFile(new File("jumps/" + getUUID().toString() + "-jump.json"));

        if (jump == null) {

            System.out.println("ok");

            jump = new Jump("Jump", getUUID(), 50, new HashSet<>());
        }

        getPlayerIfOnline().teleport(buildingJumpGame.getJumpManager().registerJump(jump, this));
    }

    public void sendToHub() {
        buildingJumpGame.getPlayerInBuildAndTest().remove(this);

        if (!buildingJumpGame.getPlayerInHub().contains(this))
            buildingJumpGame.getPlayerInHub().add(this);

        getPlayerIfOnline().teleport(buildingJumpGame.getConfiguration().getHubLocation());
    }
}
