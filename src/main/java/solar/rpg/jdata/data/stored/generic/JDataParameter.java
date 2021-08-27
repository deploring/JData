package solar.rpg.jdata.data.stored.generic;

import java.io.Serializable;

/**
 * Represents a single parameter to build with and pass to data queries.
 * This is used for manipulating and querying {@link IJStoredData} objects.
 *
 * @author jskinner
 * @since 1.0.0
 */
public final class JDataParameter {

    private final String paramName;
    private final Serializable paramValue;

    /**
     * @param paramName  Name of the SQL parameter, <em>e.g. (:foo, :bar)</em>
     * @param fieldValue Value of the SQL parameter.
     */
    public JDataParameter(String paramName, Serializable fieldValue) {
        this.paramName = paramName;
        this.paramValue = fieldValue;
    }

    public String getParameterName() {
        return paramName;
    }

    public Serializable getParameterValue() {
        return paramValue;
    }
}
