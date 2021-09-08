package solar.rpg.jdata.data.file.generic;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.stored.generic.JDataField;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * This interface represents an {@link IJFileNode} that acts as an element node, which can link to other nodes.
 * Elements of this type do not necessarily store their own data, but store child nodes as data.
 * This allows an element to form a more complex data structure consisting of other elements and text nodes.
 *
 * @author jskinner
 * @see IJFileTextNode
 * @since 1.0.0
 */
public interface IJFileElement extends IJFileNode {

    /**
     * @return True, if this is the root element.
     */
    boolean isRoot();

    /**
     * @return All child nodes belonging to this element, as an array.
     */
    @NotNull
    IJFileNode[] getChildren();

    /**
     * @param fieldName Field name of the child node to find.
     * @return Child node found under the given field name.
     * @throws NoSuchElementException Child node under the given field name could not be found.
     */
    default IJFileNode getChild(String fieldName) {
        return Arrays.stream(getChildren()).filter(child -> child.getFieldInfo().fieldName().equals(fieldName)).findFirst().orElseThrow();
    }

    /**
     * @return Array containing field metadata of all child nodes.
     * @throws AssertionError Element is expected to have at least one child node.
     */
    @NotNull
    default JDataField[] getChildFieldInfo() {
        assert hasChildren() : "Element does not have children";
        return Arrays.stream(getChildren()).map(IJFileNode::getFieldInfo).toArray(JDataField[]::new);
    }

    /**
     * @return True, if this element has at least one child node.
     */
    default boolean hasChildren() {
        return getChildren().length > 0;
    }
}
