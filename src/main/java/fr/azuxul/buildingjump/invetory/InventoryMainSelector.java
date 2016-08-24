package fr.azuxul.buildingjump.invetory;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Activity selector inventory
 *
 * @author Azuxul
 * @version 1.0
 */
public class InventoryMainSelector implements InventoryHolder, IInventory {

    private final BuildingJumpGame buildingJumpGame;
    private final Player player;
    private final Inventory inventory;

    public InventoryMainSelector(BuildingJumpGame buildingJumpGame, Player player) {

        this.buildingJumpGame = buildingJumpGame;
        this.inventory = buildingJumpGame.getServer().createInventory(this, 27, "Sélection de serveur");
        this.player = player;

        initInventory();
    }

    protected void initInventory() {

        inventory.setItem(0, new ItemStack(Material.BRICK));
    }

    @Override
    public void display() {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean click(ItemStack itemStack) {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(player);

        if(Material.BRICK.equals(itemStack.getType())) {
            playerBuildingJump.sendToBuild();
        }

        return true;
    }
}

