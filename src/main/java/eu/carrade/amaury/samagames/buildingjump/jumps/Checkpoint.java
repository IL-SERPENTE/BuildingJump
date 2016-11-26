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

import org.apache.commons.lang.Validate;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;


/**
 * Represents a checkpoint of a jump.
 *
 * Checkpoints are represented in-game by an iron pressure plate. They are
 * associated with a title, displayed above the checkpoint and as a /title
 * when it is reached, and a description, displayed as a subtitle in the
 * /title when reached. The last checkpoint name is also displayed in the
 * actionbar of the players, so this can also be considered as a section
 * title. As of the first section, I don't really know what's the better
 * option to set its name (use the jump name? Maybe make it configurable
 * in the checkpoints GUI, defaulting to the jump name? I think it's the
 * best solution).
 */
public class Checkpoint
{
    private final List<String> numerals = Arrays.asList(
            "Premier", "Second", "Troisième", "Quatrième", "Cinquième", "Sixième", "Septième", "Huitième", "Neuvième",
            "Dixième", "Onzième", "Douzième", "Treizième", "Quatorzième", "Quinzième", "Seizième", "Dix-septième",
            "Dix-huitième", "Dix-neuvième", "Vingtième", "Vingt-et-unième"
    );

    private Jump jump;

    private String name;
    private String description;

    /**
     * Checkpoint order, first is 0.
     */
    private int order;

    /**
     * Location in the jump reference.
     */
    private Vector location;


    public Checkpoint(Jump jump, Vector location)
    {
        this.jump = jump;
        this.location = location;

        this.order = jump.getCheckpoints().size();

        setName(null);

        description = "";
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        if (name == null)
        {
            if (order < numerals.size())
                this.name = numerals.get(order) + " point de passage";
            else
                this.name = (order + 1) + "ème point de passage";
        }
        else this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        Validate.notNull(description, "Description cannot be null");
        this.description = description;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order; // TODO recheck order of others to get a continuous numerotation
    }

    public Vector getLocation()
    {
        return location;
    }

    public void setLocation(Vector location)
    {
        this.location = location;
    }
}
