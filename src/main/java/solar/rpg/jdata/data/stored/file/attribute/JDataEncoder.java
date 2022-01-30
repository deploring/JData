package solar.rpg.jdata.data.stored.file.attribute;

import org.apache.commons.lang3.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.JUtils;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

/**
 * This utility class allows easy conversion between {@code String}s and other primitive data types.
 *
 * @author jskinner
 * @since 1.0.0
 */
public final class JDataEncoder {

    private static final HashMap<Class<?>, Function<String, ?>> decoderFunctions = new HashMap<>();

    private static final Function<String, Character> charDecoderFunction = s -> {
        if (s.length() != 1) throw new IllegalArgumentException("Cannot parse Character");
        return s.charAt(0);
    };

    static {
        decoderFunctions.put(String.class, s -> s);
        decoderFunctions.put(Boolean.class, Boolean::parseBoolean);
        decoderFunctions.put(Character.class, s -> {
            if (s.length() != 1) throw new IllegalArgumentException("Cannot parse Character");
            return s.charAt(0);
        });
        decoderFunctions.put(Integer.class, Integer::parseInt);
        decoderFunctions.put(Float.class, Float::parseFloat);
        decoderFunctions.put(Double.class, Double::parseDouble);
        decoderFunctions.put(Short.class, Short::parseShort);
        decoderFunctions.put(Byte.class, Byte::parseByte);
        decoderFunctions.put(UUID.class, UUID::fromString);
    }

    private static <E extends Enum<E>> E decodeEnum(@NotNull String source, @NotNull Class<E> enumClass)
    {
        return E.valueOf(enumClass, source);
    }

    /**
     * Converts the given {@code String} into the target type.
     *
     * @param source      The {@code String} to convert.
     * @param targetClass The class of the target type.
     * @param <T>         The target type.
     * @return The String value as the given target type.
     * @throws UnsupportedOperationException Cannot decode given type.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> T fromString(@NotNull String source, @NotNull Class<T> targetClass)
    {
        if (Enum.class.isAssignableFrom(targetClass)) return (T) decodeEnum(
            source,
            JUtils.resolveClass(Enum.class, targetClass));

        return (T) decoderFunctions.get(
            decoderFunctions.keySet().stream()
                .filter(clazz -> ClassUtils.isAssignable(targetClass, clazz))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException(String.format(
                    "Cannot decode %s",
                    targetClass.getSimpleName()
                )))
        ).apply(source);
    }

    @Nullable
    public static <T> String toString(@Nullable T source)
    {
        if (source == null) return null;
        if (!decoderFunctions.containsKey(source.getClass()) && !(source instanceof Enum))
            throw new UnsupportedOperationException(String.format(
                "Cannot encode %s",
                source.getClass().getSimpleName()
            ));
        return source.toString();
    }
}
