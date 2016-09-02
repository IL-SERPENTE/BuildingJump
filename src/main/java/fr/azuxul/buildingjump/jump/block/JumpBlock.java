package fr.azuxul.buildingjump.jump.block;

import fr.azuxul.buildingjump.jump.Jump;
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

    private final Jump jump;
    private final JumpLocation jumpLocation;
    private final Material material;
    private final byte dataValue;
    private final BlockType blockType;
    private BlockEffect blockEffect;

    public JumpBlock(Material material, byte dataValue, BlockType blockType, Jump jump, JumpLocation jumpLocation) {

        if (!blockType.equals(BlockType.NORMAL) && blockType.getEffectClass() != null)
            try {
                blockEffect = (BlockEffect) blockType.getEffectClass().getConstructors()[0].newInstance(this);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        else
            blockEffect = null;

        this.jump = jump;
        this.jumpLocation = jumpLocation;
        this.material = material;
        this.dataValue = dataValue;
        this.blockType = blockType;
    }

    public JumpLocation getJumpLocation() {
        return jumpLocation;
    }

    public BlockEffect getBlockEffect() {
        return blockEffect;
    }

    public Jump getJump() {
        return jump;
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
