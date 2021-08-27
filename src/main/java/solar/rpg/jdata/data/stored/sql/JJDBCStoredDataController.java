package solar.rpg.jdata.data.stored.sql;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.stored.generic.JStoredDataController;

import java.lang.reflect.InvocationTargetException;

/**
 * Implementation of a {@link JStoredDataController} that uses an SQL database.
 * The connection is provided by {@link JJDBCDatabaseHelper}.
 *
 * @author jskinner
 * @since 1.0.0
 */
public final class JJDBCStoredDataController extends JStoredDataController<JSQLStoredData> {

    /**
     * Commits all in-memory changes on all cached {@link JSQLStoredData} objects to the database.
     */
    @Override
    public void commit() {
        for (JSQLStoredData storedData : storedDataCache)
            if (storedData.canCommit()) storedData.commit();
    }

    /**
     * Discards all in-memory changes on all cached {@link JSQLStoredData} objects by refreshing them.
     */
    @Override
    public void clear() {
        for (JSQLStoredData storedData : storedDataCache)
            storedData.refresh();
    }
}