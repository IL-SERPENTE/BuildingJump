package fr.azuxul.buildingjump.event;

import fr.azuxul.buildingjump.BuildingJumpGame;
import org.bukkit.event.Listener;

/**
 * Player events
 *
 * @author Azuxul
 * @version 1.0
 */
public class PlayerEvent implements Listener {

    private final BuildingJumpGame buildingJumpGame;

    public PlayerEvent(BuildingJumpGame buildingJumpGame) {
        this.buildingJumpGame = buildingJumpGame;
    }


}
