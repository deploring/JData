package solar.rpg.jdata.data.variants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * Represents a mixed data type which can be treated as multiple values as applicable,
 * but is stored as a {@code String}.
 * This can be converted easily to other primitive types under the above assumptions.
 *
 * @author jskinner
 * @since 1.0.0
 */
public final class JTextVariant implements Serializable {

    /**
     * The serialised {@code String} representation of the variant value.
     */
    @Nullable
    private String rawValue;

    /**
     * The current {@link JDataType} of the variant;
     */
    @NotNull
    private JDataType fieldType;

    public JTextVariant() {
        rawValue = null;
        fieldType = JDataType.NULL;
    }

    public JTextVariant(@Nullable Serializable value, @NotNull JDataType fieldType) {
        this();
        setValue(value, fieldType);
    }

    /**
     * @return True, if the serialized value is null.
     */
    public boolean isNull() {
        return rawValue == null;
    }

    /**
     * Sets the variant value.
     *
     * @param value     The value to serialize.
     * @param fieldType The data type of the value to serialize.
     */
    public void setValue(Serializable value, JDataType fieldType) {
        this.fieldType = fieldType;
        rawValue = fieldType.serialize(value);
    }

    /**
     * @param dataType The data type to deserialize the value to.
     * @return The deserialized variant value.
     */
    public Serializable getValue(JDataType dataType) {
        return dataType.deserialize(rawValue);
    }

    /**
     * @return Raw serialized variant value.
     */
    public String getRawValue() {
        return rawValue;
    }
}
