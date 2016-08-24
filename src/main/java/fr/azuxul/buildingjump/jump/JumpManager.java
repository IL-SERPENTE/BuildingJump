package fr.azuxul.buildingjump.jump;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
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
    private final Map<PlayerBuildingJump, Jump> jumps;

    public JumpManager(BuildingJumpGame buildingJumpGame) {

        this.jumpAreaGenerator = new JumpAreaGenerator(buildingJumpGame);
        this.jumps = new HashMap<>();
    }

    public Location registerJump(Jump jump, PlayerBuildingJump playerBuildingJump) {

        Location jumpLoc = jumpAreaGenerator.getNextFreeArea();
        jump.registerWorldLoc(jumpLoc);

        jumps.put(playerBuildingJump, jump);
        jump.load();

        return jumpLoc;
    }

    public void unregisterJump(Jump jump) {

        jump.saveBlocks();
    }

    public Jump getPlayerLoadedJump(PlayerBuildingJump playerBuildingJump) {
        return jumps.get(playerBuildingJump);
    }
}
