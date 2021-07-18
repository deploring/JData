package solar.rpg.jdata.data.stored.generic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a single column of data of a specific type in a {@link IJStoredData} object.
 * This is generic data and is used to cache and push the given data into the storage medium.
 *
 * @param <T> The type of data that is stored under this field.
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JStoredDataField<T> {

    /**
     * Unique name that identifies the field in the storage medium.
     */
    @NotNull
    private final String fieldName;

    /**
     * Denotes whether this field is a primary identifier of a {@link IJStoredData} object.
     * Primary field values cannot be changed once they are set, and are usually determined automatically.
     */
    private final boolean isPrimary;

    /**
     * Holds the given field value in memory until it is committed to the storage medium.
     * The original value is also held in memory so it can be compared for any reason.
     */
    @Nullable
    private T storedValue, originalStoredValue;

    /**
     * @param fieldName Field name identifier.
     * @param isPrimary True, if this field is a primary identifier of the stored data object.
     */
    public JStoredDataField(@NotNull String fieldName, boolean isPrimary) {
        this.fieldName = fieldName;
        this.isPrimary = isPrimary;
    }

    @NotNull
    public String getFieldName() {
        return fieldName;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    @Nullable
    public T getStoredValue() {
        return storedValue;
    }

    /**
     * Sets the current in-memory stored data field value.
     * If the original value has not been set, this is also set.
     *
     * @param storedValue New stored data field value.
     */
    @SuppressWarnings("unchecked") //TODO: Check consequences of this.
    public void setStoredValue(@NotNull Object storedValue) {
        this.storedValue = (T) storedValue;

        if (originalStoredValue == null)
            originalStoredValue = (T) storedValue;
    }

    /**
     * Returns the original stored data field value, even if it has been changed in-memory.
     */
    @Nullable
    public T getOriginalStoredValue() {
        return originalStoredValue;
    }

    /**
     * Returns True if the original stored value does not match the current stored value.
     */
    public boolean isChanged() {
        return originalStoredValue != null && !originalStoredValue.equals(storedValue);
    }

    /**
     * Writes the in-memory value to the original value.
     * This should be called after committing data to the database.
     */
    public void commit() {
        assert isChanged() : "Data field value is not ready for committing";
        assert !isPrimary() : "Primary data fields cannot be committed";

        originalStoredValue = storedValue;
    }
}
