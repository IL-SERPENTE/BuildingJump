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
package eu.carrade.amaury.samagames.buildingjump.inventories;

import eu.carrade.amaury.samagames.buildingjump.BuildingJump;
import eu.carrade.amaury.samagames.buildingjump.game.BuildingJumpPlayer;
import fr.zcraft.zlib.core.ZLibComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;


public class InventoriesManager extends ZLibComponent implements Listener
{
    private Map<BuildingJumpPlayer.PlayerState, InventoryHandler> handlers = new HashMap<>();

    @Override
    protected void onEnable()
    {
        handlers.put(BuildingJumpPlayer.PlayerState.HUB,  new HubInventoryHandler());
        handlers.put(BuildingJumpPlayer.PlayerState.EDIT, new EditInventoryHandler());
        handlers.put(BuildingJumpPlayer.PlayerState.TEST, new TestInventoryHandler());
        handlers.put(BuildingJumpPlayer.PlayerState.PLAY, new PlayInventoryHandler());
    }


    public InventoryHandler getHandler(BuildingJumpPlayer.PlayerState state)
    {
        return handlers.get(state);
    }

    public InventoryHandler getHandler(Player player)
    {
        return getHandler(BuildingJump.get().getGame().getPlayer(player.getUniqueId()).getState());
    }

    public InventoryHandler getHandler(InventoryEvent event)
    {
        return getHandler((Player) event.getView().getPlayer());
    }

    public InventoryHandler getHandler(PlayerEvent event)
    {
        return getHandler(event.getPlayer());
    }


    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent ev)
    {
        getHandler(ev).onInventoryOpen(ev);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent ev)
    {
        getHandler(ev).onInventoryClose(ev);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent ev)
    {
        getHandler(ev).onInventoryClick(ev);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent ev)
    {
        getHandler(ev).onInventoryDrag(ev);
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent ev)
    {
        getHandler(ev).onInventoryInteract(ev);
    }

    @EventHandler
    public void onInventoryCreative(InventoryCreativeEvent ev)
    {
        getHandler(ev).onInventoryCreative(ev);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent ev)
    {
        getHandler(ev).onPlayerInteract(ev);
    }
}
