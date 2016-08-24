package fr.azuxul.buildingjump.jump;

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

    private String name;
    private Location worldLoc;
    private boolean loaded;

    public Jump(String name, UUID owner, int size, Set<JumpBlock> blocks) {

        this.name = name;
        this.owner = owner;
        this.size = size;
        this.blocks = blocks;

        this.effectBlocks = new HashSet<>();
    }

    public void registerWorldLoc(Location location) {
        this.worldLoc = location;
    }

    public void load() {

        effectBlocks.clear();

        for (JumpBlock b : blocks) {

            Block block = worldLoc.clone().add(b.getX(), b.getY(), b.getZ()).getBlock();

            block.setTypeIdAndData(b.getMaterial().getId(), b.getDataValue(), true);
            effectBlocks.add(b);
        }

        loaded = true;
    }

    public void save() {

    }
}
