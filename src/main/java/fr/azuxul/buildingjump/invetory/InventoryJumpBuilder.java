package fr.azuxul.buildingjump.invetory;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.jump.JumpLoader;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Main inventory of jump builder
 *
 * @author Azuxul
 * @version 1.0
 */
public class InventoryJumpBuilder implements InventoryHolder, IInventory {

    private final BuildingJumpGame buildingJumpGame;
    private final Player player;
    private final Inventory inventory;

    public InventoryJumpBuilder(BuildingJumpGame buildingJumpGame, Player player) {

        this.buildingJumpGame = buildingJumpGame;
        this.inventory = buildingJumpGame.getServer().createInventory(this, 27, "...");
        this.player = player;

        initInventory();
    }

    protected void initInventory() {

        inventory.setItem(0, new ItemStack(Material.WOOD_DOOR));
    }

    @Override
    public void display() {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean click(ItemStack itemStack) {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(player);

        if (Material.WOOD_DOOR.equals(itemStack.getType())) {

            JumpLoader.saveJump(buildingJumpGame.getJumpManager().getPlayerLoadedJump(playerBuildingJump));
            playerBuildingJump.sendToHub();
        }

        return true;
    }
}
