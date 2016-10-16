package fr.azuxul.buildingjump;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Enum of item trig an action when is clicked
 *
 * @author Azuxul
 * @version 1.0
 */
public enum ActionItems {

    LEAVE_JUMP(ItemUtils.addDisplayNameAndLore(new ItemStack(Material.WOOD_DOOR), "Leave jump"));

    private final ItemStack itemStack;

    ActionItems(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
