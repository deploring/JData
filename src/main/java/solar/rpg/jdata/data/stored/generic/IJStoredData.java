package solar.rpg.jdata.data.stored.generic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a piece of multivariate data which is further implemented by a storage medium.
 * They can hold the data in a cached state before committing to the target storag emedium.
 * The lifecycle of these objects is managed by a {@link JStoredDataProcessor}.
 * <p>
 * Implementations of this interface should be aware of their variables and structure, as defined
 * by any given implementations of {@link JDataField}. Each stored data object should contain
 * at least one primary key field, and one non-primary key field.
 *
 * @author jskinner
 * @since 1.0.0
 */
public interface IJStoredData extends Iterable<JDataField> {

    /**
     * @param fieldName Name that uniquely identifies this field.
     * @return {@link JDataField} instance with the given field name.
     */
    @Nullable
    JDataField getField(@NotNull String fieldName);

    /**
     * @param fieldIndex Index of the field.
     * @return {@link JDataField} instance at the given index.
     */
    @NotNull
    JDataField getField(int fieldIndex);

    /**
     * Commits the current state of the field values in memory to the storage medium.
     * This should also reset <em>original values</em>, as they are now unchanged.
     */
    void commit();

    /**
     * Update all values on all {@link JDataField} objects with the latest data from the storage medium.
     */
    void refresh();

    /**
     * Returns all primary {@link JDataField} objects as an array of {@link JDataParameter} objects.
     */
    @NotNull
    JDataParameter[] getPrimaryFieldSearchParams();

    /**
     * Returns an array of {@link JDataParameter} objects containing each primary field, along with provided
     * search values. This can be used for record searches in the stored data object.
     *
     * @param keyValues The primary key values for each parameter.
     */
    @NotNull
    JDataParameter[] getPrimaryFieldSearchParams(@NotNull String[] keyValues);

    /**
     * @return True, if there are any changes to any non-primary data fields.
     */
    boolean canCommit();

}
