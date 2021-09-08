package solar.rpg.jdata.data.file.generic;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.variants.JDataType;

import java.io.Serializable;

/**
 * This interface represents an {@link IJFileNode} that stores a text value, and does not link to other nodes.
 * Consequently, text nodes are exclusively the children of {@link IJFileElement} nodes and are used to store data.
 *
 * @author jskinner
 * @see IJFileElement
 * @since 1.0.0
 */
public interface IJFileTextNode extends IJFileNode {

    /**
     * @return The value stored by this text node, converted to the data type defined in the field info.
     */
    @NotNull
    default Serializable getValue() {
        return getValue(getFieldInfo().fieldType());
    }

    /**
     * @param dataType Type of data to convert the text node's stored value to.
     * @return The value stored by this text node, converted to the given data type.
     * @throws AssertionError This text node does not normally store a value.]
     * @see #hasValue()
     */
    @NotNull
    Serializable getValue(JDataType dataType);

    /**
     * Sets the value stored by this text node.
     *
     * @param value The value for the text node to store, under the data type defined in the field info.
     */
    void setValue(@NotNull Serializable value);

    /**
     * @return True, if a value is normally stored under this text node.
     */
    default boolean hasValue() {
        return getFieldInfo().fieldType() != JDataType.NULL;
    }
}
