package fr.azuxul.buildingjump.jump;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.bukkit.Material;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    public static Jump loadJumpFromFile(File file) {

        JsonObject json = null;
        try {
            json = new JsonParser().parse(new JsonReader(new FileReader(file))).getAsJsonObject();
        } catch (FileNotFoundException e) {
            return null;
        }

        Set<JumpBlock> blocks = new HashSet<>();

        for (JsonElement element : json.get("blocks").getAsJsonArray()) {
            JsonObject object = element.getAsJsonObject();
            blocks.add(new JumpBlock(Material.getMaterial(object.get("id").getAsInt()), object.get("value").getAsByte(), BlockType.values()[object.get("type").getAsInt()], object.get("z").getAsInt(), object.get("y").getAsInt(), object.get("z").getAsInt()));
        }

        return new Jump(json.get("name").getAsString(), UUID.fromString(json.get("owner-uuid").getAsString()), json.get("size").getAsInt(), blocks);
    }
}
