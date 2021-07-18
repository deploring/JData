package solar.rpg.jdata.data.stored.sql;

import solar.rpg.jdata.data.stored.generic.JStoredDataField;

/**
 * Represents a single column of data stored under a {@link JSQLStoredData} object.
 *
 * @param <T> The type of data that is stored under this field.
 */
public final class JSQLStoredDataField<T> extends JStoredDataField<T> {

    /**
     * @param fieldName The column name that this field is representing.
     * @param isPrimary True, if this field is a primary identifier of the stored data.
     */
    public JSQLStoredDataField(String fieldName, boolean isPrimary) {
        super(fieldName, isPrimary);
    }

    /**
     * Returns an instance of {@link JSQLParameter} with the same field name and stored value.
     */
    public JSQLParameter asSQLParameter() {
        return new JSQLParameter(getFieldName(), getStoredValue());
    }
}
