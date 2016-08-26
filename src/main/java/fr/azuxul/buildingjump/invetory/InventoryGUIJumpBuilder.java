package fr.azuxul.buildingjump.invetory;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.jump.Jump;
import fr.azuxul.buildingjump.jump.JumpLoader;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
    private final BuildingJumpGame buildingJumpGame;
    private final Player player;
    private final Inventory inventory;
    private final Jump jump;

    public InventoryGUIJumpBuilder(BuildingJumpGame buildingJumpGame, Player player) {

        super(buildingJumpGame, player, 27, "...");

        this.buildingJumpGame = buildingJumpGame;
        this.inventory = getInventory();
        this.player = player;
        this.jump = buildingJumpGame.getJumpManager().getPlayerLoadedJump(buildingJumpGame.getPlayer(player));

        initInventory();
    }

    private void initInventory() {

        inventory.setItem(0, LEAVE);
        inventory.setItem(1, SPAWN);
    }

    @Override
    public boolean click(ItemStack itemStack) {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(player);

        if (itemStack.equals(LEAVE)) {

            JumpLoader.saveJump(jump);
            playerBuildingJump.sendToHub();
        } else if (itemStack.equals(SPAWN)) {

            player.sendMessage("set spawn");
            jump.setSpawn(player.getLocation());
        }

        return true;
    }
}
