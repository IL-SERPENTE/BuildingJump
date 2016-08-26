package fr.azuxul.buildingjump.invetory;

import org.bukkit.inventory.ItemStack;

/**
 * IInventory interface
 *
 * @author Azuxul
 * @version 1.0
 */
@FunctionalInterface
interface IInventoryGUI {

    /**
     * This method was called when player click
     * on a itemStack in this inventory
     *
     * @param clickedItemStack player clicked itemStack in this inventory
     * @return boolean for chose if the player click is canceled
     */
    boolean click(ItemStack clickedItemStack);
}
