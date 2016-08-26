package fr.azuxul.buildingjump.invetory;

import fr.azuxul.buildingjump.BuildingJumpGame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Abstract class InventoryGUI class
 */
public abstract class InventoryGUI implements IInventoryGUI, InventoryHolder {

    private final Player player;
    private final Inventory inventory;

    InventoryGUI(BuildingJumpGame buildingJumpGame, Player player, int size, String name) {

        this.inventory = buildingJumpGame.getServer().createInventory(this, size, name);
        this.player = player;
    }

    public void display() {
        player.openInventory(inventory);
    }

    /**
     * Return the bukkit inventory object
     * of this inventory
     *
     * @see org.bukkit.inventory.Inventory
     * @return bukkit inventory
     */
    public Inventory getInventory() {
        return inventory;
    }
}
