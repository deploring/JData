package solar.rpg.jdata.data.stored.file.attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.file.JFileStoredData;

/**
 * Holds field information for a {@link JFileStoredData} object where the field has one or more attributes. Usages of
 * this class should always be annotated with {@link JHasAttributes} as they are mandatory. If you don't wish to declare
 * an attributed field, simply declare the field rather than using this class.
 *
 * @param <T> Type of data stored under this field.
 * @author jskinner
 * @since 1.0.0
 */
public final class JAttributedField<T> implements IJFileElementModel {

    @NotNull
    public JAttributes attributes;
    @Nullable
    private T fieldValue;

    /**
     * Constructs a new {@code JAttributedDataField} instance.
     *
     * @param fieldValue The value stored under this field.
     * @param attributes The attributes associated with this field.
     */
    private JAttributedField(@Nullable T fieldValue, @NotNull JAttributes attributes)
    {
        this.fieldValue = fieldValue;
        this.attributes = attributes;
    }

    /**
     * @param fieldValue The value stored under this field.
     * @param attributes The attributed associated with this field.
     * @param <V>        The type of data stored under this field.
     * @return New {@code JAttributedDataField<V>} object instance.
     */
    public static <V> JAttributedField<V> create(@Nullable V fieldValue, @NotNull JAttributes attributes)
    {
        return new JAttributedField<>(fieldValue, attributes);
    }

    /**
     * @return The value stored under this field.
     */
    @Nullable
    public T get()
    {
        return fieldValue;
    }

    /**
     * Sets the value stored under this field.
     *
     * @param fieldValue The value stored under this field.
     */
    public void set(@Nullable T fieldValue)
    {
        this.fieldValue = fieldValue;
    }

    @NotNull
    @Override
    public JAttributes getAttributes()
    {
        return attributes;
    }
}
