package solar.rpg.jdata.data.stored.generic;

/**
 * Classes implementing this interface should store a parameter consisting of
 * a unique identifying label and a value.
 * This can be used for queries and searches to find {@link IJStoredData objects}.
 *
 * @author jskinner
 * @since 1.0.0
 */
public interface IJDataParameter {

    /**
     * Returns a unique identifying label for this parameter.
     */
    String getParameterName();

    /**
     * Returns the in-memory value associated with this parameter.
     */
    Object getParameterValue();
}
