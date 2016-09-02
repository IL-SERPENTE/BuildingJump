package fr.azuxul.buildingjump.invetory;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.jump.block.BlockType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Inventory for select specials blocks
 *
 * @author Azuxul
 * @version 1.0
 */
public class InventoryGUIBlockSelector extends InventoryGUI {

    private static final ItemStack BACK = new ItemStack(Material.WOOD_DOOR);


    public InventoryGUIBlockSelector(BuildingJumpGame buildingJumpGame, Player player) {

        super(buildingJumpGame, player, 27, "...");
        initInventory();
    }

    private void initInventory() {

        for (BlockType blockType : BlockType.values()) {
            if (blockType.getItemStack() != null)
                inventory.addItem(blockType.getItemStack());
        }
    }

    @Override
    public boolean click(ItemStack itemStack) {

        if (!itemStack.isSimilar(BACK)) {
            player.getInventory().setItem(8, itemStack.clone());
        }

        return true;
    }
}
