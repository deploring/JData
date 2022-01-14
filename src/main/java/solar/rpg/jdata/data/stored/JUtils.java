package solar.rpg.jdata.data.stored;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

public final class JUtils {

    /**
     * Retrieves the generic object type parameter at runtime.
     *
     * @param clazz The class to retrieve the type parameter from.
     * @param <T>   The expected type of parameter. <em>Warning: This is unchecked and will not throw compile errors.</em>
     * @return The class of the type parameter.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> Class<T> getGenericType(@NotNull Class<?> clazz) {
        return (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Attempts to set the given value on the given {@link Field} in the given {@code Object}.
     *
     * @param instance The {@code Object} instance to update field for.
     * @param field    The field to update (on the {@code Object}).
     * @param value    The new value for the field.
     * @throws IllegalArgumentException Instance type and value type do not match.
     * @throws IllegalArgumentException Field is not private.
     * @throws IllegalArgumentException Unable to set field on object.
     */
    public static void writePrivateField(@NotNull Object instance, @NotNull Field field, @Nullable Object value) {
        if (!field.getClass().equals(instance.getClass()))
            throw new IllegalArgumentException("Instance type and value type do not match");
        if (!Modifier.isPrivate(field.getModifiers()))
            throw new IllegalArgumentException("Field is not private");
        try {
            field.setAccessible(true);
            field.set(instance, value);
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Unable to write to field");
        }
    }
}
