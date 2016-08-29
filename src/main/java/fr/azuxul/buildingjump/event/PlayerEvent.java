package fr.azuxul.buildingjump.event;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.GUIItems;
import fr.azuxul.buildingjump.invetory.InventoryGUI;
import fr.azuxul.buildingjump.jump.block.BlockType;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import fr.azuxul.buildingjump.player.PlayerState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
    public void onPlayerInteract(PlayerInteractEvent event) {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(event.getPlayer());

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null) {
            for (GUIItems guiItems : GUIItems.values()) {
                if (guiItems.getItemStack().isSimilar(event.getItem())) {
                    try {
                        InventoryGUI inventoryGUI = (InventoryGUI) guiItems.getInventoryClass().getConstructors()[0].newInstance(buildingJumpGame, event.getPlayer());
                        inventoryGUI.display();
                        event.setCancelled(true);

                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    return;
                }
            }

            if (event.getItem().getType().isBlock() && playerBuildingJump.getState().equals(PlayerState.BUILD) && event.getPlayer().isSneaking() && event.getPlayer().isFlying()) {

                Block block = event.getPlayer().getLocation().clone().add(0, -1, 0).getBlock();

                block.setTypeIdAndData(event.getItem().getType().getId(), event.getItem().getData().getData(), true);
                if (!buildingJumpGame.getJumpManager().getPlayerLoadedJump(playerBuildingJump).update(block, BlockType.NORMAL)) {
                    block.setTypeIdAndData(Material.AIR.getId(), (byte) 0, true);
                    event.getPlayer().sendMessage("e");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(event.getWhoClicked().getUniqueId());
        InventoryHolder inventoryHolder = event.getInventory().getHolder();

        event.setCancelled(!playerBuildingJump.getState().equals(PlayerState.BUILD));

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

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(event.getPlayer());

        if (playerBuildingJump.getState().equals(PlayerState.BUILD)) {
            if (!buildingJumpGame.getJumpManager().getPlayerLoadedJump(playerBuildingJump).update(event.getBlock(), BlockType.NORMAL)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("e");
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(event.getPlayer());
        if (playerBuildingJump.getState().equals(PlayerState.BUILD)) {
            buildingJumpGame.getServer().getScheduler().runTaskLater(buildingJumpGame.getPlugin(), () -> {
                if (!buildingJumpGame.getJumpManager().getPlayerLoadedJump(playerBuildingJump).update(event.getBlock(), BlockType.NORMAL)) {
                    event.getPlayer().sendMessage("e");
                    event.setCancelled(true);
                }
            }, 1L);
        } else {
            event.setCancelled(true);
        }
    }
}
