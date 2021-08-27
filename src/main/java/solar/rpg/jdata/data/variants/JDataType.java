package solar.rpg.jdata.data.variants;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Denotes all supported generic field types in any given storage medium.
 *
 * @author jskinner
 * @since 1.0.0
 */
public enum JDataType {
    NULL(null, true),
    STRING(String.class, true),
    CHARACTER(Character.class, false),
    BOOLEAN(Boolean.class, false),
    BYTE(Byte.class, false),
    SHORT(Short.class, false),
    INTEGER(Integer.class, false),
    FLOAT(Float.class, false),
    DOUBLE(Double.class, false),
    LONG(Long.class, false);

    private final Class<? extends Serializable> classType;
    private final boolean nullable;

    JDataType(Class<? extends Serializable> classType, boolean nullable) {
        this.classType = classType;
        this.nullable = nullable;
    }

    /**
     * @return The {@code class} of the given data type.
     */
    public Class<? extends Serializable> getClassType() {
        return classType;
    }

    /**
     * @param value The value to convert.
     * @return The value, converted to a serialized String (where applicable).
     */
    public String serialize(@Nullable Serializable value) {
        return value == null ? null : value.toString();
    }

    /**
     * @param value The value to convert.
     * @return The value, converted from a serialized String (where applicable).
     */
    public Serializable deserialize(@Nullable String value) {
        if (!nullable) Objects.requireNonNull(value);
        else assert value != null;
        switch (this) {
            case STRING -> {
                return value;
            }
            case CHARACTER -> {
                assert value.length() == 1 : String.format("Cannot convert \"%s\" into Character", value);
                return value.charAt(0);
            }
            case BOOLEAN -> {
                return Boolean.parseBoolean(value);
            }
            case BYTE -> {
                return Byte.parseByte(value);
            }
            case SHORT -> {
                return Short.parseShort(value);
            }
            case INTEGER -> {
                return Integer.parseInt(value);
            }
            case FLOAT -> {
                return Float.parseFloat(value);
            }
            case DOUBLE -> {
                return Double.parseDouble(value);
            }
            case LONG -> {
                return Long.parseLong(value);
            }
            case NULL -> {
                return null;
            }
            default -> throw new UnsupportedOperationException("Unsupported field type");
        }
    }
}
