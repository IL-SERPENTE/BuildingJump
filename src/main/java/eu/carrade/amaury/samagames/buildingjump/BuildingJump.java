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
package eu.carrade.amaury.samagames.buildingjump;

import eu.carrade.amaury.samagames.buildingjump.dependencies.WorldEditDependency;
import eu.carrade.amaury.samagames.buildingjump.game.BuildingJumpGame;
import eu.carrade.amaury.samagames.buildingjump.inventories.InventoriesManager;
import eu.carrade.amaury.samagames.buildingjump.jumps.JumpsLoader;
import eu.carrade.amaury.samagames.buildingjump.jumps.JumpsManager;
import eu.carrade.amaury.samagames.buildingjump.listeners.EditListener;
import eu.carrade.amaury.samagames.buildingjump.listeners.GenericListener;
import eu.carrade.amaury.samagames.buildingjump.listeners.HubListener;
import eu.carrade.amaury.samagames.buildingjump.listeners.PlayListener;
import eu.carrade.amaury.samagames.buildingjump.listeners.TestListener;
import eu.carrade.amaury.samagames.buildingjump.sidebars.SidebarsManager;
import eu.carrade.amaury.samagames.buildingjump.worldgen.EmptyWorldGenerator;
import fr.zcraft.zlib.components.gui.Gui;
import fr.zcraft.zlib.components.scoreboard.SidebarScoreboard;
import fr.zcraft.zlib.core.ZPlugin;
import fr.zcraft.zlib.tools.PluginLogger;
import fr.zcraft.zlib.tools.runners.RunTask;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Status;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.Vector;


public class BuildingJump extends ZPlugin
{
    private static final Vector HUB_LOCATION = new Vector(0, 64, 0);


    private static BuildingJump instance;

    private WorldEditDependency worldEditDependency;

    private BuildingJumpGame game;
    private JumpsManager jumpsManager;
    private SidebarsManager sidebarsManager;
    private InventoriesManager inventoriesManager;

    private JumpsLoader jumpsLoader;


    /**
     * WARNING: the plugin is loaded at startup, so when this is enabled,
     * no world/scoreboard/etc. is available. Use a task executed later for
     * these things.
     */
    @Override
    public void onEnable()
    {
        instance = this;

        worldEditDependency = loadComponent(WorldEditDependency.class);
        if (!worldEditDependency.isEnabled())
        {
            PluginLogger.error("WorldEdit not found, aborting");
            setEnabled(false);
            return;
        }

        game = new BuildingJumpGame();
        jumpsLoader = loadComponent(JumpsLoader.class);

        SamaGamesAPI.get().getGameManager().registerGame(game);

        // IMPORTANT initialize when the worlds are loaded (remember, plugin loaded at startup).
        RunTask.nextTick(() ->
        {
            loadComponents(Gui.class, SidebarScoreboard.class, GenericListener.class, HubListener.class, EditListener.class, TestListener.class, PlayListener.class);

            jumpsManager       = loadComponent(JumpsManager.class);
            sidebarsManager    = loadComponent(SidebarsManager.class);
            //inventoriesManager = loadComponent(InventoriesManager.class);

            for (World world : getServer().getWorlds())
            {
                world.setTime(6000l);
                world.setWeatherDuration(999999999);
                world.setStorm(false);
                world.setThundering(false);

                world.setGameRuleValue("doDaylightCycle", "false");
                world.setGameRuleValue("doWeatherCycle", "false"); // for 1.11+
            }
        });

        game.getBeginTimer().cancel();
        game.setStatus(Status.WAITING_FOR_PLAYERS);
    }


    public static BuildingJump get()
    {
        return instance;
    }

    public BuildingJumpGame getGame()
    {
        return game;
    }

    public JumpsLoader getJumpsLoader()
    {
        return jumpsLoader;
    }

    public JumpsManager getJumpsManager()
    {
        return jumpsManager;
    }

    public SidebarsManager getSidebarsManager()
    {
        return sidebarsManager;
    }

    public InventoriesManager getInventoriesManager()
    {
        return inventoriesManager;
    }

    public WorldEditDependency getWED()
    {
        return worldEditDependency;
    }

    public Location getSpawn()
    {
        return BuildingJump.HUB_LOCATION.toLocation(BuildingJump.get().getJumpsManager().getWorld());
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(final String worldName, final String id)
    {
        return new EmptyWorldGenerator();
    }
}
