package solar.rpg.jdata.data.stored.sql;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.generic.IJStoredData;
import solar.rpg.jdata.data.stored.generic.JDataField;
import solar.rpg.jdata.data.stored.generic.JDataParameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
                field.getFieldName().equals(fieldName)).findFirst().orElse(null);
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
     * Refreshes the stored data object's field values using the latest ones in the database.
     * This will overwrite any uncommitted data, and requires the object to be initialised.
     */
    @Override
    public void refresh() {
        reload(new JSQLParameters(getPrimaryFieldSearchParams()));
    }

    @Override
    public void commit() {
        database.commitStoredData(this);
    }

    @NotNull
    @Override
    public JDataParameter[] getPrimaryFieldSearchParams() {
        List<JDataParameter> list = new LinkedList<>();
        return Arrays.stream(dataFields).filter(JDataField::isPrimary)
                .map(JDataField::asSQLParameter).toArray(JDataParameter[]::new);
    }

    /**
     * Returns a {@link JSQLParameters} object containing each primary field, along with provided search values.
     *
     * @param keyValues The primary key values for each parameter.
     */
    @NotNull
    @Override
    public JDataParameter[] getPrimaryFieldSearchParams(@NotNull String[] keyValues) {
        List<JDataParameter> list = new LinkedList<>();
        Arrays.stream(dataFields).forEachOrdered(storedDataField -> list.add(new JDataParameter(storedDataField.getFieldName(), keyValues[list.size()])));

        assert keyValues.length == list.size() : "Expected primary key amount to match key value amount";

        return list.toArray(new JDataParameter[0]);
    }

    /**
     * Attempts to load the {@link JSQLStoredData} object using the provided primary key values.
     *
     * @param keyValues Primary key values.
     */
    public void load(String[] keyValues) {
        reload(new JSQLParameters(getPrimaryFieldSearchParams(keyValues)));
    }

    /**
     * Loads the latest database values into this {@link JSQLStoredData} object using the provided key search params.
     * This will overwrite any uncommitted data, unless the object is loading for the first time.
     */
    private void reload(@NotNull JSQLParameters keyParams) {
        try {
            PreparedStatement storedDataQuery = database.getStoredDataQuery(getStoredDataTableName(), keyParams);

            ResultSet storedDataSet = storedDataQuery.executeQuery();
            storedDataSet.first();
            assert storedDataSet.isLast() : "Expected single row in result set";

            for (int colIndex = 0; colIndex < storedDataSet.getMetaData().getColumnCount(); colIndex++) {
                String colName = storedDataSet.getMetaData().getColumnName(colIndex);
                JSQLDataField fieldToUpdate = getFieldByName(colName);
                assert fieldToUpdate != null : String.format("Field with name '%s' not found", colName);
                fieldToUpdate.setStoredValue(storedDataSet.getObject(colIndex));
            }

            storedDataSet.close();
            storedDataQuery.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public Iterator<JSQLDataField> iterator() {
        return Arrays.asList(dataFields).iterator();
    }

    /**
     * Returns the table name where this particular stored data is located.
     */
    @NotNull
    public String getStoredDataTableName() {
        return getClass().getSimpleName().substring(1);
    }

    /**
     * Returns a {@link JSQLParameters} object containing each field requiring a parameter for the UPDATE statement.
     */
    @NotNull
    JSQLParameters getUpdateQueryParams() {
        List<JSQLParameter> list = new LinkedList<>();
        Arrays.stream(dataFields).filter(JDataField::isChanged).forEachOrdered(dataField -> {
            assert !dataField.isPrimary() : "Expected non-primary data field";
            list.add(new JSQLParameter(dataField.getFieldName(), dataField.getStoredValue()));
        });
        return new JSQLParameters(list.toArray(new JSQLParameter[0]));
    }
}
