package fr.azuxul.buildingjump.jump.block.effect;

import fr.azuxul.buildingjump.jump.JumpMeta;
import fr.azuxul.buildingjump.jump.block.BlockType;
import fr.azuxul.buildingjump.jump.block.JumpBlock;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import fr.azuxul.buildingjump.player.PlayerState;
import net.samagames.tools.ParticleEffect;
import org.bukkit.Location;

import java.util.Date;

/**
 * Block end effect
 *
 * @author Azuxul
 * @version 1.0
 */
public class BlockEnd extends BlockEffect {

    private double i;

    public BlockEnd(JumpBlock jumpBlock) {
        super(jumpBlock, BlockType.END);
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
        if (playerBuildingJump.hasStartJump()) {
            playerBuildingJump.setJumpStart(false);

            long jumpTime = new Date().getTime() - playerBuildingJump.getJumpStartTime();

            if (playerBuildingJump.getState().equals(PlayerState.TEST)) {

                JumpMeta jumpMeta = playerBuildingJump.getCurrentJump().getJumpMeta();

                if (jumpMeta.getTestTime() > jumpTime || jumpMeta.getTestTime() < 0) {
                    playerBuildingJump.getPlayerIfOnline().sendMessage("NEW BEST TEST TIME");
                    jumpMeta.setTestTime(jumpTime);
                } else {
                    playerBuildingJump.getPlayerIfOnline().sendMessage("OLD TEST TIME IS BETTER :" + jumpMeta.getTestTime() / 1000);
                }

            }

            playerBuildingJump.getPlayerIfOnline().sendMessage("END");
            playerBuildingJump.getPlayerIfOnline().sendMessage(Double.toString((double) jumpTime / 1000));
        } else if (playerBuildingJump.getJumpStartTime() + 2000 < new Date().getTime()) {
            playerBuildingJump.getPlayerIfOnline().sendMessage("JUMP PAS START");
            playerBuildingJump.setJumpStartTime(new Date().getTime());
        }
    }

    @Override
    public void playerUp(PlayerBuildingJump playerBuildingJump) {

    }

    @Override
    public void playerDown(PlayerBuildingJump playerBuildingJump) {

    }
}
