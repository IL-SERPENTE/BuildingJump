package fr.azuxul.buildingjump.jump;

import java.util.UUID;

/**
 * Jump meta data
 *
 * @author Azuxul
 * @version 1.0
 */
public class JumpMeta {

    private final UUID uuid;
    private final UUID owner;
    private final long createDate;

    private String name;
    private int ownerDifficulty;
    private int testTime;

    public JumpMeta(UUID uuid, UUID owner, long createDate, String name, int ownerDifficulty, int testTime) {
        this.uuid = uuid;
        this.owner = owner;
        this.createDate = createDate;
        this.name = name;
        this.ownerDifficulty = ownerDifficulty;
        this.testTime = testTime;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getOwner() {
        return owner;
    }

    public long getCreateDate() {
        return createDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerDifficulty() {
        return ownerDifficulty;
    }

    public void setOwnerDifficulty(byte ownerDifficulty) {
        this.ownerDifficulty = ownerDifficulty;
    }

    public int getTestTime() {
        return testTime;
    }

    public void setTestTime(int testTime) {
        this.testTime = testTime;
    }
}
