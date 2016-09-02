package fr.azuxul.buildingjump.invetory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Enum of used items for open gui
 *
 * @author Azuxul
 * @version 1.0
 */
public enum GUIItems {

    HUB_MENU(new ItemStack(Material.BEDROCK), InventoryGUIMainSelector.class),
    BUILD_MENU(new ItemStack(Material.ACACIA_STAIRS), InventoryGUIJumpBuilder.class);

    private final ItemStack itemStack;
    private final Class<? extends InventoryGUI> inventoryClass;

    GUIItems(ItemStack itemStack, Class<? extends InventoryGUI> inventoryClass) {
        this.itemStack = itemStack;
        this.inventoryClass = inventoryClass;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Class<? extends InventoryGUI> getInventoryClass() {
        return inventoryClass;
    }
}
