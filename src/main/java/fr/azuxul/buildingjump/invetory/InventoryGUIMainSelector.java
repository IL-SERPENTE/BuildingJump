package fr.azuxul.buildingjump.invetory;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Activity selector inventory
 *
 * @author Azuxul
 * @version 1.0
 */
public class InventoryGUIMainSelector extends InventoryGUI {

    private final BuildingJumpGame buildingJumpGame;

    public InventoryGUIMainSelector(BuildingJumpGame buildingJumpGame, Player player) {

        super(buildingJumpGame, player, 27, "");
        this.buildingJumpGame = buildingJumpGame;

        initInventory();
    }

    private void initInventory() {

        getInventory().setItem(0, new ItemStack(Material.BRICK));
    }

    @Override
    public boolean click(ItemStack itemStack) {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(player);

        if(Material.BRICK.equals(itemStack.getType())) {
            new InventoryGUISelectBuildJump(buildingJumpGame, player).display();
        }

        return true;
    }
}

