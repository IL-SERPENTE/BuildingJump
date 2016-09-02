package fr.azuxul.buildingjump.jump.block.effect;

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

    public BlockEffect(JumpBlock jumpBlock) {
        this.location = jumpBlock.getJump().getWorldLocOfJumpLoc(jumpBlock.getJumpLocation());
    }
}
