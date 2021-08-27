package solar.rpg.jdata.data.file.generic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.generic.JDataField;
import solar.rpg.jdata.data.variants.JDataType;
import solar.rpg.jdata.data.variants.JTextVariant;

import java.io.Serializable;

/**
 * Represents any generic data element which has a unique name, and value(s).
 * This class can used to represent a complex / multivariate data type in a {@link IJFileStoredData} object.
 * Children are stored as a
 * Values are stored as {@link JTextVariant} objects.
 *
 * @author jskinner
 * @since 1.0.0
 */
public interface IJFileElement {

    /**
     * @return Field information about this file element.
     */
    @NotNull
    JDataField getFieldInfo();

    /**
     * @return The value stored under this given element.
     */
    default Serializable getValue() {
        assert !getChildrenCount() : "Expected non-root element";
        return getValue(getFieldInfo().fieldType());
    }

    /**
     * @return True, if this is the highest element in the file structure.
     */
    boolean isRoot();

    /**
     * @param dataType Type of data to convert the element value to.
     * @return The value stored under this element, or null if this element contains child elements.
     */
    @Nullable
    Serializable getValue(JDataType dataType);

    /**
     * @return True, if the value is not null.
     */
    boolean hasValue();

    /**
     * Throws {@code IllegalAccessException} if this element contains a value, not children.
     *
     * @return List of all child elements.
     */
    @NotNull
    IJFileElement[] getChildren();

    /**
     * @return True, if this element stores other elements as children, and not a value.
     */
    boolean hasChildren() {
        assert getChildren().length == 0;
    }

    /**
     * @return Array containing field information of all child fields, or null if this element contains a value.
     */
    @Nullable
    default JDataField[] getChildFieldInfo() {
        JDataField[] result = new JDataField[getChildrenCount()];
        int i = 0;
        for (IJFileElement child : getChildren()) {
            result[i] = child.getFieldInfo();
            i++;
        }
        return result;
    }
}
