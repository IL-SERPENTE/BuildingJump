package fr.azuxul.buildingjump.jump.block;

import fr.azuxul.buildingjump.ItemUtils;
import fr.azuxul.buildingjump.jump.block.effect.BlockEffect;
import fr.azuxul.buildingjump.jump.block.effect.BlockStart;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Enum of jump block types
 *
 * @author Azuxul
 * @version 1.0
 */
public enum BlockType {

    NORMAL(0, null, null),
    CHECKPOINT(1, ItemUtils.addDisplayNameAndLore(new ItemStack(Material.IRON_PLATE), "Checkpoint", ChatColor.WHITE + ""), null),
    START(2, ItemUtils.addDisplayNameAndLore(new ItemStack(Material.IRON_PLATE), "Debut du jump", ChatColor.WHITE + ""), BlockStart.class),
    END(3, ItemUtils.addDisplayNameAndLore(new ItemStack(Material.GOLD_PLATE), "Fin du jump", ChatColor.WHITE + ""), null);

    private final int id;
    private final ItemStack itemStack;
    private final Class<? extends BlockEffect> effectClass;

    BlockType(int id, ItemStack itemStack, Class<? extends BlockEffect> effectClass) {
        this.id = id;
        this.itemStack = itemStack;
        this.effectClass = effectClass;
    }

    public static BlockType isSpecialBlock(ItemStack itemStack) {
        for (BlockType b : values()) {
            if (itemStack.equals(b.getItemStack()))
                return b;
        }

        return NORMAL;
    }

    public int getId() {
        return id;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Class<? extends BlockEffect> getEffectClass() {
        return effectClass;
    }
}