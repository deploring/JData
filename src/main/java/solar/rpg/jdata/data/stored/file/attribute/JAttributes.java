package solar.rpg.jdata.data.stored.file.attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Stores attribute information for a concrete implementation of the {@link IJAttributable} interface.
 * The following information is stored for each attribute:
 * <ul>
 *     <li>Name, for identification.</li>
 *     <li>Type of data stored (see {@link #get(String)}).</li>
 *     <li>Value, stored as the defined type.</li>
 * </ul>
 *
 * @author jskinner
 * @since 1.0.0
 */
public class JAttributes {

    /**
     * Only certain primitive types can be stored as attribute values.
     */
    private static final List<Class<?>> ALLOWED_TYPES = List.of(new Class<?>[]{
        String.class,
        Character.class,
        Number.class
    });

    @NotNull
    private final List<String> attributeNames;
    @NotNull
    private final List<Serializable> attributeValues;
    @NotNull
    private final List<Class<? extends Serializable>> attributeTypes;

    /**
     * Constructs a new {@code JAttributes} instance.
     *
     * @param attributeNames  Names of all attributes.
     * @param attributeValues Values of all attributes.
     * @param attributeTypes  Types of all attribute values.
     * @throws IllegalArgumentException Amount of attribute names and values must match.
     * @throws IllegalArgumentException One of the given value types is not allowed.
     * @see #ALLOWED_TYPES
     */
    public JAttributes(
        @NotNull List<String> attributeNames,
        @NotNull List<Serializable> attributeValues,
        @NotNull List<Class<? extends Serializable>> attributeTypes) {
        if (attributeNames.size() != attributeValues.size() || attributeNames.size() != attributeTypes.size())
            throw new IllegalArgumentException("Amount of attribute names, values, and types must match");
        this.attributeNames = attributeNames;
        this.attributeValues = attributeValues;
        this.attributeTypes = attributeTypes;

        attributeTypes.forEach(attributeType -> ALLOWED_TYPES.stream()
            .filter(allowedType -> allowedType.isAssignableFrom(attributeType))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Illegal typr provided")));

        IntStream.range(0, attributeValues.size()).forEach(i -> validateValueType(attributeValues.get(i),
                                                                                  attributeTypes.get(i)));
    }

    /**
     * Constructs a new {@code JAttributes} instance with all attributes instantiated as null.
     *
     * @param attributeNames Names of all attributes.
     * @param attributeTypes Types of all attribute values.
     * @throws IllegalArgumentException Amount of attribute names and values must match.
     * @throws IllegalArgumentException One of the given value types is not allowed.
     * @see #ALLOWED_TYPES
     */
    public JAttributes(
        @NotNull List<String> attributeNames,
        @NotNull List<Class<? extends Serializable>> attributeTypes) {
        this(attributeNames, Arrays.asList(new Serializable[attributeNames.size()]), attributeTypes);
    }

    /**
     * Constructs a new {@code JAttributes} instances with no attributes.
     * This should be used where attributes are not defined (but an instance is required).
     */
    public JAttributes() {
        this(List.of(), List.of(), List.of());
    }

    /**
     * @return True, if there are no attributes stored under this object.
     */
    public boolean isEmpty() {
        return attributeNames.size() == 0;
    }

    /**
     * Sets the value for a given attribute.
     *
     * @param attributeName The name of the attribute.
     * @param value         The new value of the attribute.
     * @param <V>           The type of value being stored must inherit from {@link Serializable}.
     * @throws IllegalArgumentException Attribute does not exist.
     * @throws IllegalArgumentException Value type does not match actual attribute type.
     */
    public <V extends Serializable> void set(@NotNull String attributeName, @Nullable V value) {
        int attributeIndex = attributeNames.indexOf(attributeName);
        if (attributeIndex == -1)
            throw new IllegalArgumentException(String.format("Attribute %s does not exist", attributeName));

        validateValueType(value, attributeTypes.get(attributeIndex));
        attributeValues.set(attributeIndex, value);
    }

    /**
     * @param attributeName The name of the attribute.
     * @param <V>           The expected type for the attribute value <strong>(important!)</strong>.
     * @return The value of the attribute, returned as the given type.
     */
    @SuppressWarnings("unchecked")
    public <V extends Serializable> V get(@NotNull String attributeName) {
        return (V) attributeValues.get(attributeNames.indexOf(attributeName));
    }

    /**
     * Checks that an attribute's value is of the given type, otherwise an {@code IllegalArgumentException} is thrown.
     *
     * @param value         The value to check.
     * @param expectedClass The type that the value is expected to have.
     * @param <V>           The actual type of the value.
     * @throws IllegalArgumentException Value type does not match expected type.
     */
    private <V> void validateValueType(@Nullable V value, @NotNull Class<?> expectedClass) {
        if (value == null) return;

        Class<?> actualClass = value.getClass();
        if (!expectedClass.isAssignableFrom(actualClass))
            throw new IllegalArgumentException(String.format("Value type %s does not match expected type %s",
                                                             actualClass.getSimpleName(),
                                                             expectedClass));
    }
}
