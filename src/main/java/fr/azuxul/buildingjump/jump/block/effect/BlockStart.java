package fr.azuxul.buildingjump.jump.block.effect;

import fr.azuxul.buildingjump.jump.block.BlockType;
import fr.azuxul.buildingjump.jump.block.JumpBlock;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import net.samagames.tools.ParticleEffect;
import org.bukkit.Location;

import java.util.Date;

/**
 * Block start effect
 *
 * @author Azuxul
 * @version 1.0
 */
public class BlockStart extends BlockEffect {

    private double i;

    public BlockStart(JumpBlock jumpBlock) {
        super(jumpBlock, BlockType.START);
    }

    @Override
    public void displayParticles() {

        ParticleEffect.FIREWORKS_SPARK.display(0.0F, 0.0F, 0.0F, 0.0F, 1, new Location(location.getWorld(), location.getX() + 0.5D + Math.cos(this.i) * 0.5D, location.getY() + 0.15D, location.getZ() + 0.5D + Math.sin(this.i) * 0.5D), 32.0D);

        this.i += 0.25D;

        if (this.i > Math.PI * 2.0D)
            this.i = 0.0D;

    }

    @Override
    public void displayTitle() {

    }

    @Override
    public void playerOn(PlayerBuildingJump playerBuildingJump) {
        if (!playerBuildingJump.hasStartJump()) {
            playerBuildingJump.setJumpStart(true);
            playerBuildingJump.setJumpStartTime(new Date().getTime());

            playerBuildingJump.getPlayerIfOnline().sendMessage("START");
            playerBuildingJump.setCheckpointLoc(location);
        } else if (playerBuildingJump.getJumpStartTime() + 2000 < new Date().getTime()) {
            playerBuildingJump.setJumpStartTime(new Date().getTime());

            playerBuildingJump.getPlayerIfOnline().sendMessage("RESET TIME");
            playerBuildingJump.setCheckpointLoc(location);
        }
    }

    @Override
    public void playerUp(PlayerBuildingJump playerBuildingJump) {

    }

    @Override
    public void playerDown(PlayerBuildingJump playerBuildingJump) {

    }
}
