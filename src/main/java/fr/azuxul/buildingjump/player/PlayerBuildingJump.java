package fr.azuxul.buildingjump.player;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.BuildingJumpPlugin;
import fr.azuxul.buildingjump.invetory.GUIItems;
import fr.azuxul.buildingjump.jump.Jump;
import net.samagames.api.games.GamePlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Game player class of BuildingJump
 *
 * @author Azuxul
 * @version 1.0
 */
public class PlayerBuildingJump extends GamePlayer {

    private final BuildingJumpGame buildingJumpGame;
    private PlayerData playerData;
    private PlayerState state;

    public PlayerBuildingJump(Player player) {
        super(player);

        this.buildingJumpGame = BuildingJumpPlugin.getBuildingJumpGame();
        this.playerData = buildingJumpGame.getLoaderPlayer().getPlayerData(this);

        if (playerData == null) {
            playerData = new PlayerData(new ArrayList<>());
        }
    }

    @Override
    public void handleLogin(boolean reconnect) {
        super.handleLogin(reconnect);

        if (reconnect)
            this.playerData = buildingJumpGame.getLoaderPlayer().getPlayerData(this);

        sendToHub();
    }

    @Override
    public void handleLogout() {
        super.handleLogout();

        buildingJumpGame.getLoaderPlayer().savePlayerData(this);
    }

    public PlayerData getPlayerGameData() {
        return playerData;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void leaveBuild() {
        buildingJumpGame.getPlayerInBuildAndTest().remove(this);
        buildingJumpGame.getJumpManager().unregisterPlayerJump(this);

    }

    public void sendToBuild(UUID jumpUUID) {
        buildingJumpGame.getPlayerInHub().remove(this);

        if(!buildingJumpGame.getPlayerInBuildAndTest().contains(this))
            buildingJumpGame.getPlayerInBuildAndTest().add(this);

        Jump jump;

        if (jumpUUID == null) {
            jump = new Jump(getUUID(), 50, new HashMap<>(), buildingJumpGame);
            playerData.getJumpsUUID().add(jump.getJumpMeta().getUuid());
        } else {
            jump = buildingJumpGame.getJumpManager().loadJumpSave(jumpUUID);
        }

        state = PlayerState.BUILD;

        Inventory inventory = getPlayerIfOnline().getInventory();

        inventory.clear();
        inventory.setItem(0, GUIItems.BUILD_MENU.getItemStack());

        buildingJumpGame.getJumpManager().registerJump(jump, this);

        getPlayerIfOnline().setAllowFlight(true);
        getPlayerIfOnline().setFlying(true);
        getPlayerIfOnline().teleport(jump.getSpawn());
        getPlayerIfOnline().setGameMode(GameMode.CREATIVE);
    }

    public void sendToHub() {

        if (!buildingJumpGame.getPlayerInHub().contains(this))
            buildingJumpGame.getPlayerInHub().add(this);

        state = PlayerState.HUB;

        Inventory inventory = getPlayerIfOnline().getInventory();

        inventory.clear();
        inventory.setItem(0, GUIItems.HUB_MENU.getItemStack());

        getPlayerIfOnline().setGameMode(GameMode.ADVENTURE);
        getPlayerIfOnline().teleport(buildingJumpGame.getConfiguration().getHubLocation());
    }
}
