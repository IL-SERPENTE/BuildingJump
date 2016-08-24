package fr.azuxul.buildingjump.jump;

import org.bukkit.Material;

enum BlockType {

    NORMAL(0),
    CHECKPONT(1),
    START(2),
    END(3),;

    private final int id;

    BlockType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

/**
 * Jump block
 *
 * @author Azuxul
 * @version 1.0
 */
public class JumpBlock {

    private final int x;
    private final int y;
    private final int z;
    private Material material;
    private byte dataValue;
    private BlockType blockType;

    public JumpBlock(Material material, byte dataValue, BlockType blockType, int x, int y, int z) {

        this.x = x;
        this.y = y;
        this.z = z;

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
