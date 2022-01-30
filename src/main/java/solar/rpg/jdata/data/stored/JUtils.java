package solar.rpg.jdata.data.stored;

import org.apache.commons.lang3.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class JUtils {

    public static <T> Collector<T, ?, T> singletonCollector()
    {
        return Collectors.collectingAndThen(
            Collectors.toList(),
            list -> {
                if (list.size() != 1)
                    throw new IllegalStateException("Expected single result");
                return list.get(0);
            }
        );
    }

    /**
     * Attempts to convert a wildcard class into a given target class.
     *
     * @param wildcardClass The wildcard class to convert.
     * @param targetClass   The expected class type.
     * @param <T>           The expected class type.
     * @return The given target class.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> Class<T> resolveClass(@NotNull Class<T> targetClass, @NotNull Class<?> wildcardClass)
    {
        if (!targetClass.isAssignableFrom(wildcardClass))
            throw new IllegalArgumentException(String.format(
                "Provided class does not inherit from %s",
                targetClass.getSimpleName()
            ));

        return (Class<T>) wildcardClass;
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
    public static void writePrivateField(@NotNull Object instance, @NotNull Field field, @Nullable Object value)
    {
        if (value != null) {
            Class<?> fieldType = field.getType();
            Class<?> valueType = value.getClass();
            if (!ClassUtils.isAssignable(fieldType, valueType))
                throw new IllegalArgumentException(String.format(
                    "Field type %s and value type %s do not match",
                    field.getType().getSimpleName(),
                    value.getClass().getSimpleName()
                ));
        }

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

    @SuppressWarnings("unchecked") // The caller is expected to know the field type.
    public static <T> T readPrivateField(@NotNull Object instance, @NotNull Field field)
    {
        try {
            if (!Modifier.isPrivate(field.getModifiers()))
                throw new IllegalArgumentException("Field is not private");
            field.setAccessible(true);
            T result = (T) field.get(instance);
            field.setAccessible(false);
            return result;
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(
                String.format(
                    "Cannot access field %s on class %s",
                    field.getName(),
                    instance.getClass().getSimpleName()),
                e
            );
        }
    }
}
