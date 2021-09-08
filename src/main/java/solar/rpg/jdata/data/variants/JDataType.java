package solar.rpg.jdata.data.variants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Denotes all supported generic field types in any given storage medium.
 *
 * @author jskinner
 * @since 1.0.0
 */
public enum JDataType {
    /**
     * Use this data type to represent fields that don't have a specific value, e.g. element nodes, empty tags.
     * The methods {@link #toString(Serializable)} and {@link #fromString(String)} cannot be used with this data type.
     */
    NULL(
            null,
            to -> {
                throw new UnsupportedOperationException("Cannot convert null data type to String");
            },
            from -> {
                throw new UnsupportedOperationException("Cannot convert null data type from String");
            }),
    STRING(
            String.class,
            Serializable::toString,
            String::valueOf),
    CHARACTER(
            Character.class,
            Serializable::toString,
            from -> {
                assert from.length() == 1 : String.format("Cannot convert \"%s\" into Character", from);
                return from.charAt(0);
            }),
    BOOLEAN(
            Boolean.class,
            Serializable::toString,
            Boolean::parseBoolean),
    BYTE(
            Byte.class,
            Serializable::toString,
            Byte::parseByte),
    SHORT(
            Short.class,
            Serializable::toString,
            Byte::parseByte),
    INTEGER(
            Integer.class,
            Serializable::toString,
            Integer::parseInt),
    FLOAT(
            Float.class,
            Serializable::toString,
            Float::parseFloat),
    DOUBLE(
            Double.class,
            Serializable::toString,
            Double::parseDouble),
    LONG(
            Long.class,
            Serializable::toString,
            Long::parseLong),
    UUID(
            java.util.UUID.class,
            Serializable::toString,
            java.util.UUID::fromString);

    @Nullable
    private final Class<? extends Serializable> classType;
    @NotNull
    private final Function<Serializable, String> toStringFunction;
    @NotNull
    private final Function<String, Serializable> fromStringFunction;

    /**
     * @param classType          The {@code class} type associated with this data type.
     * @param toStringFunction   A function which will convert a value into its {@code String} representation.
     * @param fromStringFunction A function which will return a value converted from its {@code String} representation.
     */
    JDataType(
            @Nullable Class<? extends Serializable> classType,
            @NotNull Function<Serializable, String> toStringFunction,
            @NotNull Function<String, Serializable> fromStringFunction) {
        this.classType = classType;
        this.toStringFunction = toStringFunction;
        this.fromStringFunction = fromStringFunction;
    }

    /**
     * @param sourceClass The {@code class} associated with the given data type.
     * @return The {@link JDataType} that is associated with the given {@code class}.
     * @throws NoSuchElementException {@link JDataType} associated with the given {@code class} could not be found.
     */
    public static JDataType fromClass(Class<? extends Serializable> sourceClass) {
        return Arrays.stream(values()).filter(dataType -> sourceClass.equals(dataType.getClassType())).findFirst().orElseThrow();
    }

    /**
     * @return The {@code class} of the given data type.
     * @throws AssertionError Data type does not have an associated {@code class}.
     */
    @NotNull
    public Class<? extends Serializable> getClassType() {
        assert classType != null : "Expected data type with an associated class";
        return classType;
    }

    /**
     * @param value The value to convert to a {@code String} representation.
     * @return The value, converted to a {@code String} representation.
     */
    @Nullable
    public String toString(@Nullable Serializable value) {
        return value == null ? null : toStringFunction.apply(value);
    }

    /**
     * @param value The value to convert from its {@code String} representation.
     * @return The value, converted from its {@code String} representation.
     */
    @Nullable
    public Serializable fromString(@Nullable String value) {
        return value == null ? null : fromStringFunction.apply(value);
    }
}
