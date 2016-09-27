package fr.azuxul.buildingjump.jump.block.effect;

import fr.azuxul.buildingjump.jump.block.BlockType;
import fr.azuxul.buildingjump.jump.block.JumpBlock;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Block effect jump
 *
 * @author Azuxul
 * @version 1.0
 */
public class BlockJump extends BlockEffect {

    public BlockJump(JumpBlock jumpBlock) {
        super(jumpBlock, BlockType.JUMP);
    }

    @Override
    public void displayParticles() {

    }

    @Override
    public void displayTitle() {

    }

    @Override
    public void playerUp(PlayerBuildingJump playerBuildingJump) {
        playerBuildingJump.getPlayerIfOnline().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20, 5));

    }

    @Override
    public void playerDown(PlayerBuildingJump playerBuildingJump) {

    }

    @Override
    public void playerOn(PlayerBuildingJump playerBuildingJump) {

    }
}
