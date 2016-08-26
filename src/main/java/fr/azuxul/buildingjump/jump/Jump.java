package fr.azuxul.buildingjump.jump;

import fr.azuxul.buildingjump.BuildingJumpGame;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Jump
 *
 * @author Azuxul
 * @version 1.0
 */
public class Jump {

    private final UUID owner;
    private final Map<JumpLocation, JumpBlock> blocks;
    private final Map<JumpLocation, JumpBlock> effectBlocks;
    private final int size;
    private final boolean firstInit;
    private final BuildingJumpGame buildingJumpGame;

    private String name;
    private Location worldLoc;
    private Location spawn;
    private boolean loaded;

    public Jump(String name, UUID owner, int size, Map<JumpLocation, JumpBlock> blocks, BuildingJumpGame buildingJumpGame, boolean newJump, Location spawn) {

        this.name = name;
        this.owner = owner;
        this.size = size;
        this.blocks = blocks;
        this.buildingJumpGame = buildingJumpGame;
        this.spawn = spawn;

        this.effectBlocks = new HashMap<>();
        this.firstInit = newJump;
    }

    public Jump(String name, UUID owner, int size, Map<JumpLocation, JumpBlock> blocks, BuildingJumpGame buildingJumpGame) {

        this(name, owner, size, blocks, buildingJumpGame, false, new Location(null, 0, 3, 0, 0, 0));
    }

    public UUID getOwner() {
        return owner;
    }

    public Map<JumpLocation, JumpBlock> getBlocks() {
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
        this.worldLoc = location.clone();
    }

    public void load() {

        if (loaded)
            return;

        effectBlocks.clear();

        blocks.entrySet().forEach(e -> {

            Block block = worldLoc.clone().add(e.getKey().getX(), e.getKey().getY(), e.getKey().getZ()).getBlock();

            block.setTypeIdAndData(e.getValue().getMaterial().getId(), e.getValue().getDataValue(), true);

            if (!e.getValue().getBlockType().equals(BlockType.NORMAL))
                effectBlocks.put(e.getKey(), e.getValue());
        });

        if (firstInit) {
            for (int x = -2; x < 2; x++) {
                for (int z = -2; z < 2; z++) {
                    worldLoc.clone().add(x, 0, z).getBlock().setType(buildingJumpGame.getConfiguration().getDefaultPlatformMaterial());
                    blocks.put(new JumpLocation(x, 0, z), new JumpBlock(buildingJumpGame.getConfiguration().getDefaultPlatformMaterial(), (byte) 0, BlockType.NORMAL));
                }
            }
        }

        loaded = true;
    }

    public boolean update(Block updatedBlock, BlockType blockType) {

        int x = updatedBlock.getX() - worldLoc.getBlockX();
        int y = updatedBlock.getY() - worldLoc.getBlockY();
        int z = updatedBlock.getZ() - worldLoc.getBlockZ();

        if (Math.abs(x) >= size || Math.abs(z) >= size) {
            return false;
        } else {
            blocks.put(new JumpLocation(x, y, z), new JumpBlock(updatedBlock.getType(), updatedBlock.getData(), blockType));
            return true;
        }
    }

    public Location getSpawnInJump() {
        return spawn.clone();
    }

    public Location getSpawn() {
        spawn.setWorld(worldLoc.getWorld());

        Location spawnRealLoc = worldLoc.clone().add(spawn);

        spawnRealLoc.setYaw(spawn.getYaw());
        spawnRealLoc.setPitch(spawn.getPitch());

        return spawnRealLoc;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn.clone().subtract(worldLoc);
        this.spawn.setYaw(spawn.getYaw());
        this.spawn.setPitch(spawn.getPitch());
    }
}
