package fr.azuxul.buildingjump.jump;

import org.bukkit.Location;

import java.util.Objects;

/**
 * JumpLocation
 *
 * @author Azuxul
 * @version 1.0
 */
public class JumpLocation {

    private final int x;
    private final int y;
    private final int z;
    private Location location;

    public JumpLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.location = null;
    }

    public void setJumpCenter(Location location) {
        this.location = location.clone().add(getX(), getY(), getZ());
    }

    public Location getLocation() {
        return location.clone();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        JumpLocation that = (JumpLocation) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
