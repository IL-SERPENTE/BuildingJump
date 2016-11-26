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

import eu.carrade.amaury.samagames.buildingjump.gui.EditGUI;
import fr.zcraft.zlib.components.gui.Gui;
import fr.zcraft.zlib.tools.items.ItemStackBuilder;
import fr.zcraft.zlib.tools.items.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class EditInventoryHandler extends InventoryHandler
{
    private static final String JUMP_EDIT_OPENER_NAME = ChatColor.AQUA + "Outils de parcours" + ChatColor.GRAY + " (cliquez)";

    private ItemStack getEditGuiOpener()
    {
        return new ItemStackBuilder(Material.NETHER_STAR)
                .title(JUMP_EDIT_OPENER_NAME)
                .longLore(ChatColor.GRAY, "Cliquez ici pour accéder aux options du parcours et à d'autres outils")
                .hideAttributes()
                .item();
    }

    @Override
    public void setup(Player player)
    {
        player.getInventory().setItem(8, getEditGuiOpener());
    }

    @Override
    void onPlayerInteract(PlayerInteractEvent ev)
    {
        if (ItemUtils.areSimilar(ev.getItem(), getEditGuiOpener()))
        {
            Gui.open(ev.getPlayer(), new EditGUI());
            ev.setCancelled(true);
        }
    }

    @Override
    void onInventoryClose(InventoryCloseEvent ev)
    {
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
}
