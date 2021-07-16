package solar.rpg.jdata.data.generic;

import java.util.ArrayList;

/**
 * This is a class capable of processing relational data implemented by the given methods.
 * The currently supported storage methods are:
 * <ul>
 *     <li></li>
 *     <li></li>
 * </ul>
 *
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JStoredDataProcessor<T extends JStoredData> {

    /**
     * Responsible for managing all {@link JStoredData} instances.
     */
    private final ArrayList<T> storedDataCache;

    private JStoredDataProcessor() {
        storedDataCache = new ArrayList<>();
    }

    /**
     * Pushes all uncommitted changes from the {@link JStoredData} instances to the storage medium.
     */
    public abstract void Commit();

    /**
     * Refreshes {@link JStoredData} instances with the latest data from the storage medium, discarding any uncommitted
     * data.
     */
    public abstract void Clear();
}
