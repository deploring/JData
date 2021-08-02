package solar.rpg.jdata.data.stored.generic;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.variants.JDataType;

/**
 * Represents a single column of data of a specific type in a data object.
 *
 * @author jskinner
 * @since 1.0.0
 */
public class JDataField {

    /**
     * Unique name that identifies the field in the {@link IJStoredData} object.
     */
    @NotNull
    private final String fieldName;

    /**
     * The data type stored under this field.
     */
    @NotNull
    private final JDataType fieldType;

    /**
     * Denotes whether this field is a primary identifier of multivariate data.
     * Primary field values cannot be changed once they are set, and are usually determined automatically.
     * An exception for automatic determination of primary keys is metadata, as we may wish to customise this.
     */
    private final boolean isPrimary;

    /**
     * @param fieldName Field name identifier.
     * @param fieldType Type of data stored under this field.
     * @param isPrimary True, if this field is a primary identifier of the associated data.
     */
    public JDataField(@NotNull String fieldName, @NotNull JDataType fieldType, boolean isPrimary) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.isPrimary = isPrimary;
    }

    /**
     * @return The unique name that identifies this field in the associated {@link IJStoredData} object.
     */
    @NotNull
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @return Type of data stored under this field.
     */
    @NotNull
    public JDataType getFieldType() {
        return fieldType;
    }

    /**
     * @return True, if this field is a primary identifier of the associated stored data object.
     */
    public boolean isPrimary() {
        return isPrimary;
    }
}
