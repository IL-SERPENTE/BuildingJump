package fr.azuxul.buildingjump.jump;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import fr.azuxul.buildingjump.BuildingJumpGame;
import org.bukkit.Material;

import java.io.*;
import java.util.*;

/**
 * Jump file loader
 *
 * @author Azuxul
 * @version 1.0
 */
public class JumpLoader {

    private JumpLoader() {
    }

    public static Jump loadJumpFromFile(File file, BuildingJumpGame buildingJumpGame) {

        JsonObject json;
        try {
            JsonElement element = new JsonParser().parse(new JsonReader(new FileReader(file)));

            if (element == null || element.isJsonNull())
                return null;
            else
                json = element.getAsJsonObject();
        } catch (FileNotFoundException e) {
            return null;
        }

        Map<JumpLocation, JumpBlock> blocks = new HashMap<>();

        for (JsonElement element : json.get("blocks").getAsJsonArray()) {
            JsonObject object = element.getAsJsonObject();

            JumpBlock jumpBlock = new JumpBlock(Material.getMaterial(object.get("id").getAsInt()), object.get("value").getAsByte(), BlockType.values()[object.get("type").getAsInt()]);

            for (JsonElement loc : object.get("locations").getAsJsonArray()) {
                blocks.put(stringLocationToJumpLocation(loc.getAsString()), jumpBlock);
            }

        }

        return new Jump(json.get("name").getAsString(), UUID.fromString(json.get("owner-uuid").getAsString()), json.get("size").getAsInt(), blocks, buildingJumpGame);
    }

    public static void saveJump(Jump jump) {

        JsonObject json = new JsonObject();

        json.add("name", new JsonPrimitive(jump.getName()));
        json.add("owner-uuid", new JsonPrimitive(jump.getOwner().toString()));
        json.add("size", new JsonPrimitive(jump.getSize()));

        JsonArray array = new JsonArray();

        Map<JumpBlock, List<JumpLocation>> blocks = new HashMap<>();

        jump.getBlocks().entrySet().forEach(e -> {

            if (blocks.containsKey(e.getValue())) {
                blocks.get(e.getValue()).add(e.getKey());
            } else {
                List<JumpLocation> jumpLocationList = new ArrayList<>();

                jumpLocationList.add(e.getKey());
                blocks.put(e.getValue(), jumpLocationList);
            }
        });

        blocks.entrySet().forEach(e -> {
            JsonObject object = new JsonObject();

            object.add("id", new JsonPrimitive(e.getKey().getMaterial().getId()));
            object.add("value", new JsonPrimitive(e.getKey().getDataValue()));
            object.add("type", new JsonPrimitive(e.getKey().getBlockType().getId()));

            JsonArray locArray = new JsonArray();

            for (JumpLocation jumpLocation : e.getValue())
                locArray.add(new JsonPrimitive(jumpLocationToStringLocation(jumpLocation)));

            object.add("locations", locArray);

            array.add(object);
        });

        json.add("blocks", array);

        File f = new File("jumps/" + jump.getOwner().toString() + "-jump.json");

        try {

            if (!f.exists()) {
                f.createNewFile();
            }
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));

            writer.write(json.toString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String jumpLocationToStringLocation(JumpLocation jumpLocation) {
        return Integer.toString(jumpLocation.getX()) + "," + Integer.toString(jumpLocation.getY()) + "," + Integer.toString(jumpLocation.getZ());
    }

    public static JumpLocation stringLocationToJumpLocation(String location) {

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
}
