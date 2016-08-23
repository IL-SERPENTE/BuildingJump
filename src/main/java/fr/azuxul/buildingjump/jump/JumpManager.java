package fr.azuxul.buildingjump.jump;

import fr.azuxul.buildingjump.BuildingJumpGame;

/**
 * Jump Manager
 *
 * @author Azuxul
 * @version 1.0
 */
public class JumpManager {

    private final JumpAreaGenerator jumpAreaGenerator;

    public JumpManager(BuildingJumpGame buildingJumpGame) {

        this.jumpAreaGenerator = new JumpAreaGenerator(buildingJumpGame);
    }
}
