package solar.rpg.jdata.data.stored.file.attribute;

import org.jetbrains.annotations.NotNull;
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
        decoderFunctions.put(boolean.class, Boolean::parseBoolean);
        decoderFunctions.put(Boolean.class, Boolean::parseBoolean);
        decoderFunctions.put(char.class, s -> {
            if (s.length() != 1) throw new IllegalArgumentException("Cannot parse Character");
            return (Character) s.charAt(0);
        });
        decoderFunctions.put(Character.class, s -> {
            if (s.length() != 1) throw new IllegalArgumentException("Cannot parse Character");
            return (char) s.charAt(0);
        });
        decoderFunctions.put(int.class, Integer::parseInt);
        decoderFunctions.put(Integer.class, Integer::parseInt);
        decoderFunctions.put(float.class, Float::parseFloat);
        decoderFunctions.put(Float.class, Float::parseFloat);
        decoderFunctions.put(double.class, Double::parseDouble);
        decoderFunctions.put(Double.class, Double::parseDouble);
        decoderFunctions.put(short.class, Short::parseShort);
        decoderFunctions.put(Short.class, Short::parseShort);
        decoderFunctions.put(byte.class, Byte::parseByte);
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
        if (Enum.class.isAssignableFrom(targetClass)) return (T) decodeEnum(source, JUtils.resolveClass(Enum.class, targetClass));
        if (!decoderFunctions.containsKey(targetClass))
            throw new UnsupportedOperationException(String.format("Cannot decode %s", targetClass.getSimpleName()));
        return (T) decoderFunctions.get(targetClass).apply(source);
    }

    @NotNull
    public static <T> String toString(@NotNull T source)
    {
        return source.toString();
    }
}
