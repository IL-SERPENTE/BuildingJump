/*
 * Copyright or © or Copr. AmauryCarrade (2015)
 * 
 * http://amaury.carrade.eu
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package eu.carrade.amaury.samagames.buildingjump.jumps;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import eu.carrade.amaury.samagames.buildingjump.BuildingJump;
import eu.carrade.amaury.samagames.buildingjump.utils.WorldEditUtils;
import fr.zcraft.zlib.tools.Callback;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * A PastedJump is a representation of a jump placed in a place in the world to
 * be either edited or played. It contains a reference to the underlying jump,
 * plus methods to paste and save the structure.
 *
 * This is also used to display the world border in edition mode, and to handle
 * jump events related to the underlying jump, converting the real world
 * location to relative ones transmitted to the jump to be handled.
 *
 * If a {@link Jump} is always loaded once, there can be multiple {@link
 * PastedJump} at the same time. There will be either none or one {@link
 * PastedJump} for edition, if the jump is not published, either none, one or
 * more {@link PlayedJump PlayedJumps} for playing, in the other case.
 *
 * @see Jump Jump, the underlying object storing abstract informations totally
 * unlinked to any world.
 */
public class PastedJump
{
    private static final double ARMOR_STAND_TITLE_SHIFT = 2.5;
    private static final double ARMOR_STAND_SUBTITLE_SHIFT = 2.2;

    private static final String ARMOR_STAND_BEGINNING_TITLE = ChatColor.BOLD + "Départ";
    private static final String ARMOR_STAND_END_TITLE = ChatColor.BOLD + "Arrivée";
    private static final String ARMOR_STAND_BEGINNING_END_SUBTITLE = ChatColor.WHITE + StringUtils.repeat("\u25A3", 16) + "  ";


    private Jump jump;

    private Location pasteLocation;
    private Vector pasteVector;

    private Region region;

    private Set<Player> playersInJump = new HashSet<>();

    private Map<Checkpoint, ArmorStand> checkpointsTitlesStands   = new HashMap<>();
    private Map<Checkpoint, ArmorStand> checkpointsSubtitlesStands = new HashMap<>();

    private ArmorStand beginTitleStand    = null;
    private ArmorStand beginSubtitleStand = null;
    private ArmorStand endTitleStand      = null;
    private ArmorStand endSubtitleStand   = null;


    public PastedJump(Jump jump, Location pasteLocation)
    {
        this.jump = jump;
        this.pasteLocation = pasteLocation.clone();
        this.pasteVector = BukkitUtil.toVector(pasteLocation);

        final double halfSize = (double) jump.getSize() / 2d;

        this.region = new CuboidRegion(
                (World) BukkitUtil.getLocalWorld(pasteLocation.getWorld()),
                new Vector(pasteLocation.getX() - halfSize, 0, pasteLocation.getZ() - halfSize),
                new Vector(pasteLocation.getX() + halfSize, 256, pasteLocation.getZ() + halfSize)
        );
    }

    /**
     * Pastes the structure at the good location, reading it from the underlying {@link Jump}.
     */
    public void pasteStructure()
    {
        final EditSession session = WorldEditUtils.newEditSession(pasteLocation.getWorld());

        try
        {
            WorldEditUtils.pasteClipboard(session, jump.getData(), pasteVector, true);
        }
        catch (MaxChangedBlocksException ignored) {}
    }

    /**
     * Reads the structure in the world and saves it into the underlying {@link Jump},
     * and then into the database if asked.
     *
     * @param saveToDatabase if {@code true}, the content will be saved into the database (in a blob field).
     * @param callback A callback called when the content is saved.
     */
    public void saveStructure(boolean saveToDatabase, Callback<Boolean> callback)
    {
        final EditSession session = WorldEditUtils.newEditSession(pasteLocation.getWorld());
        final ClipboardHolder clipboard = WorldEditUtils.copy(session, region, pasteVector);

        jump.setJumpData(clipboard.getClipboard());

        if (saveToDatabase)
            BuildingJump.get().getJumpsManager().saveJump(jump, callback);
        else
            callback.call(true);
    }

    /**
     * Mark a player as in this jump.
     *
     * @param player The player.
     */
    public void addPlayer(Player player)
    {
        playersInJump.add(player);

        player.setPlayerTime(jump.getTime(), false);
        player.setPlayerWeather(jump.getWeather().getBukkitWeather());
    }

    /**
     * Removes a player from this jump.
     * @param player The player.
     */
    public void removePlayer(Player player)
    {
        playersInJump.remove(player);

        player.resetPlayerTime();
        player.resetPlayerWeather();
    }


    /**
     * Converts a location in the concrete world to a location in the jump.
     * The internal locations are relative to the smallest corner (this one being
     * 0;0;0).
     *
     * @param location A location.
     * @return The location (vector) in the jump reference.
     *
     * @see #getLocationRelativeToVector(Vector) Inverted conversion.
     * @see #getBukkitVectorRelativeToLocation(Location) Conversion to Bukkit vectors
     */
    private Vector getVectorRelativeToLocation(Location location)
    {
        return BukkitUtil.toVector(location).subtract(region.getMinimumPoint());
    }

    /**
     * Converts a location in the concrete world to a location in the jump.
     * The internal locations are relative to the smallest corner (this one being
     * 0;0;0).
     *
     * @param location A location.
     * @return The location (Bukkit vector) in the jump reference.
     *
     * @see #getLocationRelativeToVector(Vector) Inverted conversion.
     * @see #getVectorRelativeToLocation(Location) Conversion to WorldEdit vectors
     */
    private org.bukkit.util.Vector getBukkitVectorRelativeToLocation(Location location)
    {
        return location.toVector().subtract(BukkitUtil.toLocation(pasteLocation.getWorld(), region.getMinimumPoint()).toVector());
    }

    /**
     * Converts an internal location (in the jump reference) to a concrete location
     * based on this paste location.
     *
     * @param vector A vector in the jump reference (i.e. 0;0;0 = jump lower point).
     * @return The corresponding location in the concrete world.
     *
     * @see #getVectorRelativeToLocation(Location) Inverted conversion.
     */
    private Location getLocationRelativeToVector(Vector vector)
    {
        return BukkitUtil.toLocation(pasteLocation.getWorld(), region.getMinimumPoint().add(vector));
    }

    /**
     * Converts an internal location (in the jump reference) to a concrete location
     * based on this paste location.
     *
     * @param vector A vector in the jump reference (i.e. 0;0;0 = jump lower point).
     * @return The corresponding location in the concrete world.
     *
     * @see #getVectorRelativeToLocation(Location) Inverted conversion.
     */
    private Location getLocationRelativeToVector(org.bukkit.util.Vector vector)
    {
        return BukkitUtil.toLocation(pasteLocation.getWorld(), region.getMinimumPoint().add(BukkitUtil.toVector(vector)));
    }

    /**
     * @return The jump spawn point, converted to a concrete {@link Location}.
     */
    public Location getSpawn()
    {
        return getLocationRelativeToVector(jump.getSpawn());
    }

    /**
     * Sets the spawn point of the underlying jump at a concrete location in the world.
     *
     * @param location The location.
     */
    public void setSpawn(Location location)
    {
        jump.setSpawn(getVectorRelativeToLocation(location));
    }

    /**
     * @return The jump beginning, converted to a concrete {@link Location}.
     */
    public Location getBeginning()
    {
        return getLocationRelativeToVector(jump.getBeginning());
    }

    /**
     * Sets the beginning point of the underlying jump at a concrete location in the world.
     *
     * @param location The location.
     */
    public void setBeginning(Location location)
    {
        jump.setBeginning(getVectorRelativeToLocation(location));
    }

    /**
     * @return The jump end point, converted to a concrete {@link Location}.
     */
    public Location getEnd()
    {
        return getLocationRelativeToVector(jump.getEnd());
    }

    /**
     * Sets the end point of the underlying jump at a concrete location in the world.
     *
     * @param location The location.
     */
    public void setEnd(Location location)
    {
        jump.setEnd(getVectorRelativeToLocation(location));
    }

    /**
     * Sets the weather for this jump.
     * @param weather The weather.
     */
    public void setWeather(Jump.JumpWeather weather)
    {
        jump.setWeather(weather);
        playersInJump.forEach(player -> player.setPlayerWeather(weather.getBukkitWeather()));
    }

    /**
     * Sets the time for this jump.
     * @param time The time.
     */
    public void setTime(long time)
    {
        time %= 24000;
        if (time < 0) time += 24000;

        jump.setTime(time);

        final long finalTime = time;
        playersInJump.forEach(player -> player.setPlayerTime(finalTime, false));
    }

    /**
     * Adds a point (checkpoint or begin/end) to the jump. Creates titles & subtitles entities, too.
     *
     * @param pointType The point type.
     * @param plateLocation The paste location.
     *
     * @throws IllegalStateException if the block can't be placed (with error message to be displayed to the user)
     */
    public void addPoint(JumpPlate pointType, Location plateLocation) throws IllegalStateException
    {
        String title = null;
        String subtitle = null;
        boolean isBegin = false;
        Checkpoint checkpoint = null;

        switch (pointType)
        {
            case BEGIN_END:
                if (jump.getBeginning() == null)
                {
                    jump.setBeginning(getVectorRelativeToLocation(plateLocation));
                    title = ARMOR_STAND_BEGINNING_TITLE;
                    isBegin = true;
                }
                else if (jump.getEnd() == null)
                {
                    jump.setEnd(getVectorRelativeToLocation(plateLocation));
                    title = ARMOR_STAND_END_TITLE;
                }
                else
                {
                    throw new IllegalStateException("Le début et la fin sont déjà placés. Cassez-en un pour le changer.");
                }

                subtitle = ARMOR_STAND_BEGINNING_END_SUBTITLE; // Two spaces after to avoid tag overflow
                break;

            case CHECKPOINT:
                checkpoint = new Checkpoint(jump, getBukkitVectorRelativeToLocation(plateLocation));

                jump.addCheckpoint(checkpoint);

                title = checkpoint.getName();
                subtitle = checkpoint.getDescription();
                break;
        }

        final ArmorStand titleStand = spawnTitleStand(ChatColor.AQUA, title, plateLocation.clone().add(0.5, ARMOR_STAND_TITLE_SHIFT, 0.5));
        final ArmorStand subtitleStand = spawnTitleStand(ChatColor.YELLOW, subtitle, plateLocation.clone().add(0.5, ARMOR_STAND_SUBTITLE_SHIFT, 0.5));

        switch (pointType)
        {
            case BEGIN_END:
                if (isBegin)
                {
                    this.beginTitleStand = titleStand;
                    this.beginSubtitleStand = subtitleStand;
                }
                else
                {
                    this.endTitleStand = titleStand;
                    this.endSubtitleStand = subtitleStand;
                }
                break;

            case CHECKPOINT:
                this.checkpointsTitlesStands.put(checkpoint, titleStand);
                this.checkpointsSubtitlesStands.put(checkpoint, subtitleStand);
                break;
        }
    }

    private ArmorStand spawnTitleStand(ChatColor color, String title, Location location)
    {
        final ArmorStand titleStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        titleStand.setMarker(true);
        titleStand.setVisible(false);
        titleStand.setGravity(false);

        updateTitleStand(titleStand, color, title);

        return titleStand;
    }

    private void updateTitleStand(ArmorStand titleStand, ChatColor color, String title)
    {
        titleStand.setCustomNameVisible(title != null && !title.isEmpty());
        titleStand.setCustomName(title == null ? "" : color + title);
    }

    /**
     * The armor stand entities are stored in the exported schematic, but the links between the
     * entities and the beginning/end/checkpoints are not. This will reconstruct the links from
     * the stored positions and the saved entities.
     *
     * This method must be called only when the entities are completely pasted.
     */
    public void collectArmorStands()
    {
        final Pair<ArmorStand, ArmorStand> beginStands = collectArmorStands(getBeginning(), ARMOR_STAND_BEGINNING_TITLE, ARMOR_STAND_BEGINNING_END_SUBTITLE);
        final Pair<ArmorStand, ArmorStand> endStands   = collectArmorStands(getEnd(), ARMOR_STAND_END_TITLE, ARMOR_STAND_BEGINNING_END_SUBTITLE);

        beginTitleStand = beginStands.getLeft();
        beginSubtitleStand = beginStands.getRight();

        endTitleStand = endStands.getLeft();
        endSubtitleStand = endStands.getRight();

        for (final Checkpoint checkpoint : jump.getCheckpoints())
        {
            final Pair<ArmorStand, ArmorStand> stands = collectArmorStands(getLocationRelativeToVector(checkpoint.getLocation()), checkpoint.getName(), checkpoint.getDescription());

            checkpointsTitlesStands.put(checkpoint, stands.getLeft());
            checkpointsSubtitlesStands.put(checkpoint, stands.getRight());
        }
    }

    /**
     * Giving a location and a title/subtitle, this will try to find the corresponding armor stands.
     *
     * @param location A location
     * @param expectedTitle The expected title
     * @param expectedSubtitle The expected subtitle
     *
     * @return A pair with the title then the subtitle armor stand.
     */
    private Pair<ArmorStand, ArmorStand> collectArmorStands(Location location, String expectedTitle, String expectedSubtitle)
    {
        final String expectedTitleNF = ChatColor.stripColor(expectedTitle).trim();
        final String expectedSubtitleNF = ChatColor.stripColor(expectedSubtitle).trim();

        final ArmorStand[] titles = {null, null};

        pasteLocation
                .getWorld()
                .getNearbyEntities(location.add(0.5, ARMOR_STAND_TITLE_SHIFT, 0.5), 2, 2, 2)
                .stream()
                .filter(entity -> entity instanceof ArmorStand)
                .forEach(entity ->
                {
                    final String customName = ChatColor.stripColor(entity.getCustomName()).trim();
                    if (customName.equalsIgnoreCase(expectedTitleNF))
                        titles[0] = (ArmorStand) entity;
                    else if (customName.equalsIgnoreCase(expectedSubtitleNF))
                        titles[1] = (ArmorStand) entity;
                });

        return Pair.of(titles[0], titles[1]);
    }

    public Jump getJump()
    {
        return jump;
    }
}
