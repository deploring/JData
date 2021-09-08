package solar.rpg.jdata.data.stored.sql;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.generic.IJStoredData;
import solar.rpg.jdata.data.stored.generic.JDataField;
import solar.rpg.jdata.data.stored.generic.JDataParameter;
import solar.rpg.jdata.data.variants.JTextVariant;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a record stored in a database table, retrieved using SQL.
 * This data object can be used to retrieve the latest changes from the
 * database as well as pushing in-memory changes back to the database.
 *
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JSQLStoredData implements IJStoredData {

    /**
     * Retains an instance of a {@link JJDBCDatabaseHelper} to retrieve data and push uncommitted changes.
     */
    private final JJDBCDatabaseHelper database;

    /**
     * Table columns- defines the structure of this DB record.
     */
    private final JDataField[] dataFields;

    /**
     * @param database   Database connection.
     * @param dataFields Database column info, provided from the child class.
     */
    protected JSQLStoredData(JJDBCDatabaseHelper database, JDataField[] dataFields) {
        this.database = database;
        this.dataFields = dataFields;
    }

    /**
     * @param fieldName Name that uniquely identifies this field.
     * @return {@link JDataField} instance with the given field name.
     */
    @Override
    @Nullable
    public JDataField getField(@NotNull String fieldName) {
        return Arrays.stream(dataFields).filter(field ->
                field.fieldName().equals(fieldName)).findFirst().orElse(null);
    }

    /**
     * @param fieldIndex Index of the field.
     * @return {@link JDataField} instance at the given index.
     */
    @Override
    @NotNull
    public JDataField getField(int fieldIndex) {
        return dataFields[fieldIndex];
    }

    /**
     * @param valueIndex Index of the value to retrieve.
     * @return The value at the given index, as a {@link JTextVariant}.
     */
    @NotNull
    public abstract JTextVariant getValue(int valueIndex);

    /**
     * Refreshes the stored data object's field values using the latest ones in the database.
     * This will overwrite any uncommitted data, and requires the object to be initialised.
     */
    @Override
    public void refresh() {
        reload(new JSQLParameters(getPrimaryFieldValuesAsParams()));
    }

    @Override
    public void commit() {
        database.commitStoredData(this);
    }

    /**
     * Loads all field values from the database under the 
     *
     * @param keyValues
     */
    public void load(JDataParameter[] keyValues) {
        reload(new JSQLParameters(keyValues));
    }

    /**
     * Loads the latest database values into this {@link JSQLStoredData} object using the provided key search params.
     * This will overwrite any uncommitted data, unless the object is loading for the first time.
     *
     * @param keyValues Primary key search parameters (to load the correct, unique record values).
     */
    private void reload(JSQLParameters keyValues) {
    }

    /**
     * Use this to reload an existing record from the database.
     *
     * @return An array containing all primary {@link JDataField} values represented as {@link JDataParameter} objects.
     */
    @NotNull
    public JDataParameter[] getPrimaryFieldValuesAsParams() {
        return Arrays.stream(getPrimaryFields()).map(this::getFieldValueAsParameter).toArray(JDataParameter[]::new);
    }

    /**
     * @param keyValues The primary key values for each parameter.
     * @return An array of {@link JDataParameter} objects containing each primary field, along with provided
     * search values. This can be used for record searches in the stored data object.
     */
    @NotNull
    public JDataParameter[] getPrimaryFieldSearchParams(@NotNull String[] keyValues) {
        AtomicInteger primaryFieldIndex = new AtomicInteger();
        return Arrays.stream(getPrimaryFields()).map(field -> new JDataParameter(field.fieldName(), keyValues[primaryFieldIndex.getAndIncrement()])).toArray(JDataParameter[]::new);
    }

    /**
     * @return The table name where this particular stored data is located.
     */
    @NotNull
    public String getTableName() {
        return getClass().getSimpleName().substring(1);
    }
}
