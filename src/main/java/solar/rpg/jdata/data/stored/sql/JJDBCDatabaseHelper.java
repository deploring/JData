package solar.rpg.jdata.data.stored.sql;

import org.jetbrains.annotations.NotNull;

import java.sql.*;

public final class JJDBCDatabaseHelper {

    /**
     * Database connection & authentication information.
     **/
    private final String USERNAME, PASSWORD, URL, DATABASE;

    /**
     * Instance of the SQL database {@link Connection}.
     */
    private Connection connection;

    /**
     * @param username SQL server username.
     * @param password SQL server password.
     * @param hostname SQL server hostname.
     * @param port     SQL server port.
     * @param database Name of schema/database on SQL server.
     */
    public JJDBCDatabaseHelper(String username, String password, String hostname, String port, String database) {
        super();
        this.USERNAME = username;
        this.PASSWORD = password;
        this.URL = String.format("jdbc:mysql://%s:%s/", hostname, port) +
                   "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B11";
        this.DATABASE = database;

        open();

        assert connection != null : "Unable to connect to database.";
    }

    /**
     * Check if the required JDBC driver classes are present.
     *
     * @throws RuntimeException If the JDBC driver is not installed.
     */
    private void initialize() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC driver not found.");
        }
    }

    /**
     * Checks if the MySQL connection is still alive.
     * Re-opens the connection if it is no longer alive.
     */
    private void checkConnection() {
        assert connection != null : "Expected connection to exist";

        try {
            if (!connection.isValid(0))
                open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Attempt to open a connection to the SQL database.
     */
    private void open() {
        initialize();
        try {
            connection = DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);
            connection.setSchema(DATABASE);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Closes the MySQL connection. Does not attempt to re-open.
     * Unused.
     */
    public void close() {
        if (connection == null) return;

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection = null;
        }
    }

    /**
     * Creates and returns an SQL injection-safe {@link PreparedStatement}.
     *
     * @param query The SQL query.
     */
    public PreparedStatement prepare(String query) {
        checkConnection();
        try {
            return connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Runs an update SQL query using the provided SQL script and parameters.
     *
     * @param SQL       Query to run, which may contain parameters.
     * @param paramsSet Parameter values
     */
    private void runUpdateSQL(@NotNull String SQL, @NotNull JSQLParameters... paramsSet) {
        PreparedStatement statement = prepare(SQL);

        try {
            int paramIndex = 0;
            for (var params : paramsSet)
                for (var param : params) {
                    statement.setObject(paramIndex, param.getParameterValue());
                    paramIndex++;
                }

            assert statement.getParameterMetaData().getParameterCount() ==
                   paramIndex : "Parameter count mismatch between query and provided params";

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a query that contains all the columns of a given table record under the given primary key values.
     *
     * @param tableName        Stored data table name.
     * @param primaryKeyParams Primary key parameters of the stored data record to retrieve.
     */
    @NotNull
    PreparedStatement getStoredDataQuery(@NotNull String tableName, @NotNull JSQLParameters primaryKeyParams) {
        assert !primaryKeyParams.isEmpty() : "At least one primary key parameter must be provided";

        try {
            PreparedStatement select = prepare(String.format(
                " SELECT * FROM %1$s WHERE %2$s",
                tableName,
                primaryKeyParams.buildWhereClause()));

            primaryKeyParams.populateQueryParameters(select);

            return select;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Commits any in-memory changes from a {@link JSQLStoredData} object back to the database.
     *
     * @param storedData Stored object to commit.
     */
    public void commitStoredData(@NotNull JSQLStoredData storedData) {
        /*assert storedData.canCommit() : "The given stored data object is not ready for committing";

        StringBuilder updateQuery = new StringBuilder(String.format(
                " UPDATE %s SET ",
                storedData.getStoredDataTableName()));

        StringBuilder setClause = new StringBuilder();

        JSQLParameters updateParams = storedData.getUpdateQueryParams();

        for (JDataParameter param : updateParams)
            setClause.append(String.format(
                    " %1$s%2$s = :%2$s ",
                    setClause.isEmpty() ? "" : ", ",
                    param.getParameterName()));

        JSQLParameters primaryKeyParams = new JSQLParameters(storedData.getPrimaryFieldSearchParams());

        updateQuery.append(setClause);
        updateQuery.append(String.format(" WHERE %s ", primaryKeyParams.buildWhereClause()));

        runUpdateSQL(updateQuery.toString(), updateParams, primaryKeyParams);*/
    }
}
