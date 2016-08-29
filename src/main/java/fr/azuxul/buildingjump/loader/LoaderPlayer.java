package fr.azuxul.buildingjump.loader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.player.PlayerBuildingJump;
import fr.azuxul.buildingjump.player.PlayerData;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * Load player info
 *
 * @author Azuxul
 * @version 1.0
 */
public class LoaderPlayer {

    private static final String BASE_DIRECTORY = "buildingjump/players/";

    private final BuildingJumpGame buildingJumpGame;

    public LoaderPlayer(BuildingJumpGame buildingJumpGame) {
        this.buildingJumpGame = buildingJumpGame;
        initDirectory();
    }

    private void initDirectory() {
        File baseDir = new File(BASE_DIRECTORY);

        if (!baseDir.exists())
            baseDir.mkdirs();
    }

    private File getPlayerFile(UUID uuid) {
        return new File(BASE_DIRECTORY + uuid.toString() + ".json");
    }

    public PlayerData getPlayerData(PlayerBuildingJump playerBuildingJump) {

        FileReader fileReader = null;

        try {
            fileReader = new FileReader(getPlayerFile(playerBuildingJump.getUUID()));

            JsonElement element = new JsonParser().parse(fileReader);

            if (!(element == null || element.isJsonNull()))
                return new Gson().fromJson(element, PlayerData.class);

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

    public void savePlayerData(PlayerBuildingJump playerBuildingJump) {
        FileWriter fileWriter = null;

        try {
            File jumpFile = getPlayerFile(playerBuildingJump.getUUID());

            if (!jumpFile.exists())
                jumpFile.createNewFile();

            fileWriter = new FileWriter(jumpFile);
            fileWriter.write(new Gson().toJson(playerBuildingJump.getPlayerGameData()));

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
}
