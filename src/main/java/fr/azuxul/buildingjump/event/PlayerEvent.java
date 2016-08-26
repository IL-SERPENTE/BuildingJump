package fr.azuxul.buildingjump.event;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.GUIItems;
import fr.azuxul.buildingjump.invetory.InventoryGUI;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import fr.azuxul.buildingjump.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.InventoryHolder;

import java.lang.reflect.InvocationTargetException;

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
        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(player.getUniqueId());

        playerBuildingJump.sendToHub();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null) {
            for (GUIItems guiItems : GUIItems.values()) {
                if (guiItems.getItemStack().isSimilar(event.getItem())) {
                    try {
                        InventoryGUI inventoryGUI = (InventoryGUI) guiItems.getInventoryClass().getConstructors()[0].newInstance(buildingJumpGame, event.getPlayer());
                        inventoryGUI.display();

                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {

        InventoryHolder inventoryHolder = event.getInventory().getHolder();

        event.setCancelled(true);

        if (event.getCurrentItem() != null && inventoryHolder != null && inventoryHolder instanceof InventoryGUI) {

            InventoryGUI customInventory = (InventoryGUI) inventoryHolder;

            event.setCancelled(customInventory.click(event.getCurrentItem()));
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(event.getPlayer());

        if (PlayerState.HUB.equals(playerBuildingJump.getState())) {
            for (PlayerBuildingJump player : buildingJumpGame.getPlayerInHub()) {
                Player bukkitPlayer = player.getPlayerIfOnline();

                if (bukkitPlayer != null)
                    bukkitPlayer.sendMessage(event.getPlayer().getDisplayName() + ": " + event.getMessage());
            }
        } else if (PlayerState.TEST.equals(playerBuildingJump.getState()) || PlayerState.BUILD.equals(playerBuildingJump.getState())) {
            for (PlayerBuildingJump player : buildingJumpGame.getPlayerInBuildAndTest()) {
                Player bukkitPlayer = player.getPlayerIfOnline();

                if (bukkitPlayer != null)
                    bukkitPlayer.sendMessage(event.getPlayer().getDisplayName() + ": " + event.getMessage());
            }
        }
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(event.getPlayer());

        event.setCancelled(!PlayerState.BUILD.equals(playerBuildingJump.getState()));
    }
}
