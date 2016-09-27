package fr.azuxul.buildingjump.jump.block.effect;

import fr.azuxul.buildingjump.jump.block.BlockType;
import fr.azuxul.buildingjump.jump.block.JumpBlock;
import org.bukkit.Location;

/**
 * Base of block effect
 *
 * @author Azuxul
 * @version 1.0
 */
public abstract class BlockEffect implements IBlockEffect {

    protected final Location location;
    private final BlockType blockType;

    BlockEffect(JumpBlock jumpBlock, BlockType blockType) {
        this.location = jumpBlock.getJumpLocation().getLocation();
        this.blockType = blockType;
    }

    public BlockType getType() {
        return blockType;
    }
}
