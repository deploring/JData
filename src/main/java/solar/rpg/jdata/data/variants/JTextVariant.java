package solar.rpg.jdata.data.variants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * Stores a {@code String} value which can be returned as any applicable data type.
 *
 * @author jskinner
 * @see JDataType
 * @since 1.0.0
 */
public final class JTextVariant implements Serializable {

    /**
     * The stored {@code String} value. This can be converted into different data types where applicable.
     */
    @Nullable
    private String rawValue;

    public JTextVariant() {
        rawValue = null;
    }

    public JTextVariant(@Nullable Serializable value) {
        this();
        setValue(value);
    }

    /**
     * @return True, if the stored value is null.
     */
    public boolean isNull() {
        return rawValue == null;
    }

    /**
     * @param value The value to convert to a {@code String} and store.
     */
    public void setValue(@Nullable Serializable value) {
        rawValue = value == null ? null : JDataType.fromClass(value.getClass()).toString(value);
    }

    /**
     * @param asType Class of the given type to convert the raw value to.
     * @param <T>    Type to convert the raw value to and return as.
     * @return The stored value converted to the given type.
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends Serializable> T getValueAs(@NotNull Class<T> asType) {
        return isNull() ? null : (T) JDataType.fromClass(asType).fromString(rawValue);
    }

    /**
     * @return The raw string value, without any conversion.
     */
    @Nullable
    public String getRawValue() {
        return rawValue;
    }
}
