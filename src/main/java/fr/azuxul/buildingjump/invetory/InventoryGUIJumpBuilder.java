package fr.azuxul.buildingjump.invetory;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.jump.Jump;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Main inventory of jump builder
 *
 * @author Azuxul
 * @version 1.0
 */
public class InventoryGUIJumpBuilder extends InventoryGUI implements InventoryHolder {

    private static final ItemStack LEAVE = new ItemStack(Material.WOOD_DOOR);
    private static final ItemStack SPAWN = new ItemStack(Material.BED);
    private static final ItemStack BLOCKS = new ItemStack(Material.BEDROCK);
    private static final ItemStack TEST = new ItemStack(Material.IRON_PLATE);

    private final Jump jump;

    public InventoryGUIJumpBuilder(BuildingJumpGame buildingJumpGame, Player player) {

        super(buildingJumpGame, player, 27, "...");

        this.jump = buildingJumpGame.getJumpManager().getPlayerLoadedJump(buildingJumpGame.getPlayer(player));

        initInventory();
    }

    private void initInventory() {

        inventory.setItem(0, LEAVE);
        inventory.setItem(1, SPAWN);
        inventory.setItem(2, BLOCKS);
        inventory.setItem(3, TEST);
    }

    @Override
    public boolean click(ItemStack itemStack) {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(player);

        if (itemStack.equals(LEAVE)) {

            buildingJumpGame.getJumpManager().unregisterPlayerJump(playerBuildingJump);
            playerBuildingJump.leaveBuild();
            playerBuildingJump.sendToHub();
        } else if (itemStack.equals(SPAWN)) {

            player.sendMessage("set spawn");
            jump.setSpawn(player.getLocation());
        } else if (itemStack.equals(BLOCKS)) {

            new InventoryGUIBlockSelector(buildingJumpGame, player).display();
        } else if (itemStack.equals(TEST)) {

            playerBuildingJump.testJump();
        }

        return true;
    }
}
