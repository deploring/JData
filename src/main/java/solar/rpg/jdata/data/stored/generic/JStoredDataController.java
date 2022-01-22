package solar.rpg.jdata.data.stored.generic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This is a class capable of processing stored data implemented by the given storage methods. The currently supported
 * storage methods are:
 * <ul>
 *     <li>MySQL (JDBC) (wip)</li>
 *     <li>JSON (wip)</li>
 *     <li>XML (wip)</li>
 * </ul>
 *
 * @param <T> The specific implementation of {@link JStoredData} that this controller manages.
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JStoredDataController<T extends JStoredData> {

    /**
     * Responsible for managing of the lifecycle all {@link JStoredData} instances.
     */
    protected final ArrayList<T> storedDataCache;

    public JStoredDataController()
    {
        storedDataCache = new ArrayList<>();
    }

    /**
     * Pushes all uncommitted changes from the {@link JStoredData} instances to the storage medium.
     */
    public abstract void commit();

    /**
     * Refreshes {@link JStoredData} instances with the latest data from the storage medium, discarding any uncommitted
     * data.
     */
    public abstract void clear();

    /**
     * Creates an instance of the given concrete {@link JStoredData} class. If there is a matching, existing stored
     * object in the cache, this is returned instead.
     *
     * @param filePath         The file path where the stored data exists or will be created.
     * @param storedDataClass  The type of stored data that we wish to retrieve.
     * @param storedDataFilter A condition to match against an existing <u>single</u> stored data object.
     * @param <D>              The concrete type of stored data we wish to retrieve.
     * @throws IllegalArgumentException Stored data class does not have a single empty constructor.
     */
    @NotNull
    public <D extends T> D getStoredData(
        @NotNull Path filePath,
        @NotNull Class<D> storedDataClass,
        @NotNull Predicate<D> storedDataFilter)
    {
        D result = findCachedStoredData(storedDataClass, storedDataFilter);
        if (result != null) return result;

        // TODO: Either create new file if it doesn't exist, or load from existing file.

        Constructor<?>[] storedDataConstructors = storedDataClass.getDeclaredConstructors();
        if (storedDataConstructors.length != 1 || storedDataConstructors[0].getParameterCount() != 0)
            throw new IllegalArgumentException("Stored data class does not have a single empty constructor");

        return result;
    }

    /**
     * Attempts to find an already-loaded {@link JStoredData} object in the cache. If no match is found, then {@code
     * null} is returned.
     *
     * @param storedDataClass  The concrete type of stored data that we wish to retrieve.
     * @param storedDataFilter A condition to match against an existing <u>single</u> stored data object.
     * @param <D>              The concrete type of stored data we wish to retrieve.
     * @return Cached stored data object, otherwise {@code null}.
     * @throws IllegalArgumentException Multiple matches were found.
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <D extends T> D findCachedStoredData(Class<D> storedDataClass, Predicate<D> storedDataFilter)
    {
        List<D> matches = storedDataCache.stream()
            .filter(genericStoredData -> storedDataClass.isAssignableFrom(genericStoredData.getClass())
            ).map(genericStoredData -> (D) genericStoredData)
            .filter(storedDataFilter)
            .collect(Collectors.toList());
        if (matches.size() > 1) throw new IllegalArgumentException("Multiple matches found");
        else
            if (matches.size() == 1) {
                D match = matches.get(0);
                if (!match.getStoredDataState().canCommit()) match.reload();
                return match;
            } else return null;
    }
}
