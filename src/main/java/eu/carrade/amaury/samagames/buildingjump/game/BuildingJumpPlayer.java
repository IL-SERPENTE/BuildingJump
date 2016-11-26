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
package eu.carrade.amaury.samagames.buildingjump.game;

import eu.carrade.amaury.samagames.buildingjump.BuildingJump;
import eu.carrade.amaury.samagames.buildingjump.jumps.Jump;
import eu.carrade.amaury.samagames.buildingjump.jumps.PastedJump;
import fr.zcraft.zlib.components.worker.WorkerCallback;
import fr.zcraft.zlib.tools.PluginLogger;
import fr.zcraft.zlib.tools.players.ReducedDebugInfo;
import fr.zcraft.zlib.tools.runners.RunTask;
import net.samagames.api.games.GamePlayer;
import net.samagames.tools.chat.ActionBarAPI;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class BuildingJumpPlayer extends GamePlayer
{
    private PlayerState state = null;

    private final Set<Jump> jumps = new HashSet<>();
    private int maxJumps = 3; // TODO based on rank

    private PastedJump currentJump = null;


    public BuildingJumpPlayer(Player player)
    {
        super(player);
        setState(PlayerState.HUB);
    }

    public PlayerState getState()
    {
        return state;
    }

    public PastedJump getCurrentJump()
    {
        return currentJump;
    }

    public Set<Jump> getJumps()
    {
        return Collections.unmodifiableSet(jumps);
    }

    public int getJumpsCount()
    {
        return jumps.size();
    }

    public int getMaxJumps()
    {
        return maxJumps;
    }

    @Override
    public void handleLogin(boolean reconnect)
    {
        super.handleLogin(reconnect);

        switchToHub();
        ReducedDebugInfo.setForPlayer(getPlayerIfOnline(), true);

        BuildingJump.get().getJumpsLoader().loadJumpsFor(getUUID(), new WorkerCallback<Set<Jump>>() {
            @Override
            public void finished(Set<Jump> jumps)
            {
                if (jumps != null)
                    jumps.forEach(BuildingJumpPlayer.this::registerJump);
            }

            @Override
            public void errored(Throwable e)
            {
                PluginLogger.error("Unable to load jumps for player {0} (UUID {1})!", e, getOfflinePlayer().getName(), getOfflinePlayer().getUniqueId());
                getPlayerIfOnline().kickPlayer(ChatColor.RED + "Erreur lors du chargement des parcours, connexion impossible");
            }
        });
    }

    @Override
    public void handleLogout()
    {
        super.handleLogout();

        switchToHub();
    }

    /**
     * Creates a jump for this player and teleports him to this new jump.
     */
    public void createJump()
    {
        ActionBarAPI.sendPermanentMessage(getPlayerIfOnline(), ChatColor.YELLOW + "Une petite seconde... Nous posons les bases de votre parcours.");

        RunTask.nextTick(() ->
        {
            try
            {
                BuildingJump.get().getJumpsManager().createJump(getUUID(), (pastedJump) ->
                {
                    ActionBarAPI.removeMessage(getPlayerIfOnline(), true);
                    switchToEdition(pastedJump);
                });
            }
            catch (IllegalStateException e)
            {
                ActionBarAPI.removeMessage(getPlayerIfOnline(), true);
                getPlayerIfOnline().sendMessage(ChatColor.RED + "Impossible de créer un nouveau parcours (maximum : " + getMaxJumps() + "). Désolé.");
            }
        });
    }

    private void setState(PlayerState state)
    {
        if (this.state != null)
        {
            BuildingJump.get().getInventoriesManager().getHandler(this.state).tearDown(getPlayerIfOnline());
        }

        PluginLogger.info("Switching player {0} state to {1}", getOfflinePlayer().getName(), state.name());

        this.state = state;
        BuildingJump.get().getInventoriesManager().getHandler(this.state).setup(getPlayerIfOnline());
    }

    public void switchToHub()
    {
        setState(PlayerState.HUB);

        getPlayerIfOnline().setGameMode(GameMode.ADVENTURE);
        getPlayerIfOnline().teleport(BuildingJump.get().getSpawn());

        if (currentJump != null)
        {
            currentJump.removePlayer(getPlayerIfOnline());
            currentJump = null;
        }
    }

    public void switchToEdition(final PastedJump jump)
    {
        if (currentJump != null)
            currentJump.removePlayer(getPlayerIfOnline());

        setState(PlayerState.EDIT);
        currentJump = jump;

        BuildingJump.get().getSidebarsManager().updateSidebar(getPlayerIfOnline());

        getPlayerIfOnline().setGameMode(GameMode.CREATIVE);
        getPlayerIfOnline().teleport(jump.getSpawn());

        currentJump.addPlayer(getPlayerIfOnline());

        // TODO adapt world border
    }

    public void registerJump(final Jump jump)
    {
        jumps.add(jump);
    }

    public enum PlayerState
    {
        HUB, EDIT, TEST, PLAY
    }
}
