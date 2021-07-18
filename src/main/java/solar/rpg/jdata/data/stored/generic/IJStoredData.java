package solar.rpg.jdata.data.stored.generic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * Represents a piece of relational data which is further implemented by the storage medium.
 * They can hold the data in a cached state before committing to the target file.
 * The lifecycle of these objects is managed by a {@link JStoredDataProcessor}.
 *
 * @param <T> Stored data objects contain an array {@link JStoredDataField}.
 * @author jskinner
 * @since 1.0.0
 */
public interface IJStoredData<T extends JStoredDataField<?>> extends Iterable<T> {

    /**
     * Returns a {@link JStoredDataField} instance with the given field name.
     *
     * @param fieldName Name that uniquely identities this field.
     */
    @Nullable
    JStoredDataField<?> getFieldByName(@NotNull String fieldName);

    /**
     * Commits the current state of the value in memory to the storage medium.
     */
    void commit();

    /**
     * Update all values on all {$link JStoredDataField} with the latest data from the storage medium.
     */
    void refresh();

    /**
     * Returns an iterator which provides sequential access to all of the stored data field information.
     */
    Iterator<T> iterator();

    /**
     * Returns all primary {@link JStoredDataField} objects as an array of {@link IJDataParameter} objects.
     */
    @NotNull
    IJDataParameter[] getPrimaryFieldSearchParams();

    /**
     * Returns an array of {@link IJDataParameter} objects containing each primary field, along with provided
     * search values.
     *
     * @param keyValues The primary key values for each parameter.
     */
    @NotNull
    IJDataParameter[] getPrimaryFieldSearchParams(@NotNull String[] keyValues);

    /**
     * Returns True if there are any changes to any non-primary data fields.
     */
    default boolean canCommit() {
        for (T dataField : this)
            if (dataField.isChanged())
                assert !dataField.isPrimary() : "Primary fields cannot be changed";
            else return false;
        return true;
    }
}
