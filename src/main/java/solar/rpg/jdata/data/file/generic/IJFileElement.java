package solar.rpg.jdata.data.file.generic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.generic.JDataField;
import solar.rpg.jdata.data.variants.JDataType;
import solar.rpg.jdata.data.variants.JTextVariant;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Represents any generic data element which has a unique name, and value(s).
 * This class can used to represent a complex / multivariate data type in a {@link JDataField}.
 * Fields can be accessed by name or by index.
 * Values are stored as {@link JTextVariant} objects.
 *
 * @author jskinner
 * @since 1.0.0
 */
public interface IJFileElement extends Iterable<IJFileElement> {

    /**
     * @return Field information about this file element.
     */
    @Nullable
    JDataField getFieldInfo();

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
    List<IJFileElement> getChildren();

    /**
     * Throws {@code IllegalAccessException} if this element contains a value, not children.
     *
     * @return Number of child elements.
     */
    int getChildrenCount();

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

    @Override
    default Iterator<IJFileElement> iterator() {
        return getChildren().iterator();
    }
}
