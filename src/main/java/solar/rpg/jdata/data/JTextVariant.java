package solar.rpg.jdata.data;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a mixed data type which can be treated as multiple
 * values as applicable, under the assumption that the caller knows
 * what data types can be appropriately casted to the value.
 * The raw value is stored as a {@link String}, and can be converted
 * easily to other primitive types under the above assumptions.
 *
 * @author jskinner
 * @since 1.0.0
 */
public final class JTextVariant {

    @Nullable
    private String rawValue;

    public JTextVariant() {
        rawValue = null;
    }

    public JTextVariant(@Nullable String value) {
        rawValue = value;
    }

    public JTextVariant(int value) {
        this(String.valueOf(value));
    }

    public JTextVariant(double value) {
        this(String.valueOf(value));
    }

    public JTextVariant(float value) {
        this(String.valueOf(value));
    }

    public JTextVariant(short value) {
        this(String.valueOf(value));
    }

    public JTextVariant(byte value) {
        this(String.valueOf(value));
    }

    public JTextVariant(long value) {
        this(String.valueOf(value));
    }

    public JTextVariant(char value) {
        this(String.valueOf(value));
    }

    public JTextVariant(boolean value) {
        this(String.valueOf(value));
    }

    public JTextVariant(Object value) {
        this(String.valueOf(value));
    }

    public boolean isNull() {
        return rawValue == null;
    }

    public void setValue(String value) {
        rawValue = value;
    }

    public void setValue(Object value) {
        rawValue = String.valueOf(value);
    }

    public String asString() {
        Objects.requireNonNull(rawValue);
        return rawValue;
    }

    public int asInt() {
        Objects.requireNonNull(rawValue);
        return Integer.parseInt(rawValue);
    }

    public double asDouble() {
        Objects.requireNonNull(rawValue);
        return Double.parseDouble(rawValue);
    }

    public float asFloat() {
        Objects.requireNonNull(rawValue);
        return Float.parseFloat(rawValue);
    }

    public float asShort() {
        Objects.requireNonNull(rawValue);
        return Short.parseShort(rawValue);
    }

    public float asByte() {
        Objects.requireNonNull(rawValue);
        return Byte.parseByte(rawValue);
    }

    public float asLong() {
        Objects.requireNonNull(rawValue);
        return Long.parseLong(rawValue);
    }

    public char asChar() {
        Objects.requireNonNull(rawValue);
        assert rawValue.length() == 1 : "Expected String length to be 1 to cast to Character";
        return rawValue.charAt(0);
    }

    public boolean asBoolean() {
        Objects.requireNonNull(rawValue);
        return Boolean.parseBoolean(rawValue);
    }

    public Object asObject() {
        return rawValue;
    }
}
