package solar.rpg.jdata.data.stored.generic;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.stored.JStoredDataState;

/**
 * {@code JStoredData} is an abstract class which represents a multivariate entity managed by any given storage medium.
 * Implementations of this class should define the structure of the stored data such that new instances can be created
 * and existing instances can be loaded. Instances of this class hold their data in a cached state which can be
 * committed into the storage medium as desired. There is also functionality to roll back cached changes to stored data.
 * Use {@link JStoredDataController} where the management of object lifecycles for multiple instances is needed.
 *
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JStoredData {

    @NotNull
    private JStoredDataState dataState;

    /**
     * Constructs a new {@code JStoredData} instance. The stored data state is set to {@link
     * JStoredDataState#UNITIALISED}, and the instance should not be used until it is initialised. Initialisation is
     * implemented at a lower level by the particular storage medium.
     */
    protected JStoredData()
    {
        dataState = JStoredDataState.UNITIALISED;
    }

    /**
     * Commits the current state of the stored data in memory to the storage medium. This should also reset <em>original
     * values</em>, as they are now unchanged. //TODO: Keep track of original values.
     *
     * @throws IllegalStateException Cannot commit in current state.
     * @see JStoredDataState#canCommit()
     */
    public final void commit()
    {
        if (!getStoredDataState().canCommit()) throw new IllegalStateException("Cannot commit in current state");
        onCommit();
    }

    /**
     * Child classes should implement appropriate logic to commit the current state of the stored data in memory to the
     * specific storage medium.
     */
    protected abstract void onCommit();

    /**
     * Refreshes the current state of the stored data in memory with the latest data from the storage medium.
     *
     * @throws IllegalStateException Cannot reload in current state.
     * @see JStoredDataState#canRefresh() ()
     */
    public final void reload()
    {
        if (!getStoredDataState().canCommit()) throw new IllegalStateException("Cannot commit in current state");
        onRefresh();
    }

    /**
     * Child classes should implement appropriate logic to refresh the stored data with the latest data from the storage
     * medium.
     */
    protected abstract void onRefresh();

    /**
     * Deletes this stored data from the storage medium.
     *
     * @throws IllegalStateException Cannot delete a stored object already marked as {@link JStoredDataState#REMOVED}.
     */
    public final void delete()
    {
        switch (getStoredDataState()) {
            case UNCHANGED, CREATED, CHANGED -> setStoredDataState(JStoredDataState.REMOVED);
            case REMOVED -> throw new IllegalStateException("Cannot remove an already-removed stored object");
        }

        onDelete();
    }

    /**
     * Child classes should implement appropriate logic to delete this stored data from the storage medium.
     */
    protected abstract void onDelete();

    @NotNull
    public final JStoredDataState getStoredDataState()
    {
        return dataState;
    }

    protected final void setStoredDataState(@NotNull JStoredDataState dataState)
    {
        if (dataState == JStoredDataState.UNITIALISED) throw new IllegalArgumentException("Invalid stored data state");
        this.dataState = dataState;
    }
}
