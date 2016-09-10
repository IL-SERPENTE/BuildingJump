package fr.azuxul.buildingjump.jump.block;

import fr.azuxul.buildingjump.ItemUtils;
import fr.azuxul.buildingjump.jump.block.effect.BlockEffect;
import fr.azuxul.buildingjump.jump.block.effect.BlockEnd;
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

    NORMAL(0, null, null, null),
    START(1, ItemUtils.addDisplayNameAndLore(new ItemStack(Material.IRON_PLATE), "Debut du jump", ChatColor.WHITE + ""), BlockStart.class, Material.AIR),
    CHECKPOINT(2, ItemUtils.addDisplayNameAndLore(new ItemStack(Material.IRON_PLATE), "Checkpoint", ChatColor.WHITE + ""), null, Material.IRON_PLATE),
    END(3, ItemUtils.addDisplayNameAndLore(new ItemStack(Material.IRON_PLATE), "Fin du jump", ChatColor.WHITE + ""), BlockEnd.class, Material.IRON_PLATE);

    private final int id;
    private final ItemStack itemStack;
    private final Class<? extends BlockEffect> effectClass;
    private final Material realMaterial;

    BlockType(int id, ItemStack itemStack, Class<? extends BlockEffect> effectClass, Material realMaterial) {
        this.id = id;
        this.itemStack = itemStack;
        this.effectClass = effectClass;
        this.realMaterial = realMaterial;
    }

    public static BlockType isSpecialBlock(ItemStack itemStack) {
        for (BlockType b : values()) {
            if (itemStack.equals(b.getItemStack()))
                return b;
        }

        return NORMAL;
    }

    public Material getRealMaterial() {
        return realMaterial;
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