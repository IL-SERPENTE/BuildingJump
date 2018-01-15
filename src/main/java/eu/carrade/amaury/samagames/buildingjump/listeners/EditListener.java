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

import eu.carrade.amaury.samagames.buildingjump.events.PlayerEntersStateEvent;
import eu.carrade.amaury.samagames.buildingjump.game.BuildingJumpPlayer;
import eu.carrade.amaury.samagames.buildingjump.gui.edit.EditGUI;
import eu.carrade.amaury.samagames.buildingjump.jumps.JumpPlate;
import fr.zcraft.zlib.components.gui.Gui;
import fr.zcraft.zlib.tools.items.ItemStackBuilder;
import fr.zcraft.zlib.tools.items.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class EditListener extends ByStateListener implements Listener
{
    private static final String JUMP_EDIT_OPENER_NAME = ChatColor.AQUA + "Outils de parcours" + ChatColor.GRAY + " (cliquez)";

    public EditListener()
    {
        super(BuildingJumpPlayer.PlayerState.EDIT);
    }


    private ItemStack getEditGuiOpener()
    {
        return new ItemStackBuilder(Material.NETHER_STAR)
                .title(JUMP_EDIT_OPENER_NAME)
                .longLore(ChatColor.GRAY, "Cliquez ici pour accéder aux options du parcours et à d'autres outils")
                .hideAttributes()
                .item();
    }

    @EventHandler
    public void onStateEnter(final PlayerEntersStateEvent ev)
    {
        if (!check(ev)) return;

        ev.getPlayer().getInventory().setItem(8, getEditGuiOpener());
    }

    @EventHandler
    void onPlayerInteract(final PlayerInteractEvent ev)
    {
        if (!check(ev)) return;

        if (ItemUtils.areSimilar(ev.getItem(), getEditGuiOpener()))
        {
            Gui.open(ev.getPlayer(), new EditGUI());
            ev.setCancelled(true);
        }
    }

    @EventHandler
    void onInventoryClose(final InventoryCloseEvent ev)
    {
        if (!check(ev)) return;

        // We check if the tools item is still there, if not we re-add it
        boolean found = false;

        for (ItemStack stack : ev.getPlayer().getInventory())
        {
            if (ItemUtils.areSimilar(stack, getEditGuiOpener()))
            {
                found = true;
                break;
            }
        }

        if (!found)
        {
            ev.getPlayer().getInventory().setItem(8, getEditGuiOpener());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent ev)
    {
        if (!check(ev)) return;

        final JumpPlate plateType = JumpPlate.getPlateFor(ev.getItemInHand());
        if (plateType == null) return;

        try
        {
            getBJPlayer(ev.getPlayer()).getCurrentJump().addPoint(plateType, ev.getBlockPlaced().getLocation());
        }
        catch (IllegalStateException e)
        {
            ev.setCancelled(true);
            ev.getPlayer().sendMessage(ChatColor.RED + "Vous ne pouvez pas placer ceci. " + e.getMessage());
        }
    }
}
