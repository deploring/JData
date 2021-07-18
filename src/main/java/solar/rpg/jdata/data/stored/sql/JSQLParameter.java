package solar.rpg.jdata.data.stored.sql;

import solar.rpg.jdata.data.stored.generic.IJDataParameter;

import java.sql.PreparedStatement;

/**
 * Represents a single parameter to build with and pass to a {@link PreparedStatement} query.
 * This is used for manipulating {@link JSQLStoredData} objects.
 *
 * @author jskinner
 * @since 1.0.0
 */
public final class JSQLParameter implements IJDataParameter {

    /**
     * Name of the SQL parameter, <em>e.g. (:foo, :bar)</em>
     */
    private final String paramName;

    /**
     * Value of the SQL parameter which is safe against SQL injection.
     */
    private final Object paramValue;

    /**
     * @param paramName Name of the SQL parameter, <em>e.g. (:foo, :bar)</em>
     * @param fieldValue Value of the SQL parameter.
     */
    public JSQLParameter(String paramName, Object fieldValue) {
        this.paramName = paramName;
        this.paramValue = fieldValue;
    }

    @Override
    public String getParameterName() {
        return paramName;
    }

    @Override
    public Object getParameterValue() {
        return paramValue;
    }
}
