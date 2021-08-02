package solar.rpg.jdata.data.stored.sql;

import solar.rpg.jdata.data.stored.generic.JDataField;
import solar.rpg.jdata.data.variants.JDataType;

/**
 * Represents a single column of data stored under a {@link JSQLStoredData} DB object.
 */
public final class JSQLDataField extends JDataField {

    /**
     * @param fieldName The column name that this field is representing.
     * @param fieldType Type of data stored under this field.
     * @param isPrimary True, if this field is a primary identifier of the stored data.
     */
    public JSQLDataField(String fieldName, JDataType fieldType, boolean isPrimary) {
        super(fieldName, fieldType, isPrimary);
    }
}
