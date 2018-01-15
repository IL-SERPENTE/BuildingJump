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
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import eu.carrade.amaury.samagames.buildingjump.BuildingJump;
import eu.carrade.amaury.samagames.buildingjump.game.BuildingJumpPlayer;
import eu.carrade.amaury.samagames.buildingjump.utils.WorldEditUtils;
import fr.zcraft.zlib.components.worker.WorkerCallback;
import fr.zcraft.zlib.core.ZLibComponent;
import fr.zcraft.zlib.tools.Callback;
import fr.zcraft.zlib.tools.PluginLogger;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class JumpsManager extends ZLibComponent
{
    /**
     * The distance between two jumps.
     */
    private final int DISTANCE_BETWEEN_JUMPS = 1000;

    /**
     * The max coordinate to use to place a jump.
     */
    private final int MAX_COORDINATE = 29998000;


    /**
     * The jumps metadata loaded in memory. Map Jump UUID -> jump.
     */
    private Map<UUID, Jump> jumps = new HashMap<>();

    /**
     * The jumps physically pasted somewhere in the world to be played. Map Jump
     * location (min corner) in the world -> PlayedJump object
     */
    private Map<Location, PlayedJump> playedJumps = new HashMap<>();

    /**
     * The jumps physically pasted somewhere to be edited by their owner. Map
     * Jump UUID -> jump location (min corner) in the world.
     */
    private Map<UUID, Location> editedJumps = new HashMap<>();

    /**
     * Locations where a jump is pasted for edition or play.
     */
    private Set<Location> usedLocations = new HashSet<>();

    /**
     * The main world
     */
    private World world = Bukkit.getWorlds().get(0);

    /**
     * Last used location to place a jump.
     */
    private Location lastUsedLocation = null;

    /**
     * The current line used where the jumps are placed. This is the Z coordinate,
     * and the X changes for every new jump.
     *
     * This variable will probably never be updated in production as one line can
     * handle 6 million jumps :D .
     */
    private int currentLine = DISTANCE_BETWEEN_JUMPS;


    @Override
    protected void onDisable()
    {
        for (Jump jump : jumps.values())
            saveJump(jump, null);
    }


    /**
     * Loads a jump in memory.
     *
     * @param jumpID   The jump UUID.
     * @param callback A callback called with the jump object loaded. If no jump
     *                 was found with this UUID, {@code null} will be passed.
     */
    public void loadJump(final UUID jumpID, final Callback<Jump> callback)
    {
        Validate.notNull(jumpID, "Jump UUID cannot be null");

        BuildingJump.get().getJumpsLoader().loadJump(jumpID, new WorkerCallback<Jump>()
        {
            @Override
            public void finished(Jump jump)
            {
                jumps.put(jump.getUniqueID(), jump);
                callback.call(jump);
            }

            @Override
            public void errored(Throwable throwable)
            {
                PluginLogger.error("Unable to load jump with UUID {0}", throwable, jumpID);
                callback.call(null);
            }
        });
    }

    /**
     * Loads all the jumps for the given player in memory.
     *
     * This will only load the jumps object from the database, not the
     * schematic, neither in memory nor in the world.
     *
     * @param playerID The player UUID
     * @param callback A callback called when the jumps are loaded, with the
     *                 amount of jumps loaded as an argument. If the jumps
     *                 cannot be loaded, null is given (not 0).
     */
    public void loadJumpsFor(final UUID playerID, final Callback<Integer> callback)
    {
        Validate.notNull(playerID, "Player UUID cannot be null");

        BuildingJump.get().getJumpsLoader().loadJumpsFor(playerID, new WorkerCallback<Set<Jump>>()
        {
            @Override
            public void finished(Set<Jump> result)
            {
                for (final Jump jump : result)
                    jumps.put(jump.getUniqueID(), jump);

                callback.call(result.size());
            }

            @Override
            public void errored(Throwable exception)
            {
                PluginLogger.error("Unable to load jumps of player with UUID {0}", exception, playerID);
                callback.call(null);
            }
        });
    }

    /**
     * Loads the given jump in the world. This will asynchronously load the
     * schematic, paste it somewhere in the world and then call the callback.
     *
     * If the jump is not already loaded in memory, it is loaded on the fly.
     *
     * @param jumpID     The jump to build.
     * @param forEdition If {@code true}, the jump will be loaded for edition.
     *                   Else, for playing. Only the owner should be teleported
     *                   to a jump for edition.
     *                   In edition mode, if the jump is already pasted somewhere
     *                   to be edited, it will be reused.
     *                   In play mode, a new one will always be pasted, but only
     *                   if the jump is published. Else, the callback will
     *                   receive {@code null} instead of the location.
     * @param callback   A callback to call when the jump is loaded and built.
     *                   The jump spawn location is given as argument.
     *
     * @throws IllegalArgumentException if no jump with this UUID can be found.
     */
    public void buildJump(final UUID jumpID, final boolean forEdition, final Callback<Location> callback) throws IllegalArgumentException
    {
        Validate.notNull(jumpID, "Jump UUID cannot be null");

        if (!jumps.containsKey(jumpID))
            loadJump(jumpID, jump -> buildJump(jump, forEdition, callback));
        else
            buildJump(jumps.get(jumpID), forEdition, callback);
    }

    /**
     * Loads the given jump in the world. This will asynchronously load the
     * schematic, paste it somewhere in the world and then call the callback.
     *
     * If the jump is not already loaded in memory, it is loaded on the fly.
     *
     * @param jump       The jump to build.
     * @param forEdition If {@code true}, the jump will be loaded for edition.
     *                   Else, for playing. Only the owner should be teleported
     *                   to a jump for edition.
     *                   In edition mode, if the jump is already pasted somewhere
     *                   to be edited, it will be reused.
     *                   In play mode, a new one will always be pasted, but only
     *                   if the jump is published. Else, the callback will
     *                   receive {@code null} instead of the location.
     * @param callback   A callback to call when the jump is loaded and built.
     *                   The jump spawn location is given as argument.
     *
     * @throws IllegalArgumentException if no jump with this UUID can be found.
     */
    public void buildJump(Jump jump, boolean forEdition, Callback<Location> callback)
    {
        Validate.notNull(jump, "Jump cannot be null");
        // TODO determine where to place the jump and then...
    }

    /**
     * Creates a jump for the given player.
     *
     * A new jump is created, registered, and then built and registered for edition.
     * The jump is, of course, initially unpublished.
     *
     * If the user cannot create a new jump, an exception will be thrown, and the
     * callback, not called.
     *
     * @param playerID The player UUID
     * @param callback A callback to call when the jump is created, build, and
     *                 saved. The jump default spawn location is given as
     *                 argument.
     */
    public void createJump(UUID playerID, Callback<PastedJump> callback) throws IllegalStateException
    {
        Validate.notNull(playerID, "Player UUID cannot be null");

        final BuildingJumpPlayer player = BuildingJump.get().getGame().getPlayer(playerID);

        if (player.getJumpsCount() == player.getMaxJumps())
            throw new IllegalArgumentException("Player with UUID " + playerID + " cannot create a new jump (max: " + player.getMaxJumps() + ")");

        final Player bPlayer = player.getPlayerIfOnline();
        final String jumpName;

        if (bPlayer != null) jumpName = "Parcours de " + bPlayer.getName();
        else                 jumpName = "Parcours sans nom"; // Just in case

        final Location buildLocation = getNextLocation();


        final Jump jump = new Jump(jumpName, playerID);
        final PastedJump pastedJump = new PastedJump(jump, buildLocation);


        // We save the jump in the runtime list of loaded jumps.
        jumps.put(jump.getUniqueID(), jump);
        player.registerJump(jump);


        // We create the initial base.
        // TODO As for now, it's a simple squared dirt start. In the future, we could replace this with
        // TODO a small base build by our builders, maybe.
        try
        {
            final EditSession session = WorldEditUtils.newEditSession(world);
            final Vector buildLocationVector = BukkitUtil.toVector(buildLocation);
            final Region base = new CuboidRegion(buildLocationVector.subtract(5, 0, 5), buildLocationVector.add(5, 0, 5));

            session.setBlocks(base, new BaseBlock(BlockID.GRASS));
            session.flushQueue();
        }
        catch (MaxChangedBlocksException ignored) {}

        pastedJump.setSpawn(buildLocation.add(.5, 1, .5));

        pastedJump.saveStructure(false, success ->  // success is always true here
        {
            saveJump(jump, (saveSuccess) ->
            {
                if (!saveSuccess)
                    PluginLogger.error("Cannot save jump with ID {0}!", jump.getUniqueID());
            });

            callback.call(pastedJump);
        });
    }

    /**
     * Saves a jump.
     *
     * @param jumpID   The jump UUID to save.
     * @param callback A callback called when the task is completed. Boolean
     *                 passed representing the operation success.
     *
     * @throws IllegalArgumentException if no jump exist with this UUID.
     */
    public void saveJump(UUID jumpID, Callback<Boolean> callback) throws IllegalArgumentException
    {
        Validate.notNull(jumpID);

        if (!jumps.containsKey(jumpID))
            throw new IllegalArgumentException("There is no jump with the UUID " + jumpID);

        saveJump(jumps.get(jumpID), callback);
    }

    /**
     * Saves a jump.
     *
     * @param jump     The jump UUID to save.
     * @param callback A callback called when the task is completed. Boolean
     *                 passed representing the operation success.
     *
     * @throws IllegalArgumentException if no jump exist with this UUID.
     */
    public void saveJump(Jump jump, Callback<Boolean> callback) throws IllegalArgumentException
    {
        Validate.notNull(jump);

        if (callback != null) callback.call(true); // TODO
    }


    /**
     * Determines a free location on the server map where a jump can be pasted,
     * either for edition or to be played.
     *
     * Implementation flaw: the system will break if, on a single server, more
     * than 18 000 000 000 000 jumps are are loaded.
     *
     * @return The next free location where a jump can be placed.
     */
    private Location getNextLocation()
    {
        // First location, always at 0, 64, 1000 (assuming DISTANCE_BETWEEN_JUMPS = 1000).
        if (lastUsedLocation == null)
        {
            lastUsedLocation = new Location(world, 0, 64, currentLine);
        }

        // If we reach the world limit in the positives: go to the negatives.
        // (Hint: this condition will NEVER be successful.)
        else if (lastUsedLocation.getBlockX() >= 0 && lastUsedLocation.getX() + DISTANCE_BETWEEN_JUMPS > MAX_COORDINATE)
        {
            lastUsedLocation.setX(-DISTANCE_BETWEEN_JUMPS);
        }

        // If we reach the world limit in the negatives: go to the next line and start over.
        // (Hint: this condition will be met less times than the previous one.)
        else if (lastUsedLocation.getBlockX() < 0 && lastUsedLocation.getX() - DISTANCE_BETWEEN_JUMPS < MAX_COORDINATE)
        {
            currentLine -= DISTANCE_BETWEEN_JUMPS;
            lastUsedLocation = new Location(world, 0, 64, currentLine);
        }

        // Normal incrementation of the X coordinate (positives).
        else if (lastUsedLocation.getBlockX() >= 0)
        {
            lastUsedLocation.setX(lastUsedLocation.getX() + DISTANCE_BETWEEN_JUMPS);
        }

        // Normal decrementation of the X coordinate (negatives, also probably never executed).
        else
        {
            lastUsedLocation.setX(lastUsedLocation.getX() - DISTANCE_BETWEEN_JUMPS);
        }

        // So many never executed branches here... Ahh, perfectionism.
        return lastUsedLocation.clone();
    }

    public World getWorld()
    {
        return world;
    }
}
