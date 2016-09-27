package fr.azuxul.buildingjump.player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Saved player data
 *
 * @author Azuxul
 * @version 1.0
 */
public class PlayerData {

    private final List<UUID> jumpsUUID;
    private final Map<UUID, Long> jumpsTime;

    public PlayerData(List<UUID> jumpsUUID, Map<UUID, Long> jumpsTime) {

        this.jumpsUUID = jumpsUUID;
        this.jumpsTime = jumpsTime;
    }

    public List<UUID> getJumpsUUID() {
        return jumpsUUID;
    }

    public Map<UUID, Long> getJumpsTime() {
        return jumpsTime;
    }
}
