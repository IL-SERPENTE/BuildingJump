package fr.azuxul.buildingjump.player;

import fr.azuxul.buildingjump.ActionItems;
import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.BuildingJumpPlugin;
import fr.azuxul.buildingjump.invetory.GUIItems;
import fr.azuxul.buildingjump.jump.Jump;
import net.minecraft.server.v1_9_R2.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_9_R2.WorldBorder;
import net.samagames.api.games.GamePlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Date;
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
    private long jumpStartTime;
    private Jump currentJump;
    private boolean jumpStart;
    private Location checkpointLoc;

    public PlayerBuildingJump(Player player) {
        super(player);

        this.buildingJumpGame = BuildingJumpPlugin.getBuildingJumpGame();
        this.playerData = buildingJumpGame.getLoaderPlayer().getPlayerData(this);

        if (playerData == null) {
            playerData = new PlayerData(new ArrayList<>(), new HashMap<>());
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

    public Location getCheckpointLoc() {
        return checkpointLoc;
    }

    public void setCheckpointLoc(Location checkpointLoc) {
        this.checkpointLoc = checkpointLoc;
    }

    public void teleportToCheckpoint() {
        getPlayerIfOnline().setFallDistance(0);
        getPlayerIfOnline().teleport(checkpointLoc.clone().add(0, 2, 0));
    }

    public boolean hasStartJump() {
        return jumpStart;
    }

    public void setJumpStart(boolean jumpStart) {
        this.jumpStart = jumpStart;
    }

    public long getJumpStartTime() {
        return jumpStartTime;
    }

    public void setJumpStartTime(long jumpStartTime) {
        this.jumpStartTime = jumpStartTime;
    }

    public Jump getCurrentJump() {
        return currentJump;
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

    public void testJump() {
        state = PlayerState.TEST;

        Player player = getPlayerIfOnline();

        player.teleport(currentJump.getSpawn());
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.getInventory().setItem(8, ActionItems.LEAVE_JUMP.getItemStack());
    }

    public void sendToBuild(UUID jumpUUID) {

        Jump jump;

        if (jumpUUID == null) {
            jump = new Jump(getUUID(), 50, new HashMap<>(), buildingJumpGame);
            playerData.getJumpsUUID().add(jump.getJumpMeta().getUuid());
        } else if (buildingJumpGame.getJumpManager().getJumps().get(uuid) != null && buildingJumpGame.getJumpManager().getJumps().get(uuid).getLastLoadedUpdate() + 900000 >= new Date().getTime()) {
            jump = buildingJumpGame.getJumpManager().getJumps().get(uuid);
        } else {
            jump = buildingJumpGame.getJumpManager().loadJumpSave(jumpUUID);
        }

        state = PlayerState.BUILD;

        Inventory inventory = getPlayerIfOnline().getInventory();

        inventory.clear();
        inventory.setItem(0, GUIItems.BUILD_MENU.getItemStack());

        buildingJumpGame.getJumpManager().registerJump(jump);

        getPlayerIfOnline().setAllowFlight(true);
        getPlayerIfOnline().setFlying(true);
        getPlayerIfOnline().teleport(jump.getSpawn());
        getPlayerIfOnline().setGameMode(GameMode.CREATIVE);

        WorldBorder worldBorder = new WorldBorder();

        worldBorder.setCenter(jump.getJumpCenter().getBlockX(), jump.getJumpCenter().getBlockZ());
        worldBorder.setDamageAmount(0);
        worldBorder.setDamageBuffer(0);
        worldBorder.setSize(jump.getSize() * 2);
        worldBorder.setWarningDistance(0);
        worldBorder.setWarningTime(0);

        ((CraftPlayer) getPlayerIfOnline()).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));

        currentJump = jump;
    }

    public void sendToHub() {

        state = PlayerState.HUB;
        currentJump = null;

        Inventory inventory = getPlayerIfOnline().getInventory();

        inventory.clear();
        inventory.setItem(0, GUIItems.HUB_MENU.getItemStack());

        getPlayerIfOnline().setGameMode(GameMode.ADVENTURE);
        getPlayerIfOnline().teleport(buildingJumpGame.getConfiguration().getHubLocation());

        WorldBorder worldBorder = new WorldBorder();

        Location loc = buildingJumpGame.getConfiguration().getHubLocation();

        worldBorder.setCenter(loc.getBlockX(), loc.getBlockZ());
        worldBorder.setSize(2000);
        worldBorder.setWarningDistance(0);

        ((CraftPlayer) getPlayerIfOnline()).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));


        currentJump = null;
    }
}
