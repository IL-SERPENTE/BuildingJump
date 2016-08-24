package fr.azuxul.buildingjump.jump;

import fr.azuxul.buildingjump.BuildingJumpGame;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Jump Manager
 *
 * @author Azuxul
 * @version 1.0
 */
public class JumpManager {

    private final JumpAreaGenerator jumpAreaGenerator;
    private final Map<Jump, Location> jumps;

    public JumpManager(BuildingJumpGame buildingJumpGame) {

        this.jumpAreaGenerator = new JumpAreaGenerator(buildingJumpGame);
        this.jumps = new HashMap<>();
    }

    public Location registerJump(Jump jump) {

        Location jumpLoc = jumpAreaGenerator.getNextFreeArea();

        jumps.put(jump, jumpLoc);

        return jumpLoc;
    }
}
