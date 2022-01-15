package solar.rpg.jdata.data.stored;

import solar.rpg.jdata.data.stored.generic.JStoredData;

/**
 * Denotes the different states that the data of a {@link JStoredData} object can be in.
 *
 * @author jskinner
 * @since 1.0.0
 */
public enum JStoredDataState {
    /**
     * The stored object holds an existing piece of data, and has not been changed.
     */
    UNCHANGED(false, true),
    /**
     * The stored object holds a new piece of data, whose record(s) are yet to be created through a commit.
     */
    CREATED(true, false),
    /**
     * The stored object holds an existing piece of data, whose data has been changed, and is yet to be committed.
     */
    CHANGED(true, true),
    /**
     * The stored object held an existing piece of data, whose record(s) are yet to be deleted through a commit.
     */
    REMOVED(true, true),
    /**
     * Initial state. The stored data object should not be used until it is initialised; this can either be as a new
     * instance or from existing stored data.
     */
    UNITIALISED(false, false);

    private final boolean requiresCommit, canRefresh;

    JStoredDataState(boolean requiresCommit, boolean canRefresh) {
        this.requiresCommit = requiresCommit;
        this.canRefresh = canRefresh;
    }

    /**
     * @return True, if there are changes to a {@link JStoredData} object that will be lost if not committed.
     */
    public boolean canCommit() {
        return requiresCommit;
    }

    /**
     * @return True, if a {@link JStoredData} object is allowed to refresh in the given state.
     */
    public boolean canRefresh() {
        return canRefresh;
    }
}
