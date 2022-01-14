package solar.rpg.jdata.data.stored.generic;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.stored.JStoredDataState;
import solar.rpg.jdata.data.stored.file.attribute.JAttributedField;

/**
 * Represents a piece of multivariate data which is managed by any given storage medium.
 * It can hold the data in a cached state before committing to the target storage medium.
 * The lifecycle of these objects is managed by a {@link JStoredDataController}.
 * !!Implementations of this interface should be aware of their field structure, as defined
 * !!by the given implementations of {@link JAttributedField}. Each stored data object should contain
 * !!at least one primary key field, and one non-primary key field.
 *
 * @author jskinner
 * @since 1.0.0
 */
public interface IJStoredData {

    /**
     * @return The current state of the stored data object.
     * @see JStoredDataState
     */
    @NotNull
    JStoredDataState getStoredDataState();

    /**
     * This should be called whenever there is an update to the stored data state:
     * <ul>
     *     <li>{@link JStoredDataState#UNCHANGED} in the case that we have freshly-reloaded field values.</li>
     *     <li>{@link JStoredDataState#CREATED} in the case that the data is a brand-new record.</li>
     *     <li>{@link JStoredDataState#CHANGED} in the case that there is an update to a field value.</li>
     *     <li>{@link JStoredDataState#REMOVED} in the case that the record is deleted.</li>
     * </ul>
     *
     * @param dataState New stored data state value.
     * @see #onUpdateValue()
     * @see #onDelete()
     */
    void setStoredDataState(JStoredDataState dataState);

    /**
     * @return True, if there are any changes to any non-primary data fields.
     */
    default boolean canCommit() {
        return getStoredDataState().isRequiresCommit();
    }

    /**
     * Commits the current state of the field values in memory to the storage medium.
     * This should also reset <em>original values</em>, as they are now unchanged.
     */
    void commit();

    /**
     * Refreshes all field values with the latest data from the storage medium.
     * The stored data must be associated with a saved record before a refresh can occur.
     * Use {@link JStoredDataState#canReload()} to ensure this.
     */
    void refresh();

    /**
     * Deletes the stored data record from the storage medium.
     * This method should call {@link #onDelete()} when being implemented.
     */
    void delete();

    /**
     * When a value is updated, the stored data state should be changed appropriately.
     * As a result, implementations of this interface should call this virtual method when updating a field value.
     * It can be further overridden if further value updating logic is required for any reason.
     */
    default void onUpdateValue() {
        switch (getStoredDataState()) {
            case UNCHANGED -> setStoredDataState(JStoredDataState.CHANGED);
            case CREATED -> {
                // The stored data record is yet to be created, so a state update is not necessary.
            }
            case CHANGED -> {
                // The stored data has already been changed, so a state update is not necessary.
            }
            case REMOVED -> throw new IllegalStateException("Cannot update values of a removed stored object");
        }
    }

    /**
     * This virtual method should be called by the child implementation if the stored data record
     * is marked for deletion. As a result, this method is closely tied to the {@link #delete()} function.
     * It can be overridden if further value deletion logic is required for any reason.
     */
    default void onDelete() {
        switch (getStoredDataState()) {
            case UNCHANGED, CREATED, CHANGED -> setStoredDataState(JStoredDataState.REMOVED);
            case REMOVED -> throw new IllegalStateException("Cannot remove an already-removed stored object");
        }
    }
}
