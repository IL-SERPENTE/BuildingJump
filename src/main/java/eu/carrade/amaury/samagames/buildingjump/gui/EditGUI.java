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
package eu.carrade.amaury.samagames.buildingjump.gui;

import eu.carrade.amaury.samagames.buildingjump.BuildingJump;
import eu.carrade.amaury.samagames.buildingjump.jumps.Jump;
import eu.carrade.amaury.samagames.buildingjump.jumps.PastedJump;
import eu.carrade.amaury.samagames.buildingjump.utils.Utils;
import fr.zcraft.zlib.components.gui.ActionGui;
import fr.zcraft.zlib.components.gui.Gui;
import fr.zcraft.zlib.components.gui.GuiAction;
import fr.zcraft.zlib.components.gui.PromptGui;
import fr.zcraft.zlib.tools.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;


public class EditGUI extends ActionGui
{
    private final static int COL_METADATA = 1;
    private final static int COL_POINTS   = 4;
    private final static int COL_WORLD    = 7;


    @Override
    protected void onUpdate()
    {
        setTitle(ChatColor.BLACK + "Outils de parcours");
        setHeight(6);

        final PastedJump pJump = getPastedJump();
        final Jump jump = pJump.getJump();


        // Jump metadata

        action("rename", COL_METADATA + 9, new ItemStackBuilder(Material.BOOK_AND_QUILL)
                .title(ChatColor.BLUE, "Renommer votre parcours")
                .longLore(ChatColor.GRAY, "Cliquez ici pour renommer votre parcours. Un parcours avec un nom original est plus sympa :)")
                .loreLine()
                .lore(ChatColor.GRAY + "Nom actuel : " + ChatColor.WHITE + jump.getName())
                .hideAttributes()
        );

        action("difficulty", COL_METADATA + 18, new ItemStackBuilder(itemForDifficulty(jump.getDifficulty()))
                .title(ChatColor.GREEN, "Difficulté du parcours")
                .longLore(ChatColor.GRAY, "Précisez la difficulté estimée de votre parcours. Il est important de bien le renseigner : c'est ce sur quoi la recherche de jumps adaptés se base.")
                .loreLine()
                .loreLine(ChatColor.GOLD, "Cliquez pour changer")
                .loreLine(jump.getDifficulty() == Jump.JumpDifficulty.EASY ? ChatColor.YELLOW : ChatColor.GRAY, "» Facile")
                .loreLine(jump.getDifficulty() == Jump.JumpDifficulty.NORMAL ? ChatColor.YELLOW : ChatColor.GRAY, "» Normal")
                .loreLine(jump.getDifficulty() == Jump.JumpDifficulty.HARD ? ChatColor.YELLOW : ChatColor.GRAY, "» Difficile")
                .hideAttributes()
        );

        action("", COL_METADATA + 27, new ItemStackBuilder(itemForDuration(jump.getDuration()))
                .title(ChatColor.GREEN, "Durée du parcours")
                .longLore(ChatColor.GRAY, "Précisez la durée approximative nécessaire pour réaliser votre parcours.")
                .loreLine()
                .loreLine(ChatColor.GOLD, "Déterminé automatiquement")
                .loreLine(jump.getDuration() == null ? ChatColor.YELLOW : ChatColor.GRAY, "» Indéterminé")
                .loreLine(jump.getDuration() == Jump.JumpDuration.VERY_SHORT ? ChatColor.YELLOW : ChatColor.GRAY, "» Très court (moins de 2 minutes)")
                .loreLine(jump.getDuration() == Jump.JumpDuration.SHORT ? ChatColor.YELLOW : ChatColor.GRAY, "» Court (2-5 minutes)")
                .loreLine(jump.getDuration() == Jump.JumpDuration.LONG ? ChatColor.YELLOW : ChatColor.GRAY, "» Long (5-15 minutes)")
                .loreLine(jump.getDuration() == Jump.JumpDuration.VERY_LONG ? ChatColor.YELLOW : ChatColor.GRAY, "» Très long (15-30 minutes)")
                .hideAttributes()
        );

        action("publish", COL_METADATA + 36, new ItemStackBuilder(Material.MAGMA_CREAM)
                        .title(ChatColor.GOLD, "Publier le parcours")
                        .longLore(ChatColor.GRAY, "Cliquez pour publier votre parcours.")
                        .loreLine()
                        .longLore(ChatColor.GRAY, "Vous devrez tout d'abord " + ChatColor.WHITE + "le réaliser une fois" + ChatColor.GRAY + ", pour estimer sa durée.")
                        .longLore(ChatColor.GRAY, "Ensuite il sera publié pour être joué par tous !")
                        .loreLine()
                        .longLore(ChatColor.GRAY, "Une fois publié, un parcours ne peut plus être modifié sans ête dépublié.")
                        .hideAttributes()
        );


        // Jump locations

        action("set_spawn", COL_POINTS + 9, new ItemStackBuilder(Material.END_CRYSTAL)
                .title(ChatColor.YELLOW, "Modifier le point d'apparition")
                .longLore(ChatColor.GRAY, "Cliquez ici pour modifier le point d'apparition. C'est l'endroit où les joueurs arrivant sur votre parcours seront téléportés.")
                .loreLine()
                .longLore(ChatColor.WHITE + "Cliquez " + ChatColor.GRAY + "pour définir le point d'apparition à votre position courante.")
                .hideAttributes()
        );

        action("get_begin_end", COL_POINTS + 18, new ItemStackBuilder(Material.IRON_PLATE)
                .title(ChatColor.YELLOW, "Début ou fin du parcours")
                .longLore(ChatColor.GRAY, "Déposez cet plaque de pression au début et à la fin du parcours pour le délimiter. Le premier placé sera le début, l'autre la fin.")
                .loreLine()
                .longLore(ChatColor.WHITE + "Cliquez " + ChatColor.GRAY + "pour obtenir l'élément.")
                .hideAttributes()
        );

        action("get_checkpoint", COL_POINTS + 27, new ItemStackBuilder(Material.GOLD_PLATE)
                .title(ChatColor.YELLOW, "Étape de parcours")
                .longLore(ChatColor.GRAY, "Déposez cet plaque de pression à chaque étape (checkpoint) du parcours. Le premier placé devra être accédé en premier, puis le second, etc.")
                .longLore(ChatColor.GRAY, "Vous pourrez après coup réordonner les étapes (élément ci-dessous).")
                .loreLine()
                .longLore(ChatColor.WHITE + "Cliquez " + ChatColor.GRAY + "pour obtenir l'élément.")
                .hideAttributes()
        );

        action("manage_checkpoints", COL_POINTS + 36, new ItemStackBuilder(Material.STRUCTURE_VOID)
                .title(ChatColor.GREEN, "Réorganiser les étapes")
                .longLore(ChatColor.GRAY, "Cliquez ici pour ouvrir une interface vous permettant de réorganiser et renommer les étapes de votre parcours sans tout replacer à la main.")
                .hideAttributes()
        );


        // Jump world tools

        action("jump_time", COL_WORLD + 9, new ItemStackBuilder(Material.WATCH)
                .title(ChatColor.YELLOW, "Changer l'heure")
                .longLore(ChatColor.GRAY, "Cliquez pour changer l'heure de votre parcours.")
                .loreLine()
                .loreLine(ChatColor.GOLD, "Cliquez pour changer")
                .loreLine(jump.getTime() == 18000 ? ChatColor.YELLOW : ChatColor.GRAY, "» 00:00")
                .loreLine(jump.getTime() == 21000 ? ChatColor.YELLOW : ChatColor.GRAY, "» 03:00")
                .loreLine(jump.getTime() == 0 ? ChatColor.YELLOW : ChatColor.GRAY, "» 06:00")
                .loreLine(jump.getTime() == 3000 ? ChatColor.YELLOW : ChatColor.GRAY, "» 09:00")
                .loreLine(jump.getTime() == 6000 ? ChatColor.YELLOW : ChatColor.GRAY, "» 12:00")
                .loreLine(jump.getTime() == 9000 ? ChatColor.YELLOW : ChatColor.GRAY, "» 15:00")
                .loreLine(jump.getTime() == 12000 ? ChatColor.YELLOW : ChatColor.GRAY, "» 18:00")
                .loreLine(jump.getTime() == 15000 ? ChatColor.YELLOW : ChatColor.GRAY, "» 21:00")
                .hideAttributes()
        );

        action("jump_weather", COL_WORLD + 18, new ItemStackBuilder(itemForWeather(jump.getWeather()))
                .title(ChatColor.YELLOW, "Changer la météo")
                .longLore(ChatColor.GRAY, "Cliquez ici pour changer la météo sur votre parcours.")
                .loreLine()
                .loreLine(ChatColor.GOLD, "Cliquez pour changer")
                .loreLine(jump.getWeather() == Jump.JumpWeather.SUN ? ChatColor.YELLOW : ChatColor.GRAY, "» Ensoleillé")
                .loreLine(jump.getWeather() == Jump.JumpWeather.RAIN ? ChatColor.YELLOW : ChatColor.GRAY, "» Pluvieux")
                .loreLine(jump.getWeather() == Jump.JumpWeather.SNOW ? ChatColor.YELLOW : ChatColor.GRAY, "» Neigeux")
                .loreLine(jump.getWeather() == Jump.JumpWeather.THUNDER ? ChatColor.YELLOW : ChatColor.GRAY, "» Orageux")
                .loreLine(jump.getWeather() == Jump.JumpWeather.THUNDER_WITHOUT_RAIN ? ChatColor.YELLOW : ChatColor.GRAY, "» Orageux, sans pluie")
                .hideAttributes()
        );

        action("jump_biome", COL_WORLD + 27, new ItemStackBuilder(Material.PAPER)
                .title(ChatColor.YELLOW, ChatColor.STRIKETHROUGH + "Changer le biome")
                .loreLine(ChatColor.DARK_GRAY, "FONCTIONNALITÉ À VENIR")
                .longLore(ChatColor.GRAY, "Cliquez ici pour changer le biome de votre parcours.")
                .loreLine()
                .loreLine(ChatColor.GOLD, "Cliquez pour changer")
                .loreLine(ChatColor.YELLOW, "» Plaine")
                .loreLine(ChatColor.GRAY, "» Forêt")
                .loreLine(ChatColor.GRAY, "» Montagnes")
                .loreLine(ChatColor.GRAY, "» Et d'autres à venir")
                .hideAttributes()
        );


        action("quit", getSize() - 1, new ItemStackBuilder(Material.BARRIER)
                .title(ChatColor.RED, "Retour à l'accueil")
                .longLore(ChatColor.GRAY, "Enregistre votre parcours et retourne à l'accueil du BuildingJump")
                .hideAttributes()
        );
    }


    private PastedJump getPastedJump()
    {
        return BuildingJump.get().getGame().getPlayer(getPlayer().getUniqueId()).getCurrentJump();
    }

    private Jump getJump()
    {
        return getPastedJump().getJump();
    }


    private Material itemForDifficulty(Jump.JumpDifficulty difficulty)
    {
        switch (difficulty)
        {
            case EASY:
                return Material.CLAY_BRICK;

            case NORMAL:
                return Material.IRON_INGOT;

            case HARD:
                return Material.GOLD_INGOT;
        }

        return null;
    }

    private ItemStack itemForDuration(Jump.JumpDuration duration)
    {
        PotionType type = null;

        if (duration == null)
        {
            type = PotionType.INVISIBILITY;
        }
        else
        {
            switch (duration)
            {
                case VERY_SHORT:
                    type = PotionType.JUMP;
                    break;
                case SHORT:
                    type = PotionType.LUCK;
                    break;
                case LONG:
                    type = PotionType.STRENGTH;
                    break;
                case VERY_LONG:
                    type = PotionType.INSTANT_DAMAGE;
                    break;
            }
        }

        final ItemStack item = new ItemStack(Material.TIPPED_ARROW);
        final PotionMeta meta = (PotionMeta) item.getItemMeta();

        meta.setBasePotionData(new PotionData(type, false, false));
        item.setItemMeta(meta);

        return item;
    }

    private Material itemForWeather(Jump.JumpWeather weather)
    {
        switch (weather)
        {
            case SUN:                  return Material.DOUBLE_PLANT;
            case RAIN:                 return Material.WATER_BUCKET;
            case SNOW:                 return Material.SNOW_BALL;
            case THUNDER:              return Material.BLAZE_ROD;
            case THUNDER_WITHOUT_RAIN: return Material.BLAZE_ROD;
            default:                   return null;
        }
    }


    @GuiAction ("rename")
    protected void rename()
    {
        Gui.open(getPlayer(), new PromptGui(name ->
        {
            getJump().setName(name);
            BuildingJump.get().getSidebarsManager().refreshSidebar(getPlayer());
        }, getJump().getName()), this);
    }

    @GuiAction ("difficulty")
    protected void difficulty(InventoryClickEvent ev)
    {
        final int direction = ev.isRightClick() ? -1 : 1;

        getJump().setDifficulty(Utils.getNextElement(getJump().getDifficulty(), direction));
        update();
    }

    @GuiAction ("jump_time")
    protected void jump_time(InventoryClickEvent ev)
    {
        final int direction = ev.isRightClick() ? -1 : 1;

        getPastedJump().setTime((getJump().getTime() + 3000 * direction) % 24000);
        update();
    }

    @GuiAction ("jump_weather")
    protected void jump_weather(InventoryClickEvent ev)
    {
        final int direction = ev.isRightClick() ? -1 : 1;

        getPastedJump().setWeather(Utils.getNextElement(getJump().getWeather(), direction));
        update();
    }
}
