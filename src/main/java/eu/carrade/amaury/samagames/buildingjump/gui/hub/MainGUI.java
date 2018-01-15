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
package eu.carrade.amaury.samagames.buildingjump.gui.hub;

import eu.carrade.amaury.samagames.buildingjump.BuildingJump;
import eu.carrade.amaury.samagames.buildingjump.game.BuildingJumpPlayer;
import fr.zcraft.zlib.components.gui.ActionGui;
import fr.zcraft.zlib.components.gui.GuiAction;
import fr.zcraft.zlib.tools.PluginLogger;
import fr.zcraft.zlib.tools.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;


public class MainGUI extends ActionGui
{
    private BuildingJumpPlayer bjPlayer;

    @Override
    protected void onUpdate()
    {
        setTitle(ChatColor.BLACK + "BuildingJump");
        setHeight(3);

        bjPlayer = BuildingJump.get().getGame().getPlayer(getPlayer().getUniqueId());

        if (bjPlayer == null)
        {
            PluginLogger.error("Unregistered player {0} (UUID {1}), this should not happen", getPlayer().getName(), getPlayer().getUniqueId());
            close();
            return;
        }


        final ItemStackBuilder newJumpItem = new ItemStackBuilder(Material.DIAMOND_PICKAXE)
                .title(ChatColor.GREEN, "Créer un parcours de saut")
                .loreLine(ChatColor.BLUE, "Lancez-vous, créez votre propre parcours !")
                .loreLine()
                .longLore(ChatColor.GRAY, "Vous serez libre de faire ce que vous voulez sur un terrain. Ensuite, tout le monde pourra tenter votre jump !")
                .loreLine()
                .lore(ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Cliquez" + ChatColor.GRAY + " pour créer votre parcours")
                .hideAttributes();

        if (bjPlayer.getJumpsCount() == 0)
        {
            action("new_jump", 12, newJumpItem);
        }
        else if (bjPlayer.getMaxJumps() > 1)
        {
            action("jumps", 12, new ItemStackBuilder(Material.DIAMOND_PICKAXE)
                    .title(ChatColor.GREEN, "Mes parcours")
                    .loreLine(ChatColor.BLUE, "Affiche la liste de vos parcours de saut")
                    .loreLine()
                    .lore(ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Cliquez" + ChatColor.GRAY + " pour voir vos parcours")
                    .hideAttributes()
            );

            if (bjPlayer.getJumpsCount() + 1 != bjPlayer.getMaxJumps())
            {
                action("new_jump", 11, newJumpItem);
            }
        }
        else
        {
            action("my_jump", 12, new ItemStackBuilder(Material.DIAMOND_PICKAXE)
                    .title(ChatColor.GREEN, "Mon parcours")
                    .loreLine(ChatColor.BLUE, "Vous téléporte à votre parcours")
                    .loreLine()
                    .lore(ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Cliquez" + ChatColor.GRAY + " pour être téléporté à votre parcours")
                    .hideAttributes()
            );
        }

        action("play", 14, new ItemStackBuilder(Material.FEATHER)
                .title(ChatColor.GREEN, "Jouer")
                .loreLine(ChatColor.BLUE, "Défiez les parcours des autres joueurs !")
                .loreLine()
                .lore(ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Cliquez" + ChatColor.GRAY + " pour choisir des jumps à défier")
                .hideAttributes()
        );
    }

    @GuiAction("new_jump")
    public void new_jump()
    {
        close();
        bjPlayer.createJump();
    }

    @GuiAction("jumps")
    public void jumps()
    {

    }

    @GuiAction("my_jump")
    public void my_jump()
    {

    }

    @GuiAction("play")
    public void play()
    {

    }
}
