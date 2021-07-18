package solar.rpg.jdata.data.stored.sql;

import solar.rpg.jdata.data.stored.generic.JStoredDataProcessor;

import java.lang.reflect.InvocationTargetException;

/**
 * Implementation of a {@link JStoredDataProcessor} that uses an SQL database.
 * The connection is provided by {@link JJDBCDatabaseHelper}.
 *
 * @author jskinner
 * @since 1.0.0
 */
public final class JJDBCStoredDataProcessor extends JStoredDataProcessor<JSQLStoredData> {

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

    /**
     * Returns an instance of the given stored object class, under the given primary field values.
     *
     * @param storedDataClass Stored data type that we wish to retrieve.
     * @param keyValues       Primary field values.
     */
    @Override
    public JSQLStoredData getStoredData(Class<JSQLStoredData> storedDataClass, String[] keyValues) {
        JSQLStoredData result = findCachedStoredData(storedDataClass, keyValues);

        if (result != null) return result;

        assert storedDataClass.getDeclaredConstructors().length == 1 : "Expected a single constructor";
        assert storedDataClass.getDeclaredConstructors()[0].getParameterCount() == 0 : "Expected constructor to have no parameters";

        try {
            result = (JSQLStoredData) storedDataClass.getDeclaredConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        result.load(keyValues);

        return result;
    }
}