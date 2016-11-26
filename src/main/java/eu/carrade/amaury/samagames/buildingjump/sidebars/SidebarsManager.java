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
package eu.carrade.amaury.samagames.buildingjump.sidebars;

import eu.carrade.amaury.samagames.buildingjump.BuildingJump;
import eu.carrade.amaury.samagames.buildingjump.game.BuildingJumpPlayer;
import fr.zcraft.zlib.components.scoreboard.Sidebar;
import fr.zcraft.zlib.core.ZLibComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;


public class SidebarsManager extends ZLibComponent implements Listener
{
    private final Sidebar hubSidebar = new HubSidebar();
    private final Sidebar editSidebar = new EditSidebar();


    /**
     * Updates the sidebar for the player according to it's state.
     * This will remove the current sidebar then apply the good one. To be
     * called when the state changes.
     *
     * @param player The player.
     */
    public void updateSidebar(Player player)
    {
        updateSidebar(player.getUniqueId());
    }

    /**
     * Updates the sidebar for the player according to it's state.
     * This will remove the current sidebar then apply the good one. To be
     * called when the state changes.
     *
     * @param id The player UUID.
     */
    public void updateSidebar(UUID id)
    {
        updateSidebar(BuildingJump.get().getGame().getPlayer(id));
    }

    /**
     * Updates the sidebar for the player according to it's state.
     * This will remove the current sidebar then apply the good one. To be
     * called when the state changes.
     *
     * @param player The player.
     */
    public void updateSidebar(BuildingJumpPlayer player)
    {
        hubSidebar.removeRecipient(player.getUUID());
        editSidebar.removeRecipient(player.getUUID());

        final Sidebar newSidebar = getSidebarForState(player.getState());

        if (newSidebar != null)
        {
            newSidebar.addRecipient(player.getUUID());
            newSidebar.refresh();
        }
    }


    /**
     * Refreshes the sidebar of this player (if it exists).
     *
     * @param player The player.
     */
    public void refreshSidebar(Player player)
    {
        refreshSidebar(player.getUniqueId());
    }

    /**
     * Refreshes the sidebar of this player (if it exists).
     *
     * @param id The player UUID.
     */
    public void refreshSidebar(UUID id)
    {
        refreshSidebar(BuildingJump.get().getGame().getPlayer(id));
    }

    /**
     * Refreshes the sidebar of this player (if it exists).
     *
     * @param player The player.
     */
    public void refreshSidebar(BuildingJumpPlayer player)
    {
        final Sidebar sidebar = getSidebarForState(player.getState());
        if (sidebar != null) sidebar.refresh();
    }


    /**
     * Returns the sidebar registered for the given state.
     *
     * @param state A state.
     * @return A {@link Sidebar}, or {@code null} if no one is registered for this state.
     */
    private Sidebar getSidebarForState(BuildingJumpPlayer.PlayerState state)
    {
        switch (state)
        {
            case HUB:  return hubSidebar;
            case EDIT: return editSidebar;
            case TEST: return null;
            case PLAY: return null;
            default:   return null;
        }
    }


    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent ev)
    {
        updateSidebar(ev.getPlayer());
    }
}
