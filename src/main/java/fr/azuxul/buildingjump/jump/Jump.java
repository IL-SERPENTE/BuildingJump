package fr.azuxul.buildingjump.jump;

import fr.azuxul.buildingjump.BuildingJumpGame;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Jump
 *
 * @author Azuxul
 * @version 1.0
 */
public class Jump {

    private final UUID owner;
    private final Set<JumpBlock> blocks;
    private final Set<JumpBlock> effectBlocks;
    private final int size;
    private final boolean firstInit;
    private final BuildingJumpGame buildingJumpGame;

    private String name;
    private Location worldLoc;
    private boolean loaded;

    public Jump(String name, UUID owner, int size, Set<JumpBlock> blocks, BuildingJumpGame buildingJumpGame, boolean newJump) {

        this.name = name;
        this.owner = owner;
        this.size = size;
        this.blocks = blocks;
        this.buildingJumpGame = buildingJumpGame;

        this.effectBlocks = new HashSet<>();
        this.firstInit = newJump;
    }

    public Jump(String name, UUID owner, int size, Set<JumpBlock> blocks, BuildingJumpGame buildingJumpGame) {

        this(name, owner, size, blocks, buildingJumpGame, false);
    }

    public UUID getOwner() {
        return owner;
    }

    public Set<JumpBlock> getBlocks() {
        return blocks;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void registerWorldLoc(Location location) {
        this.worldLoc = location;
    }

    public void load() {

        if (loaded)
            return;

        effectBlocks.clear();

        for (JumpBlock b : blocks) {

            Block block = worldLoc.clone().add(b.getX(), b.getY(), b.getZ()).getBlock();

            block.setTypeIdAndData(b.getMaterial().getId(), b.getDataValue(), true);
            effectBlocks.add(b);
        }

        if (firstInit) {
            for (int x = -2; x < 2; x++) {
                for (int z = -2; z < 2; z++) {
                    worldLoc.clone().add(x, 0, z).getBlock().setType(buildingJumpGame.getConfiguration().getDefaultPlatformMaterial());
                }
            }
        }

        loaded = true;
    }

    public void update(Location worldLoc) {

    }
}
