package fr.azuxul.buildingjump.loader;

import com.google.gson.*;
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
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Jump loader and saver
 *
 * @author Azuxul
 * @version 1.0
 */
public class LoaderJump {

    private static final String BASE_DIRECTORY = "buildingjump/jumps/";

    private final BuildingJumpGame buildingJumpGame;

    public LoaderJump(BuildingJumpGame buildingJumpGame) {
        this.buildingJumpGame = buildingJumpGame;
        initDirectory();
    }

    private static File getFileForID(UUID uuid) {
        return new File(BASE_DIRECTORY + uuid.toString() + ".json");
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

    private void initDirectory() {
        File baseDir = new File(BASE_DIRECTORY);

        if (!baseDir.exists())
            baseDir.mkdirs();
    }

    public UUID getNewUUID() {

        UUID uuid;

        do {
            uuid = UUID.randomUUID();

        } while (jumpExists(uuid));

        return uuid;
    }

    public boolean jumpExists(UUID uuid) {

        return new File(BASE_DIRECTORY + uuid.toString() + ".json").exists();
    }

    public Jump loadFormFile(UUID uuid) {

        FileReader fileReader = null;

        try {
            fileReader = new FileReader(getFileForID(uuid));

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

        FileWriter fileWriter = null;

        try {
            File jumpFile = getFileForID(jump.getJumpMeta().getUuid());

            if (!jumpFile.exists())
                jumpFile.createNewFile();

            fileWriter = new FileWriter(jumpFile);
            fileWriter.write(saveToJSON(jump).toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public JumpMeta loadJumpMetaFromJSON(JsonObject json) {
        return new JumpMeta(UUID.fromString(json.get("uuid").getAsString()), UUID.fromString(json.get("owner-uuid").getAsString()), json.get("create-time").getAsLong(), json.get("name").getAsString(), json.get("owner-difficulty").getAsInt(), json.get("test-time").getAsLong());
    }

    public Jump loadFormJSON(JsonObject json) {

        Map<JumpLocation, JumpBlock> blocks = new HashMap<>();

        for (JsonElement element : json.get("blocks").getAsJsonArray()) {
            JsonObject object = element.getAsJsonObject();

            for (JsonElement loc : object.get("locations").getAsJsonArray()) {
                JumpLocation jumpLocation = stringLocationToJumpLocation(loc.getAsString());
                JumpBlock jumpBlock = new JumpBlock(Material.getMaterial(object.get("id").getAsInt()), object.get("value").getAsByte(), BlockType.values()[object.get("type").getAsInt()], jumpLocation);
                blocks.put(jumpLocation, jumpBlock);
            }
        }

        return new Jump(loadJumpMetaFromJSON(json), json.get("size").getAsInt(), blocks, buildingJumpGame, false, stringLocationToSpawnLocation(json.get("spawn").getAsString()));

    }

    public JsonObject saveToJSON(Jump jump) {

        JsonObject jsonJump = new JsonObject();

        JumpMeta jumpMeta = jump.getJumpMeta();

        jsonJump.add("uuid", new JsonPrimitive(jumpMeta.getUuid().toString()));
        jsonJump.add("name", new JsonPrimitive(jumpMeta.getName()));
        jsonJump.add("owner-uuid", new JsonPrimitive(jumpMeta.getOwner().toString()));
        jsonJump.add("create-time", new JsonPrimitive(jumpMeta.getCreateDate()));
        jsonJump.add("owner-difficulty", new JsonPrimitive(jumpMeta.getOwnerDifficulty()));
        jsonJump.add("test-time", new JsonPrimitive(jumpMeta.getTestTime()));
        jsonJump.add("size", new JsonPrimitive(jump.getSize()));
        jsonJump.add("spawn", new JsonPrimitive(spawnLocationToStringLocation(jump.getSpawnInJump())));
        jsonJump.add("blocks", getJSONBlocksArray(jump));

        return jsonJump;
    }

    private JsonArray getJSONBlocksArray(Jump jump) {

        JsonArray blockArray = new JsonArray();
        Map<JumpBlock, List<JumpLocation>> blockTypeMap = new HashMap<>();

        jump.getBlocksForSave().entrySet().forEach(e -> {

            if (blockTypeMap.containsKey(e.getValue())) {
                blockTypeMap.get(e.getValue()).add(e.getKey());
            } else {
                List<JumpLocation> jumpLocationList = new ArrayList<>();

                jumpLocationList.add(e.getKey());
                blockTypeMap.put(e.getValue(), jumpLocationList);
            }
        });

        blockTypeMap.entrySet().forEach(e -> {
            if (!e.getKey().getMaterial().equals(Material.AIR) || e.getKey().getBlockType() != BlockType.NORMAL) {
                JsonObject object = new JsonObject();
                JsonArray locArray = new JsonArray();

                object.add("id", new JsonPrimitive(e.getKey().getMaterial().getId()));
                object.add("value", new JsonPrimitive(e.getKey().getDataValue()));
                object.add("type", new JsonPrimitive(e.getKey().getBlockType().getId()));


                for (JumpLocation jumpLocation : e.getValue())
                    locArray.add(new JsonPrimitive(jumpLocationToStringLocation(jumpLocation)));

                object.add("locations", locArray);

                blockArray.add(object);
            }
        });

        return blockArray;
    }
}
