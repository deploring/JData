package solar.rpg.jdata.data.stored.generic;

/**
 * Represents a single parameter to build with and pass to data queries.
 * This is used for manipulating and querying {@link IJStoredData} objects.
 *
 * @author jskinner
 * @since 1.0.0
 */
public final class JDataParameter {

    private final String paramName;
    private final Object paramValue;

    /**
     * @param paramName  Name of the SQL parameter, <em>e.g. (:foo, :bar)</em>
     * @param fieldValue Value of the SQL parameter.
     */
    public JDataParameter(String paramName, Object fieldValue) {
        this.paramName = paramName;
        this.paramValue = fieldValue;
    }

    public String getParameterName() {
        return paramName;
    }

    public Object getParameterValue() {
        return paramValue;
    }
}
