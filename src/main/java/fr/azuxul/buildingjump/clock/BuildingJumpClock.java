package fr.azuxul.buildingjump.clock;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.jump.block.effect.BlockEffect;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import fr.azuxul.buildingjump.player.PlayerState;
import org.bukkit.Location;

/**
 * Game loop
 *
 * @author Azuxul
 * @version 1.0
 */
public class BuildingJumpClock implements Runnable {

    private final BuildingJumpGame buildingJumpGame;

    public BuildingJumpClock(BuildingJumpGame buildingJumpGame) {

        this.buildingJumpGame = buildingJumpGame;
    }

    @Override
    public void run() {

        buildingJumpGame.getJumpManager().getJumps().entrySet().forEach(j -> j.getValue().getEffectBlocks().values().forEach(blockEffect -> {
            if (blockEffect != null)
                blockEffect.displayParticles();
        }));

        buildingJumpGame.getInGamePlayers().values().stream().filter(playerBuildingJump1 -> playerBuildingJump1.getState().equals(PlayerState.TEST) || playerBuildingJump1.getState().equals(PlayerState.BUILD)).forEach(this::updatePlayer);
    }

    private void updatePlayer(PlayerBuildingJump playerBuildingJump) {
        if (playerBuildingJump.getState().equals(PlayerState.TEST)) {
            Location playerLoc = playerBuildingJump.getPlayerIfOnline().getLocation().getBlock().getLocation();

            if (playerLoc.getY() <= 0) {
                playerBuildingJump.teleportToCheckpoint();
            } else {
                BlockEffect blockEffect = playerBuildingJump.getCurrentJump().getEffectBlocks().get(playerLoc.clone().add(0, -1, 0));
                if (blockEffect != null)
                    blockEffect.playerUp(playerBuildingJump);

                blockEffect = playerBuildingJump.getCurrentJump().getEffectBlocks().get(playerLoc.clone().add(0, 2, 0));
                if (blockEffect != null)
                    blockEffect.playerDown(playerBuildingJump);

                blockEffect = playerBuildingJump.getCurrentJump().getEffectBlocks().get(playerLoc.clone().add(0, 1, 0));
                if (blockEffect != null)
                    blockEffect.playerOn(playerBuildingJump);

                blockEffect = playerBuildingJump.getCurrentJump().getEffectBlocks().get(playerLoc);
                if (blockEffect != null)
                    blockEffect.playerOn(playerBuildingJump);
            }
        }
    }
}
