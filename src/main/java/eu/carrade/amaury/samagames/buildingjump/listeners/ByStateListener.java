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
package eu.carrade.amaury.samagames.buildingjump.listeners;

import eu.carrade.amaury.samagames.buildingjump.BuildingJump;
import eu.carrade.amaury.samagames.buildingjump.events.PlayerEntersStateEvent;
import eu.carrade.amaury.samagames.buildingjump.events.PlayerLeavesStateEvent;
import eu.carrade.amaury.samagames.buildingjump.game.BuildingJumpPlayer;
import fr.zcraft.zlib.core.ZLibComponent;
import fr.zcraft.zlib.tools.reflection.Reflection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerEvent;

import java.lang.reflect.InvocationTargetException;


/**
 * This kind of listener provides  a check method to execute events only for players in the right state.
 */
public abstract class ByStateListener extends ZLibComponent
{
    private BuildingJumpPlayer.PlayerState state;

    /**
     * Constructs a stated listener. The {@link #check(Event)} methods will
     * return true only if either the event is not a player-based event, or if
     * the player is currently in this state.
     *
     * @param state The player state this listener.
     */
    protected ByStateListener(BuildingJumpPlayer.PlayerState state)
    {
        this.state = state;
    }

    /**
     * Checks if a player is in the right state.
     *
     * @param player A player.
     *
     * @return {@code true} if in the right state.
     */
    protected boolean check(final Player player)
    {
        return getBJPlayer(player).getState() == state;
    }

    /**
     * Checks if an event should be executed, based on the player state.
     *
     * @param ev A player event.
     *
     * @return {@code true} if the event should be executed.
     */
    protected boolean check(final PlayerEvent ev)
    {
        return check(ev.getPlayer());
    }

    /**
     * Checks if an event should be executed, based on the player state.
     *
     * @param ev An inventory event.
     *
     * @return {@code true} if the event should be executed.
     */
    protected boolean check(final InventoryEvent ev)
    {
        return check(((Player) ev.getView().getPlayer()));
    }

    /**
     * Checks if an event should be executed, based on the player state.
     *
     * @param ev A player enters state event.
     *
     * @return {@code true} if the event should be executed.
     */
    protected boolean check(final PlayerEntersStateEvent ev)
    {
        return ev.getNewState() == state;
    }

    /**
     * Checks if an event should be executed, based on the player state.
     *
     * @param ev A player leaves state event.
     *
     * @return {@code true} if the event should be executed.
     */
    protected boolean check(final PlayerLeavesStateEvent ev)
    {
        return ev.getOldState() == state;
    }

    /**
     * Checks if an event should be executed, based on the player state.
     *
     * @param ev An event.
     *
     * @return {@code true} if the event should be executed, i.e. either if this
     * event is not a player-related one (does not have a `getPlayer()` method)
     * or if the player retrieved is not in the right state.
     */
    protected boolean check(final Event ev)
    {
        try
        {
            return check((Player) Reflection.call(ev, "getPlayer"));
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            return false;
        }
    }

    /**
     * Shortcut to get a Building Jump player object.
     *
     * @param player A Bukkit player object.
     * @return The corresponding BuildingJump player object.
     */
    protected BuildingJumpPlayer getBJPlayer(final Player player)
    {
        return BuildingJump.get().getGame().getPlayer(player.getUniqueId());
    }
}
