package fr.azuxul.buildingjump.jump;

import fr.azuxul.buildingjump.BuildingJumpGame;
import fr.azuxul.buildingjump.Configuration;
import org.bukkit.Location;

/**
 * Jump area generator
 *
 * @author Azuxul
 * @version 1.0
 */
public class JumpAreaGenerator {

    private final Configuration configuration;

    private final int minSize;

    private int generatedArenas = 0;

    public JumpAreaGenerator(BuildingJumpGame buildingJumpGame) {

        this.configuration = buildingJumpGame.getConfiguration();

        this.minSize = configuration.getHubLocation().getBlockZ() - configuration.getJumpDistanceFromJump() * 2;
    }

    public Location getNextFreeArea() {

        int size = configuration.getJumpMaxSize();

        Location area = configuration.getHubLocation();
        area.setY(70);

        if(generatedArenas == 0) {

            area.setZ(minSize < 0 ? minSize - size : minSize + size);
            area.add(configuration.getJumpDistanceFromHub() + (configuration.getJumpDistanceFromHub() < 0 ? configuration.getJumpDistanceFromHub() - size : configuration.getJumpDistanceFromHub() + size), 0, 0);
        } else {


            int x = configuration.getJumpDistanceFromHub() + (configuration.getJumpDistanceFromHub() < 0 ? configuration.getJumpDistanceFromHub() - size : configuration.getJumpDistanceFromHub() + size) + (generatedArenas%5) * configuration.getJumpDistanceFromJump();

            area.add(x, 0, 0);
            area.setZ((minSize < 0 ? minSize - size : minSize + size) + (generatedArenas - 5 * (generatedArenas%5)) * size);

        }

        generatedArenas++;
        return area;
    }
}
