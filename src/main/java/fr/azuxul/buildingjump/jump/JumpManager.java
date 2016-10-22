package fr.azuxul.buildingjump.jump;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.loader.LoaderJump;
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
    private final LoaderJump loaderJump;
    private final Map<UUID, Jump> jumps;

    public JumpManager(BuildingJumpGame buildingJumpGame) {

        this.buildingJumpGame = buildingJumpGame;
        this.jumpAreaGenerator = new JumpAreaGenerator(buildingJumpGame);
        this.jumps = new HashMap<>();
        loaderJump = new LoaderJump(buildingJumpGame);
    }

    public Jump loadJumpSave(UUID uuid) {

        return loaderJump.loadFormFile(uuid);
    }

    public UUID getNewJumpUUID() {
        return loaderJump.getNewUUID();
    }

    public Location registerJump(Jump jump) {

        Location jumpLoc = jumpAreaGenerator.getNextFreeArea();
        jump.registerWorldLoc(jumpLoc);

        jumps.put(jump.getJumpMeta().getUuid(), jump);
        jump.load();

        return jumpLoc.clone();
    }

    public Map<UUID, Jump> getJumps() {
        return jumps;
    }

    public void unregisterJump(Jump jump) {

        jumps.remove(jump.getJumpMeta().getUuid());

        loaderJump.saveToFile(jump);
    }
}
