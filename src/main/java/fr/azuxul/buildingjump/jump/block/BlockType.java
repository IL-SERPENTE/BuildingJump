package fr.azuxul.buildingjump.jump.block;

import fr.azuxul.buildingjump.ItemUtils;
import fr.azuxul.buildingjump.jump.block.effect.BlockEffect;
import fr.azuxul.buildingjump.jump.block.effect.BlockEnd;
import fr.azuxul.buildingjump.jump.block.effect.BlockJump;
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
    END(3, ItemUtils.addDisplayNameAndLore(new ItemStack(Material.IRON_PLATE), "Fin du jump", ChatColor.WHITE + ""), BlockEnd.class, Material.IRON_PLATE),
    JUMP(4, ItemUtils.addDisplayNameAndLore(new ItemStack(Material.STAINED_CLAY, 1, (short) 6), "JUMP BOST", ChatColor.GREEN + ""), BlockJump.class, Material.STAINED_CLAY);

    private final int id;
    private final ItemStack itemStack;
    private final Class<? extends BlockEffect> effectClass;
    private final Material realMaterial;
    private final int maxLevelEffect1;
    private final int maxLevelEffect2;

    BlockType(int id, ItemStack itemStack, Class<? extends BlockEffect> effectClass, Material realMaterial, int levels) {
        this.id = id;
        this.itemStack = itemStack;
        this.effectClass = effectClass;
        this.realMaterial = realMaterial;
        this.maxLevelEffect1 = levels;
        this.maxLevelEffect2 = -1;
    }

    BlockType(int id, ItemStack itemStack, Class<? extends BlockEffect> effectClass, Material realMaterial, int levelsEffect1, int levelsEffect2) {
        this.id = id;
        this.itemStack = itemStack;
        this.effectClass = effectClass;
        this.realMaterial = realMaterial;
        this.maxLevelEffect1 = levelsEffect1;
        this.maxLevelEffect2 = levelsEffect2;
    }

    BlockType(int id, ItemStack itemStack, Class<? extends BlockEffect> effectClass, Material realMaterial) {
        this.id = id;
        this.itemStack = itemStack;
        this.effectClass = effectClass;
        this.realMaterial = realMaterial;
        this.maxLevelEffect1 = -1;
        this.maxLevelEffect2 = -1;
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

    public int getMaxLevelEffect1() {
        return maxLevelEffect1;
    }

    public int getMaxLevelEffect2() {
        return maxLevelEffect2;
    }

    public boolean hasLevels() {
        return maxLevelEffect1 > 0;
    }
}