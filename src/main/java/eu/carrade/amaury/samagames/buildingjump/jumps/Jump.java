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

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import fr.zcraft.zlib.tools.Callback;
import org.bukkit.WeatherType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * This is a Jump (yes, really).
 *
 * <p>A jump is the internal representation only, you can think about it as
 * the things saved into the database.  All the locations are stored as
 * {@link Vector vectors} and are relative to the lower corner of the
 * area (the point with the lowest coordinates). These locations are
 * stored the same way in {@link Checkpoint checkpoints}.</p>
 *
 * <p>You will, almost, never see this object alone. It will always be associated
 * with a {@link PastedJump}, representing the same jump but somewhere in the
 * concrete, real world, in a totally arbitrary location chosen by
 * {@link JumpsManager#getNextLocation() the jumps manager}.</p>
 *
 * <p>To persist modifications made to the jump, you must either run
 * {@link PastedJump#saveStructure(boolean, Callback) the saveStructure method}
 * in the associated pasted jump, with the first parameter set to {@code true},
 * to save the physical structure, either use the {@link JumpsManager#saveJump(Jump, Callback)}
 * in the jumps manager to avoid the WorldEdit copy of the area.</p>
 *
 * @see PastedJump PastedJump, the bridge between the abstract database-based Jump object and it's
 * location in a real concrete world.
 */
public class Jump
{
    private UUID id;
    private String name;

    private UUID jumpOwner;

    private boolean published;

    private Clipboard jumpData = null;

    private JumpDifficulty difficulty = JumpDifficulty.NORMAL;
    private JumpDuration duration = null;
    private long jumpOwnerTime = 0;

    /*
     * All inside locations are vectors relative to the minimal point of the region.
     */
    private Vector spawn = null;
    private Vector beginning = null;
    private Vector end = null;

    private JumpWeather weather = JumpWeather.SUN;
    private long time = 6000;

    private List<Checkpoint> checkpoints = new ArrayList<>();

    private int jumpSize = 100; // blocks


    /**
     * Constructor for deserialization
     *
     * @param id Jump UUID
     * @param name Jump name
     * @param jumpOwner Jump owner
     * @param difficulty Jump difficulty
     * @param duration Jump duration
     * @param jumpOwnerTime The owner time on this jump
     */
    public Jump(UUID id, String name, UUID jumpOwner, boolean published,
                JumpDifficulty difficulty, JumpDuration duration, long jumpOwnerTime,
                Vector spawn, Vector beginning, Vector end, JumpWeather weather, long time, List<Checkpoint> checkpoints)
    {
        this.id = id;
        this.name = name;
        this.jumpOwner = jumpOwner;
        this.published = published;
        this.difficulty = difficulty;
        this.duration = duration;
        this.jumpOwnerTime = jumpOwnerTime;
        this.spawn = spawn;
        this.beginning = beginning;
        this.end = end;
        this.weather = weather;
        this.time = time;
        this.checkpoints = checkpoints;
    }

    /**
     * Constructor for creation
     * @param name Jump name
     * @param jumpOwner Jump owner
     */
    public Jump(final String name, final UUID jumpOwner)
    {
        this.id = UUID.randomUUID();
        this.name = name;
        this.jumpOwner = jumpOwner;
    }



    /* **  GETTERS & SETTERS  ** */


    public UUID getUniqueID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public UUID getJumpOwner()
    {
        return jumpOwner;
    }

    public boolean isPublished()
    {
        return published;
    }

    public JumpDifficulty getDifficulty()
    {
        return difficulty;
    }

    public JumpDuration getDuration()
    {
        return duration;
    }

    public long getJumpOwnerTime()
    {
        return jumpOwnerTime;
    }

    public Vector getSpawn()
    {
        return spawn;
    }

    public Vector getBeginning()
    {
        return beginning;
    }

    public Vector getEnd()
    {
        return end;
    }

    public JumpWeather getWeather()
    {
        return weather;
    }

    public long getTime()
    {
        return time;
    }

    public List<Checkpoint> getCheckpoints()
    {
        return checkpoints;
    }

    public int getSize()
    {
        return jumpSize;
    }

    public Clipboard getData()
    {
        return jumpData;
    }


    public void setName(String name)
    {
        this.name = name;
    }

    public void setPublished(boolean published)
    {
        this.published = published;
    }

    public void setJumpData(Clipboard jumpData)
    {
        this.jumpData = jumpData;
    }

    public void setDifficulty(JumpDifficulty difficulty)
    {
        this.difficulty = difficulty;
    }

    public void setDuration(JumpDuration duration)
    {
        this.duration = duration;
    }

    public void setJumpOwnerTime(long jumpOwnerTime)
    {
        this.jumpOwnerTime = jumpOwnerTime;
    }

    public void setSpawn(Vector spawn)
    {
        this.spawn = spawn;
    }

    public void setBeginning(Vector beginning)
    {
        this.beginning = beginning;
    }

    public void setEnd(Vector end)
    {
        this.end = end;
    }

    public void setWeather(JumpWeather weather)
    {
        this.weather = weather;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public void setJumpSize(int jumpSize)
    {
        this.jumpSize = jumpSize;
    }


    public enum JumpDifficulty
    {
        EASY("Facile"),
        NORMAL("Moyen"),
        HARD("Difficile");


        private final String name;

        JumpDifficulty(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    public enum JumpDuration
    {
        VERY_SHORT("Très court"),
        SHORT("Court"),
        LONG("Long"),
        VERY_LONG("Très long");


        private final String name;

        JumpDuration(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    public enum JumpWeather
    {
        SUN(WeatherType.CLEAR),
        RAIN(WeatherType.DOWNFALL),
        SNOW(WeatherType.DOWNFALL),
        THUNDER(WeatherType.DOWNFALL),
        THUNDER_WITHOUT_RAIN(WeatherType.CLEAR);

        private final WeatherType bukkitWeather;

        JumpWeather(WeatherType bukkitWeather)
        {
            this.bukkitWeather = bukkitWeather;
        }

        public WeatherType getBukkitWeather()
        {
            return bukkitWeather;
        }
    }
}
