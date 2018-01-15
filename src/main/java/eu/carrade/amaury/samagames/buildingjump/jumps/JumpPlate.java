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

import fr.zcraft.zlib.tools.items.GlowEffect;
import fr.zcraft.zlib.tools.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


/**
 * A jump pressure plate, representing either a start/end or a checkpoint.
 */
public enum JumpPlate
{
    BEGIN_END(
            Material.IRON_PLATE,
            "Début ou fin de parcours",
            "Placez ceci au début puis à la fin de votre parcours."
    ),

    CHECKPOINT(
            Material.GOLD_PLATE,
            "Étape de parcours (checkpoint)",
            "Placez ceci à chaque étape de votre parcours.",
            "",
            "Vous pouvez réorganiser les étapes sans replacer via l'étoile."
    );


    private final Material material;
    private final String displayName;
    private final String[] lore;


    JumpPlate(Material material, String displayName, String... lore)
    {
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
    }


    /**
     * @return An item representing this kind of plate
     */
    public ItemStack getItem()
    {
        ItemStackBuilder builder = new ItemStackBuilder(material)
                .title(ChatColor.YELLOW, displayName)
                .glow(true);

        for (String loreLine : lore)
        {
            builder.longLore(ChatColor.GRAY, loreLine);
        }

        return builder.hideAttributes().item();
    }

    /**
     * Checks if a given item is a jump plate.
     *
     * @param stack The item stack to be tested.
     * @return {@code true} if it's a jump plate, and the right kind of plate.
     */
    public boolean is(ItemStack stack)
    {
        return getPlateFor(stack) == this;
    }

    /**
     * Returns the plate type corresponding to the given item.
     *
     * @param stack An item stack.
     * @return The {@link JumpPlate} type, or {@code null} if it's not a plate.
     */
    public static JumpPlate getPlateFor(ItemStack stack)
    {
        if (stack == null || !GlowEffect.hasGlow(stack)) return null;

        for (JumpPlate plateType : values())
            if (plateType.material == stack.getType())
                return plateType;

        return null;
    }
}
