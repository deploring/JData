package solar.rpg.jdata.data.file.generic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.generic.JStoredDataField;

import java.util.Collections;
import java.util.Iterator;

/**
 * Use this sub-implementation of {@link IJFileElement} where no data is held inside the element.
 * This is the case for example, in XML because of empty tags (which may only have attributes).
 *
 * @author jskinner
 * @since 1.0.0
 */
public interface IJEmptyElement extends IJFileElement<Object> {

    /**
     * {@link IJEmptyElement} does not have any fields or associated values.
     */
    @Nullable
    @Override
    default JStoredDataField<?> getFieldInfo(@NotNull String name) {
        return null;
    }

    /**
     * {@link IJEmptyElement} does not have any fields or associated values.
     */
    @Nullable
    @Override
    default JStoredDataField<?> getFieldInfo(int index) {
        return null;
    }

    /**
     * {@link IJEmptyElement} does not have any fields or associated values.
     */
    @Nullable
    @Override
    default Object getValue(@NotNull String fieldName) {
        return null;
    }

    /**
     * {@link IJEmptyElement} does not have any fields or associated values.
     */
    @Nullable
    @Override
    default Object getValue(int index) {
        return null;
    }

    /**
     * {@link IJEmptyElement} does not have any fields or associated values.
     */
    @NotNull
    @Override
    default Iterator<JStoredDataField<?>> iterator() {
        return Collections.emptyIterator();
    }
}
