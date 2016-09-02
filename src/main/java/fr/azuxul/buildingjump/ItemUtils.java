package fr.azuxul.buildingjump;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Item utils class
 *
 * @author Azuxul
 * @version 1.0
 */
public class ItemUtils {

    private ItemUtils() {
    }

    public static ItemStack addDisplayNameAndLore(ItemStack itemStack, String name, String... lore) {

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
