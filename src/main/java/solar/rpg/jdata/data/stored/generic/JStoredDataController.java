package solar.rpg.jdata.data.stored.generic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * This is a class capable of processing stored data implemented by the given storage methods.
 * The currently supported storage methods are:
 * <ul>
 *     <li>MySQL (JDBC) (wip)</li>
 *     <li>JSON (wip)</li>
 *     <li>XML (wip)</li>
 * </ul>
 *
 * @param <T> The specific implementation of {@link IJStoredData} that this controller manages.
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JStoredDataController<T extends IJStoredData> {

    /**
     * Responsible for managing of the lifecycle all {@link IJStoredData} instances.
     */
    protected final ArrayList<T> storedDataCache;

    public JStoredDataController() {
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

    /**
     * Returns an instance of the given {@link IJStoredData} class, under the given primary field values.
     * If there is a matching, existing stored object in cache, this is returned instead.
     *
     * @param storedDataClass Stored data type that we wish to retrieve.
     * @param keyValues       Primary field values.
     */
    @NotNull
    public T getStoredData(Class<T> storedDataClass, String[] keyValues) {
        T result = findCachedStoredData(storedDataClass, keyValues);

        if (result != null) return result;

        assert storedDataClass.getDeclaredConstructors().length == 1 : "Expected a single constructor";
        assert storedDataClass.getDeclaredConstructors()[0].getParameterCount() == 0 : "Expected constructor to have no parameters";

        try {
            result = storedDataClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassCastException | NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }

        result.load(result.getPrimaryFieldSearchParams(keyValues));

        return result;
    }

    /**
     * Attempts to find an already-existing {@link IJStoredData} object in the cache.
     * If no match is found (by comparing primary field values), then {@code null} is returned.
     *
     * @param storedDataClass Stored data type that we wish to retrieve.
     * @param keyValues       Primary field values (must uniquely identify the field).
     */
    @Nullable
    protected T findCachedStoredData(Class<T> storedDataClass, String[] keyValues) {
        for (T storedData : storedDataCache) {
            JDataParameter[] params = storedData.getPrimaryFieldSearchParams(keyValues);
            assert params.length == keyValues.length : "Mismatch in primary stored data amounts";

            boolean match = true;

            for (JDataParameter param : params) {
                JDataField storedDataField = storedData.getField(param.getParameterName());
                assert storedData.getField(param.getParameterName()) != null : String.format("Could not find stored data field %s", param.getParameterName());


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
