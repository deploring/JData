package solar.rpg.jdata.data.file.generic;

import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.variants.JDataType;

import java.io.Serializable;

/**
 * Use this sub-implementation of {@link IJFileElement} where no data is held inside the element.
 * This is the case for example, in XML because of empty tags (which may only have attributes).
 *
 * @author jskinner
 * @since 1.0.0
 */
public interface IJEmptyElement extends IJFileElement {

    /**
     * {@link IJEmptyElement} does not have any fields or associated values.
     */
    @Nullable
    @Override
    default Serializable getValue(JDataType dataType) {
        return null;
    }
}
