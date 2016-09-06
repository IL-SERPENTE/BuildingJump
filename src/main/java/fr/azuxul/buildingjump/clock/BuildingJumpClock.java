package fr.azuxul.buildingjump.clock;

import fr.azuxul.buildingjump.BuildingJumpGame;

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
    }
}
