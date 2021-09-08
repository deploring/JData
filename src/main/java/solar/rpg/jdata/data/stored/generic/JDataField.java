package solar.rpg.jdata.data.stored.generic;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.variants.JDataType;

import java.util.Objects;

/**
 * Holds field information in the context of an unchanging data structure.
 *
 * @param fieldName The name that identifies this field.
 * @param fieldType Type of data stored under this field.
 * @param isPrimary True, if this field is a primary identifier of the associated data type.
 * @author jskinner
 * @since 1.0.0
 */
public record JDataField(@NotNull String fieldName, @NotNull JDataType fieldType, boolean isPrimary) {

    /**
     * @param fieldName Field name identifier.
     * @param fieldType Type of data stored under this field.
     * @param isPrimary True, if this field is a primary identifier of the associated stored data.
     */
    public JDataField(@NotNull String fieldName, @NotNull JDataType fieldType, boolean isPrimary) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.isPrimary = isPrimary;
    }

    /**
     * @param fieldName Field name identifier.
     * @param fieldType Type of data stored under this field.
     */
    public JDataField(@NotNull String fieldName, @NotNull JDataType fieldType) {
        this(fieldName, fieldType, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JDataField that = (JDataField) o;
        return isPrimary == that.isPrimary && fieldName.equals(that.fieldName) && fieldType == that.fieldType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, fieldType, isPrimary);
    }
}
