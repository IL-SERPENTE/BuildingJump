package fr.azuxul.buildingjump.invetory;

import org.bukkit.inventory.Inventory;
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

    void display();

    /**
     * Return the bukkit inventory object
     * of this inventory
     *
     * @see org.bukkit.inventory.Inventory
     * @return bukkit inventory
     */
    Inventory getInventory();
}
