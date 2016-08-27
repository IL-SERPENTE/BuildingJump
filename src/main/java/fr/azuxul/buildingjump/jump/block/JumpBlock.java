package fr.azuxul.buildingjump.jump.block;

import org.bukkit.Material;

import java.util.Objects;

/**
 * Jump block
 *
 * @author Azuxul
 * @version 1.0
 */
public class JumpBlock {

    private Material material;
    private byte dataValue;
    private BlockType blockType;

    public JumpBlock(Material material, byte dataValue, BlockType blockType) {

        this.material = material;
        this.dataValue = dataValue;
        this.blockType = blockType;
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
