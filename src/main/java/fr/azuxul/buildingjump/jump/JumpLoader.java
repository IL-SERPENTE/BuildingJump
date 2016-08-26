package fr.azuxul.buildingjump.jump;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import fr.azuxul.buildingjump.BuildingJumpGame;
import org.bukkit.Material;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
            json = new JsonParser().parse(new JsonReader(new FileReader(file))).getAsJsonObject();
        } catch (FileNotFoundException e) {
            return null;
        }

        Set<JumpBlock> blocks = new HashSet<>();

        for (JsonElement element : json.get("blocks").getAsJsonArray()) {
            JsonObject object = element.getAsJsonObject();
            blocks.add(new JumpBlock(Material.getMaterial(object.get("id").getAsInt()), object.get("value").getAsByte(), BlockType.values()[object.get("type").getAsInt()], object.get("x").getAsInt(), object.get("y").getAsInt(), object.get("z").getAsInt()));
        }

        return new Jump(json.get("name").getAsString(), UUID.fromString(json.get("owner-uuid").getAsString()), json.get("size").getAsInt(), blocks, buildingJumpGame);
    }

    public static void saveJump(Jump jump) {

        JsonObject json = new JsonObject();

        json.add("name", new JsonPrimitive(jump.getName()));
        json.add("owner-uuid", new JsonPrimitive(jump.getOwner().toString()));
        json.add("size", new JsonPrimitive(jump.getSize()));

        JsonArray array = new JsonArray();

        for (JumpBlock j : jump.getBlocks()) {

            JsonObject object = new JsonObject();

            object.add("id", new JsonPrimitive(j.getMaterial().getId()));
            object.add("value", new JsonPrimitive(j.getDataValue()));
            object.add("type", new JsonPrimitive(j.getBlockType().getId()));
            object.add("x", new JsonPrimitive(j.getX()));
            object.add("y", new JsonPrimitive(j.getY()));
            object.add("z", new JsonPrimitive(j.getZ()));

            array.add(object);
        }

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
}
