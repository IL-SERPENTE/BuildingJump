package fr.azuxul.buildingjump.invetory;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.jump.block.BlockType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Inventory for select details of specials blocks
 *
 * @author Azuxul
 * @version 1.0
 */
public class InventoryGUIAdvanceBlockSelector extends InventoryGUI {

    private static final ItemStack BACK = new ItemStack(Material.WOOD_DOOR);

    private final BlockType blockType;

    public InventoryGUIAdvanceBlockSelector(BuildingJumpGame buildingJumpGame, Player player, BlockType blockType) {

        super(buildingJumpGame, player, 27, "...");
        this.blockType = blockType;
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