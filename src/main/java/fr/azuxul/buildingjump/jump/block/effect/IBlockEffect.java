package fr.azuxul.buildingjump.jump.block.effect;

import fr.azuxul.buildingjump.player.PlayerBuildingJump;

/**
 * Interface of effect blocks
 *
 * @author Azuxul
 * @version 1.0
 */
public interface IBlockEffect {

    void displayParticles();

    void displayTitle();

    void playerEnter(PlayerBuildingJump playerBuildingJump);

    void playerUp(PlayerBuildingJump playerBuildingJump);

    void playerDown(PlayerBuildingJump playerBuildingJump);

    void playerLeave(PlayerBuildingJump playerBuildingJump);
}
