package fr.azuxul.buildingjump.jump;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Jump Manager
 *
 * @author Azuxul
 * @version 1.0
 */
public class JumpManager {

    private final BuildingJumpGame buildingJumpGame;
    private final JumpAreaGenerator jumpAreaGenerator;
    private final Map<PlayerBuildingJump, Jump> jumps;

    public JumpManager(BuildingJumpGame buildingJumpGame) {

        this.buildingJumpGame = buildingJumpGame;
        this.jumpAreaGenerator = new JumpAreaGenerator(buildingJumpGame);
        this.jumps = new HashMap<>();
    }

    public Jump loadJumpSave(UUID uuid) {

        return null;
    }

    public Jump loadJumpSave(PlayerBuildingJump playerBuildingJump) {

        return loadJumpSave(playerBuildingJump.getUUID());
    }

    public Location registerJump(Jump jump, PlayerBuildingJump playerBuildingJump) {

        Location jumpLoc = jumpAreaGenerator.getNextFreeArea();
        jump.registerWorldLoc(jumpLoc);

        jumps.put(playerBuildingJump, jump);
        jump.load();

        return jumpLoc.clone();
    }

    public Map<PlayerBuildingJump, Jump> getJumps() {
        return jumps;
    }

    public void unregisterPlayerJump(PlayerBuildingJump playerBuildingJump) {
        jumps.remove(playerBuildingJump);
    }

    public void unregisterJump(Jump jump) {

        PlayerBuildingJump playerBuildingJump = buildingJumpGame.getPlayer(jump.getJumpMeta().getOwner());

        if (playerBuildingJump == null) {
            jumps.entrySet().stream().filter(e -> e.getValue().equals(jump)).forEach(e -> jumps.remove(e.getKey()));
        } else {
            jumps.remove(playerBuildingJump);
        }
    }

    public Jump getPlayerLoadedJump(PlayerBuildingJump playerBuildingJump) {
        return jumps.get(playerBuildingJump);
    }
}
