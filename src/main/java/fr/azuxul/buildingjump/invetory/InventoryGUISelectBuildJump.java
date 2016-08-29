package fr.azuxul.buildingjump.invetory;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.jump.JumpMeta;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Inventory for select the jump for build
 *
 * @author Azuxul
 * @version 1.0
 */
public class InventoryGUISelectBuildJump extends InventoryGUI {

    private static final ItemStack JUMP = new ItemStack(Material.WOOD);
    private static final ItemStack NEXT_PAGE = new ItemStack(Material.ACACIA_FENCE);
    private static final ItemStack PREV_PAGE = new ItemStack(Material.APPLE);
    private static final ItemStack NEW_JUMP = new ItemStack(Material.ARMOR_STAND);

    private final BuildingJumpGame buildingJumpGame;
    private final Player player;
    private final int page;

    public InventoryGUISelectBuildJump(BuildingJumpGame buildingJumpGame, Player player, int page) {

        super(buildingJumpGame, player, 54, "");
        this.buildingJumpGame = buildingJumpGame;
        this.player = player;
        this.page = page;

        initInventory();
    }

    public InventoryGUISelectBuildJump(BuildingJumpGame buildingJumpGame, Player player) {
        this(buildingJumpGame, player, 1);
    }

    private void initInventory() {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(player);
        List<UUID> jumps = playerBuildingJump.getPlayerGameData().getJumpsUUID();

        int max = jumps.size() - 1 - 45 * (page - 1);
        max = max >= 45 ? 44 : max;

        for (int i = 0; i <= max; i++) {
            getInventory().setItem(i, getItemStackForJump(jumps.get(max + 45 * (page - 1))));
        }

        getInventory().setItem(45, NEW_JUMP);
    }

    private ItemStack getItemStackForJump(UUID jumpUUID) {
        ItemStack itemStack = JUMP.clone();

        JumpMeta meta = buildingJumpGame.getJumpManager().loadJumpSave(jumpUUID).getJumpMeta();
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Dif: " + ChatColor.GOLD + meta.getOwnerDifficulty());
        lore.add(ChatColor.GRAY + "Temps en test: " + ChatColor.GREEN + (meta.getTestTime() > 0 ? meta.getTestTime() / 60 + ":" + (meta.getTestTime() - meta.getTestTime() % 60 * 60) : "--:--"));
        lore.add(ChatColor.GRAY + "Date de création: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date(meta.getCreateDate())));
        lore.add(ChatColor.GRAY + "ID: " + jumpUUID.toString());
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(ChatColor.GREEN + meta.getName());
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public boolean click(ItemStack itemStack) {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(player);

        UUID uuid = isJumpClicked(itemStack);

        if (uuid != null) {
            playerBuildingJump.sendToBuild(uuid);
        } else if (itemStack.isSimilar(NEW_JUMP)) {
            playerBuildingJump.sendToBuild(null);
        }

        return true;
    }

    private UUID isJumpClicked(ItemStack itemStack) {

        if (itemStack.getType().equals(JUMP.getType()) && itemStack.getData().getData() == JUMP.getData().getData() && probablyUUIDInLore(itemStack.getItemMeta())) {
            return UUID.fromString(itemStack.getItemMeta().getLore().get(3).substring(6));
        } else
            return null;
    }

    private boolean probablyUUIDInLore(ItemMeta itemMeta) {
        return itemMeta != null && itemMeta.getLore() != null && itemMeta.getLore().size() >= 4;
    }
}