package fr.azuxul.buildingjump;

import com.google.gson.JsonObject;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;

/**
 * Configuration class of this game
 *
 * @author Azuxul
 * @version 1.0
 */
public class Configuration {

    private final Location hubLocation;
    private final int jumpDistanceFromHub;
    private final int jumpDistanceFromJump;
    private final int jumpMaxSize;

    Configuration(JsonObject jsonConfig) {

        final JsonObject jsonLocations = jsonConfig.getAsJsonObject("locations");
        final JsonObject jsonJumpConfig = jsonConfig.getAsJsonObject("jump-configuration");

        this.hubLocation = LocationUtils.str2loc(jsonLocations.get("hub").getAsString());

        this.jumpDistanceFromHub = jsonJumpConfig.get("hub-distance").getAsInt();
        this.jumpDistanceFromJump = jsonJumpConfig.get("jump-distance").getAsInt();
        this.jumpMaxSize = jsonJumpConfig.get("max-size").getAsInt();
    }

    public Location getHubLocation() {
        return hubLocation.clone();
    }

    public int getJumpMaxSize() {
        return jumpMaxSize;
    }

    public int getJumpDistanceFromHub() {
        return jumpDistanceFromHub;
    }

    public int getJumpDistanceFromJump() {
        return jumpDistanceFromJump;
    }
}
