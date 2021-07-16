package solar.rpg.jdata.data.generic;

/**
 * Represents a piece of relational data which is further implemented by the storage medium.
 * They can hold the data in a cached state before committing to the target file.
 * The lifecycle of these objects is managed by a {@link JStoredDataProcessor}.
 *
 * @author jskinner
 * @since 1.0.0
 */
public interface JStoredData<T> {

    /**
     * @param fieldName Name that uniquely identities this field.
     * @return {@link JStoredDataField} instance with the given field name.
     */
    JStoredDataField GetFieldByName(String fieldName);

    /**
     * Commits the current state of the value in memory to the storage medium.
     */
    void Commit();

    /**
     * Update all values on all {$link JStoredDataField} with the latest data from the storage medium.
     */
    void Refresh();
}
