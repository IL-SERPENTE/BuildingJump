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
    private boolean loaded;

    public Jump(String name, UUID owner, int size, Map<JumpLocation, JumpBlock> blocks, BuildingJumpGame buildingJumpGame, boolean newJump) {

        this.name = name;
        this.owner = owner;
        this.size = size;
        this.blocks = blocks;
        this.buildingJumpGame = buildingJumpGame;

        this.effectBlocks = new HashMap<>();
        this.firstInit = newJump;
    }

    public Jump(String name, UUID owner, int size, Map<JumpLocation, JumpBlock> blocks, BuildingJumpGame buildingJumpGame) {

        this(name, owner, size, blocks, buildingJumpGame, false);
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

    public void update(Block updatedBlock, BlockType blockType) {

        System.out.println("ok");

        blocks.put(new JumpLocation(updatedBlock.getX() - worldLoc.getBlockX(), updatedBlock.getY() - worldLoc.getBlockY(), updatedBlock.getZ() - worldLoc.getBlockZ()), new JumpBlock(updatedBlock.getType(), updatedBlock.getData(), blockType));
    }
}
