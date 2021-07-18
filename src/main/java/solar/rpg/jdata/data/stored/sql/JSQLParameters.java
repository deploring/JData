package solar.rpg.jdata.data.stored.sql;

import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Rather than using an array of {@link JSQLParameter}, this helper class can directly interact with
 * {@link PreparedStatement} to build WHERE clauses and pass in parameters.
 *
 * @author jskinner
 * @since 1.0.0
 */
public final class JSQLParameters implements Iterable<JSQLParameter> {

    @NotNull
    private final JSQLParameter[] parameters;

    /**
     * Builds a {@link JSQLParameters} object using supplied parameters.
     *
     * @param parameters Supplied parameters.
     */
    public JSQLParameters(@NotNull JSQLParameter[] parameters) {
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
     * Builds a WHERE clause for a {@link PreparedStatement} using the provided parameters.
     */
    public @NotNull
    String buildWhereClause() {
        StringBuilder whereClause = new StringBuilder();

        for (JSQLParameter parameter : parameters)
            whereClause.append(String.format(
                    " %1$s%2$s = :%2$s ",
                    whereClause.isEmpty() ? "" : " AND ",
                    parameter.getParameterName()));

        return whereClause.toString();
    }

    /**
     * Populates a {@link PreparedStatement} object using the provided parameters.
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
    public Iterator<JSQLParameter> iterator() {
        return Arrays.asList(parameters).iterator();
    }
}
