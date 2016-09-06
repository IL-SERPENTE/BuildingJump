package fr.azuxul.buildingjump.jump.block;

import fr.azuxul.buildingjump.jump.JumpLocation;
import fr.azuxul.buildingjump.jump.block.effect.BlockEffect;
import org.bukkit.Material;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Jump block
 *
 * @author Azuxul
 * @version 1.0
 */
public class JumpBlock {

    private final JumpLocation jumpLocation;
    private final Material material;
    private final byte dataValue;
    private final BlockType blockType;
    private BlockEffect blockEffect;

    public JumpBlock(Material material, byte dataValue, BlockType blockType, JumpLocation jumpLocation) {

        this.jumpLocation = jumpLocation;
        this.material = material;
        this.dataValue = dataValue;
        this.blockType = blockType;

        generateBlockEffect();
    }

    private void generateBlockEffect() {

        if (!blockType.equals(BlockType.NORMAL) && blockType.getEffectClass() != null)
            try {
                blockEffect = (BlockEffect) blockType.getEffectClass().getConstructors()[0].newInstance(this);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        else
            blockEffect = null;
    }

    public JumpLocation getJumpLocation() {
        return jumpLocation;
    }

    public BlockEffect getBlockEffect() {
        if (blockEffect == null)
            generateBlockEffect();

        return blockEffect;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getDataValue() {
        return dataValue;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        JumpBlock jumpBlock = (JumpBlock) o;
        return dataValue == jumpBlock.dataValue && material == jumpBlock.material && blockType == jumpBlock.blockType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, dataValue, blockType);
    }
}
