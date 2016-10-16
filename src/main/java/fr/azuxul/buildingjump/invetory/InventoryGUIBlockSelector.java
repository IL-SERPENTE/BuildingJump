package fr.azuxul.buildingjump.invetory;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.jump.block.BlockType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Inventory for select specials blocks
 *
 * @author Azuxul
 * @version 1.0
 */
public class InventoryGUIBlockSelector extends InventoryGUI {

    private static final ItemStack BACK = new ItemStack(Material.WOOD_DOOR);

    private final Map<ItemStack, BlockType> items;

    public InventoryGUIBlockSelector(BuildingJumpGame buildingJumpGame, Player player) {

        super(buildingJumpGame, player, 27, "...");
        items = new HashMap<>();
        initInventory();
    }

    private void initInventory() {

        for (BlockType blockType : BlockType.values()) {
            if (blockType.getItemStack() != null) {
                items.put(blockType.getItemStack(), blockType);
                inventory.addItem(blockType.getItemStack());
            }
        }
    }

    @Override
    public boolean click(ItemStack itemStack) {

        if (!itemStack.isSimilar(BACK)) {
            BlockType blockType = items.get(itemStack);
            if (blockType.hasLevels()) {
                new InventoryGUIAdvanceBlockSelector(buildingJumpGame, player, blockType).display();
            } else {
                player.getInventory().setItem(8, itemStack.clone());
            }
        } else {
            new InventoryGUIJumpBuilder(buildingJumpGame, player).display();
        }

        return true;
    }
}
