package solar.rpg.jdata.data.file.generic;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.stored.generic.JDataField;

/**
 * This interface represents any type of node stored as text in a file.
 * A node is a basic data type that can store its own data and link to other nodes.
 * Classes implementing {@link IJFileNode} are expected to store field information using {@link JDataField}.
 * Nodes that are linked together can form a more complex data structure.
 *
 * @author jskinner
 * @see JDataField
 * @since 1.0.0
 */
public interface IJFileNode {

    /**
     * @return Field metadata information relating to this node.
     */
    @NotNull
    JDataField getFieldInfo();
}
