package solar.rpg.jdata.data.stored.generic;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a single column of data of a specific type in a {@link IJStoredData} object.
 *
 * @param <T> The type of data that is stored under this field.
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JStoredDataField<T> {

    /**
     * Unique name that identifies the field in the {@link IJStoredData} object.
     */
    @NotNull
    private final String fieldName;

    /**
     * Denotes whether this field is a primary identifier of a {@link IJStoredData} object.
     * Primary field values cannot be changed once they are set, and are usually determined automatically.
     * An exception for automatic determination of primary keys is metadata, as we may wish to customise this.
     */
    private final boolean isPrimary;

    /**
     * @param fieldName Field name identifier.
     * @param isPrimary True, if this field is a primary identifier of the associated stored data object.
     */
    public JStoredDataField(@NotNull String fieldName, boolean isPrimary) {
        this.fieldName = fieldName;
        this.isPrimary = isPrimary;
    }

    /**
     * Returns the unique names that identities this field in the associated {@link IJStoredData} object.
     */
    @NotNull
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @return True, if this field is a primary identifier of the associated stored data object.
     */
    public boolean isPrimary() {
        return isPrimary;
    }

    /**
     * @return The {@code Class} of the stored type.
     */
    public abstract Class<T> getTypeClass();
}
