package fr.azuxul.buildingjump.jump.block.effect;

import fr.azuxul.buildingjump.jump.block.JumpBlock;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import net.samagames.tools.ParticleEffect;
import org.bukkit.Location;

/**
 * Block start effect
 *
 * @author Azuxul
 * @version 1.0
 */
public class BlockStart extends BlockEffect {

    private double i;

    public BlockStart(JumpBlock jumpBlock) {
        super(jumpBlock);
    }

    @Override
    public void displayParticles() {

        ParticleEffect.FIREWORKS_SPARK.display(0.0F, 0.0F, 0.0F, 0.0F, 1, new Location(location.getWorld(), location.getX() + Math.cos(this.i) * 0.2, location.getY() + 0.15D, location.getZ() + Math.sin(this.i) * 0.2), 32.0D);

        this.i += 0.25D;

        if (this.i > Math.PI * 2.0D)
            this.i = 0.0D;

    }

    @Override
    public void displayTitle() {

    }

    @Override
    public void playerEnter(PlayerBuildingJump playerBuildingJump) {

    }

    @Override
    public void playerUp(PlayerBuildingJump playerBuildingJump) {

    }

    @Override
    public void playerDown(PlayerBuildingJump playerBuildingJump) {

    }

    @Override
    public void playerLeave(PlayerBuildingJump playerBuildingJump) {

    }
}
