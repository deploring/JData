package solar.rpg.jdata.data.stored.generic;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * This is a class capable of processing relational data implemented by the given methods.
 * The currently supported storage methods are:
 * <ul>
 *     <li>MySQL (JDBC) (wip)</li>
 *     <li>JSON (wip)</li>
 *     <li>XML (wip)</li>
 * </ul>
 *
 * @param <T> The specific implementation of {@link IJStoredData} that this processor manages.
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JStoredDataProcessor<T extends IJStoredData> {

    /**
     * Responsible for managing of the lifecycle all {@link IJStoredData} instances.
     */
    protected final ArrayList<T> storedDataCache;

    public JStoredDataProcessor() {
        storedDataCache = new ArrayList<>();
    }

    /**
     * Pushes all uncommitted changes from the {@link IJStoredData} instances to the storage medium.
     */
    public abstract void commit();

    /**
     * Refreshes {@link IJStoredData} instances with the latest data from the storage medium, discarding any uncommitted
     * data.
     */
    public abstract void clear();

    /**
     * Returns an instance of the given {@link IJStoredData} object, under the given primary field values.
     *
     * @param storedDataClass Stored data type that we wish to retrieve.
     * @param keyValues       Primary field values.
     */
    public abstract T getStoredData(Class<T> storedDataClass, String[] keyValues);

    /**
     * Attempts to find an already-existing {@link IJStoredData} object in the cache.
     * If no match is found (by comparing primary field values), then {@code null} is returned.
     *
     * @param storedDataClass Stored data type that we wish to retrieve.
     * @param keyValues       Primary field values.
     */
    @Nullable
    protected T findCachedStoredData(Class<T> storedDataClass, String[] keyValues) {
        for (T storedData : storedDataCache) {
            if (!storedData.getClass().equals(storedDataClass)) continue;

            IJDataParameter[] params = storedData.getPrimaryFieldSearchParams(keyValues);
            assert params.length == keyValues.length : "Mismatch in primary stored data amounts";

            boolean match = true;

            for (IJDataParameter param : params) {
                JDataField<?> storedDataField = storedData.getFieldByName(param.getParameterName());
                assert storedDataField != null : String.format("Could not find stored data field %s", param.getParameterName());
                assert storedDataField.getStoredValue() != null : "Stored data field value was not set";

                if (!storedDataField.getStoredValue().equals(param.getParameterValue())) {
                    match = false;
                    break;
                }
            }

            if (match) {
                // Grab the latest data while we're here if there's nothing to commit.
                if (!storedData.canCommit()) storedData.refresh();
                return storedData;
            }
        }
        return null;
    }
}
