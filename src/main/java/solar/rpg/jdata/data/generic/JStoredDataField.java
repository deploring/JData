package solar.rpg.jdata.data.generic;

/**
 * Represents a single column of data of a specific type in a {@link JStoredData} object.
 * This is generic data and is used to cache and push the given data into the storage medium.
 *
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JStoredDataField {

    /**
     * Unique name that identifies the field in the storage medium.
     */
    private final String fieldName;

    /**
     * The type of primitive data that will be used in the storage medium.
     */
    private final JStoredDataPrimitiveType fieldType;

    /**
     * Denotes whether this field is a primary identifier of a {@link JStoredData} object.
     */
    private final boolean isPrimary;

    /**
     * Holds the given field value in memory until it is committed to the storage medium.
     */
    private Object storedValue;

    public JStoredDataField(String fieldName, JStoredDataPrimitiveType fieldType, boolean isPrimary) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.isPrimary = isPrimary;
    }

    public JStoredDataField(String fieldName, JStoredDataPrimitiveType fieldType, boolean isPrimary, Object storedValue) {
        this(fieldName, fieldType, isPrimary);
        this.storedValue = storedValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public JStoredDataPrimitiveType getFieldType() {
        return fieldType;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public Object getStoredValue() {
        return storedValue;
    }

    /**
     * Commits the current state of the value in memory to the storage medium.
     */
    public abstract void Commit();

    /**
     * Blows away any uncommitted changes to the value in memory and retrieves the latest value from the storage medium.
     */
    public abstract void Refresh();
}
