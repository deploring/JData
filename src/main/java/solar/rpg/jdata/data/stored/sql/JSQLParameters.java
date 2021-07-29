package solar.rpg.jdata.data.stored.sql;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.stored.generic.JDataParameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Rather than using an array of {@link JDataParameter}, this helper class can directly interact
 * with {@link PreparedStatement} queries to build WHERE clauses and pass in parameters.
 *
 * @author jskinner
 * @since 1.0.0
 */
public final class JSQLParameters implements Iterable<JDataParameter> {

    @NotNull
    private final JDataParameter[] parameters;

    /**
     * Builds a {@link JSQLParameters} object using supplied parameters.
     *
     * @param parameters Supplied set of {@link JDataParameter}.
     */
    public JSQLParameters(@NotNull JDataParameter[] parameters) {
        this.parameters = parameters;
    }

    /**
     * Returns true if there are 0 parameters.
     */
    public boolean isEmpty() {
        return parameters.length == 0;
    }

    /**
     * Returns number of provided parameters.
     */
    public int getSize() {
        return parameters.length;
    }

    /**
     * Builds a parameterized WHERE clause for a {@link PreparedStatement} SQL query using the provided parameters.
     */
    @NotNull
    public String buildWhereClause() {
        StringBuilder whereClause = new StringBuilder();

        for (JDataParameter parameter : parameters)
            whereClause.append(String.format(
                    " %1$s%2$s = :%2$s ",
                    whereClause.isEmpty() ? "" : " AND ",
                    parameter.getParameterName()));

        return whereClause.toString();
    }

    /**
     * Populates a {@link PreparedStatement} object's parameters using the provided parameters.
     *
     * @param statement The query object to populate.
     * @throws SQLException Can fail for any number of reasons.
     */
    public void populateQueryParameters(@NotNull PreparedStatement statement) throws SQLException {
        for (int keyIndex = 0; keyIndex < parameters.length; keyIndex++)
            statement.setObject(keyIndex, parameters[keyIndex].getParameterValue());
    }

    @Override
    @NotNull
    public Iterator<JDataParameter> iterator() {
        return Arrays.asList(parameters).iterator();
    }
}
