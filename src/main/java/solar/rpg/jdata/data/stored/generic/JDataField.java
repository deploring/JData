package solar.rpg.jdata.data.stored.generic;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.variants.JDataType;

/**
 * Holds field information related to an {@link IJStoredData} object's structure.
 *
 * @param fieldName The unique name that identifies this field in the associated {@link IJStoredData} object.
 * @param fieldType Type of data stored under this field.
 * @param isPrimary True, if this field is a primary identifier of the associated {@link IJStoredData} object.
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
}
