package fr.azuxul.buildingjump.invetory;

import fr.azuxul.buildingjump.BuildingJumpGame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Interface of custom inventory of buildingjump
 */
public interface IInventory {

    /**
     * This method was called when player click
     * on a itemStack in this inventory
     *
     * @param clickedItemStack player clicked itemStack in this inventory
     * @return boolean for chose if the player click is canceled
     */
    boolean click(ItemStack clickedItemStack);

    /**
     * Return the bukkit inventory object
     * of this inventory
     *
     * @see org.bukkit.inventory.Inventory
     * @return bukkit inventory
     */
    Inventory getInventory();
}
