package fr.azuxul.buildingjump.player;

import java.util.List;
import java.util.UUID;

/**
 * Saved player data
 *
 * @author Azuxul
 * @version 1.0
 */
public class PlayerData {

    private final List<UUID> jumpsUUID;

    public PlayerData(List<UUID> jumpsUUID) {

        this.jumpsUUID = jumpsUUID;
    }

    public List<UUID> getJumpsUUID() {
        return jumpsUUID;
    }
}
