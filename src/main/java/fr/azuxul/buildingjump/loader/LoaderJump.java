package fr.azuxul.buildingjump.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.jump.Jump;
import fr.azuxul.buildingjump.jump.JumpLocation;
import fr.azuxul.buildingjump.jump.JumpMeta;
import fr.azuxul.buildingjump.jump.block.BlockType;
import fr.azuxul.buildingjump.jump.block.JumpBlock;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Azuxul on 27/08/2016.
 */
public class LoaderJump {

    private final BuildingJumpGame buildingJumpGame;

    public LoaderJump(BuildingJumpGame buildingJumpGame) {
        this.buildingJumpGame = buildingJumpGame;
    }

    private static File getFileForID(String id) {
        return new File("jumps/" + id + ".json");
    }

    private static String jumpLocationToStringLocation(JumpLocation jumpLocation) {
        return Integer.toString(jumpLocation.getX()) + "," + Integer.toString(jumpLocation.getY()) + "," + Integer.toString(jumpLocation.getZ());
    }

    private static JumpLocation stringLocationToJumpLocation(String location) {

        String[] loc = location.split(",");

        if (loc.length <= 0) {
            loc = location.split(", ");
            if (loc.length <= 0) {
                return null;
            }
        }

        if (loc.length < 3) {
            return null;
        }

        return new JumpLocation(Integer.parseInt(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]));
    }

    private static String spawnLocationToStringLocation(Location spawn) {
        return Double.toString(spawn.getX()) + "," + Double.toString(spawn.getY()) + "," + Double.toString(spawn.getZ()) + "," + Float.toString(spawn.getYaw()) + "," + Float.toString(spawn.getPitch());
    }

    private static Location stringLocationToSpawnLocation(String location) {

        String[] loc = location.split(",");

        if (loc.length <= 0) {
            loc = location.split(", ");
            if (loc.length <= 0) {
                return null;
            }
        }

        if (loc.length < 5) {
            return null;
        }

        return new Location(null, Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Float.parseFloat(loc[3]), Float.parseFloat(loc[4]));
    }

    public Jump loadFormFile(UUID uuid) {

        FileReader fileReader = null;

        try {
            fileReader = new FileReader(getFileForID(""));

            JsonElement element = new JsonParser().parse(fileReader);

            if (!(element == null || element.isJsonNull()))
                return loadFormJSON((JsonObject) element);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null)
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    public void saveToFile(Jump jump) {

    }

    public Jump loadFormJSON(JsonObject json) {

        Map<JumpLocation, JumpBlock> blocks = new HashMap<>();

        for (JsonElement element : json.get("blocks").getAsJsonArray()) {
            JsonObject object = element.getAsJsonObject();

            JumpBlock jumpBlock = new JumpBlock(Material.getMaterial(object.get("id").getAsInt()), object.get("value").getAsByte(), BlockType.values()[object.get("type").getAsInt()]);

            for (JsonElement loc : object.get("locations").getAsJsonArray()) {
                blocks.put(stringLocationToJumpLocation(loc.getAsString()), jumpBlock);
            }
        }

        JumpMeta jumpMeta = new JumpMeta("", UUID.fromString(json.get("owner-uuid").getAsString()), 0, json.get("name").getAsString(), -1, -1);

        return new Jump(jumpMeta, json.get("size").getAsInt(), blocks, buildingJumpGame, false, stringLocationToSpawnLocation((json.get("spawn").getAsString())));

    }

    public JsonObject saveToJSON(Jump jump) {

        return null;
    }
}
