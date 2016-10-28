package fr.azuxul.buildingjump.command;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.jump.block.BlockType;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import fr.azuxul.buildingjump.player.PlayerState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Command executor of /blockgive
 *
 * @author Azuxul
 * @version 1.0
 */
public class CommandBlockGive implements CommandExecutor {

    private final BuildingJumpGame buildingJumpGame;

    public CommandBlockGive(BuildingJumpGame buildingJumpGame) {
        this.buildingJumpGame = buildingJumpGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;
        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(player);

        if (!playerBuildingJump.getState().equals(PlayerState.BUILD)) {
            player.sendMessage("BUILD");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("MISSING ARGS");
            return true;
        } else if (args.length < 2) {
            player.getInventory().addItem(BlockType.values()[Integer.valueOf(args[0])].getItemStack());
        } else {
            ItemStack itemStack = BlockType.values()[Integer.valueOf(args[0])].getItemStack();
            String loreStr = "";


            for (int i = 1; i < args.length; i++) {
                loreStr += args[i] + ",";
            }

            ItemMeta itemMeta = itemStack.getItemMeta();

            List<String> lore = itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore();
            lore.add(loreStr.substring(0, loreStr.length() - 2));

            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

        }

        return true;
    }
}
