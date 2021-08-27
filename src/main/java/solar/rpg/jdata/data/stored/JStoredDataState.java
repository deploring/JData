package solar.rpg.jdata.data.stored;

import solar.rpg.jdata.data.stored.generic.IJStoredData;

/**
 * Denotes the different states that the data of a {@link IJStoredData} object can be in.
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
    REMOVED(true, true);

    private final boolean requiresCommit, canReload;

    JStoredDataState(boolean requiresCommit, boolean canReload) {
        this.requiresCommit = requiresCommit;
        this.canReload = canReload;
    }

    /**
     * @return True, if there are changes to the {@link IJStoredData} object that will be lost if not committed.
     */
    public boolean isRequiresCommit() {
        return requiresCommit;
    }

    /**
     * @return True, if the {@link IJStoredData} object is allowed to reload its values in the given state.
     */
    public boolean canReload() {
        return canReload;
    }
}
