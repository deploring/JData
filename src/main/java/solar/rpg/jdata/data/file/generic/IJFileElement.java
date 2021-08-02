package solar.rpg.jdata.data.file.generic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.generic.JStoredDataField;

/**
 * Represents any generic data element which has a unique name, and value(s).
 * This class can used to represent a complex / multivariate data type in a {@link JStoredDataField}.
 * Fields can be accessed by name or by index.
 *
 * @param <T> The type of data stored in this element, if any.
 * @author jskinner
 * @since 1.0.0
 */
public interface IJFileElement<T> extends Iterable<JStoredDataField<?>> {

    /**
     * @param name Name of the {@link JStoredDataField} to retrieve.
     * @return The stored data field, if found.
     */
    @Nullable
    JStoredDataField<?> getFieldInfo(@NotNull String name);

    /**
     * @param index Index of the {@link JStoredDataField} to retrieve.
     * @return The stored data field, if found.
     */
    @Nullable
    JStoredDataField<?> getFieldInfo(int index);

    /**
     * @param fieldName Name of the stored data field to retrieve the associated value from.
     * @return The value under the given field name, if found.
     */
    @Nullable
    T getValue(@NotNull String fieldName);

    /**
     * @param index Index of the stored data field to retrieve the associated value from.
     * @return The value under the given field index, if found.
     */
    @Nullable
    T getValue(int index);
}
