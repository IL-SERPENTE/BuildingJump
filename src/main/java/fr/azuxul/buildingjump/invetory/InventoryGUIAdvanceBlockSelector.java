package fr.azuxul.buildingjump.invetory;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.jump.block.BlockType;
import net.samagames.tools.ItemUtils;
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

    private final static ItemStack HEAD_0 = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMGViZTdlNTIxNTE2OWE2OTlhY2M2Y2VmYTdiNzNmZGIxMDhkYjg3YmI2ZGFlMjg0OWZiZTI0NzE0YjI3In19fQ==");
    private final static ItemStack HEAD_1 = ItemUtils.getCustomHead("71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");
    private final static ItemStack HEAD_2 = ItemUtils.getCustomHead("71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");
    private final static ItemStack HEAD_3 = ItemUtils.getCustomHead("71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");
    private final static ItemStack HEAD_4 = ItemUtils.getCustomHead("71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");
    private final static ItemStack HEAD_5 = ItemUtils.getCustomHead("71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");
    private final static ItemStack HEAD_6 = ItemUtils.getCustomHead("71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");
    private final static ItemStack HEAD_7 = ItemUtils.getCustomHead("71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");
    private final static ItemStack HEAD_8 = ItemUtils.getCustomHead("71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");
    private final static ItemStack HEAD_9 = ItemUtils.getCustomHead("71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");

    private static final ItemStack BACK = new ItemStack(Material.WOOD_DOOR);

    private final BlockType blockType;

    public InventoryGUIAdvanceBlockSelector(BuildingJumpGame buildingJumpGame, Player player, BlockType blockType) {

        super(buildingJumpGame, player, 27, "...");
        this.blockType = blockType;
        initInventory();
    }

    private void initInventory() {

        updateInventory(0, 0);
    }

    private void updateInventory(int nb1, int nb2) {

        String nb1S = Integer.toString(nb1);
        String nb2S = Integer.toString(nb2);

        inventory.setItem(12, getHeadForNumber(nb1S.length() < 2 ? 0 : nb1S.charAt(1)));
        inventory.setItem(13, getHeadForNumber(nb1S.charAt(0)));
        inventory.setItem(21, getHeadForNumber(nb1S.length() < 2 ? 0 : nb2S.charAt(1)));
        inventory.setItem(22, getHeadForNumber(nb2S.charAt(0)));
    }

    private ItemStack getHeadForNumber(int number) {

        switch (number) {
            case 1:
                return HEAD_1;
            case 2:
                return HEAD_2;
            case 3:
                return HEAD_3;
            case 4:
                return HEAD_4;
            case 5:
                return HEAD_5;
            case 6:
                return HEAD_6;
            case 7:
                return HEAD_7;
            case 8:
                return HEAD_8;
            case 9:
                return HEAD_9;
            default:
                return HEAD_0;
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