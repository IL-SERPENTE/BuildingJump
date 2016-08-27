package fr.azuxul.buildingjump.jump.block;

/**
 * Enum of jump block types
 *
 * @author Azuxul
 * @version 1.0
 */
public enum BlockType {

    NORMAL(0),
    CHECKPONT(1),
    START(2),
    END(3),;

    private final int id;

    BlockType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}