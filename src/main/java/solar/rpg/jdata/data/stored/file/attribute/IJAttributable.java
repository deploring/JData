package solar.rpg.jdata.data.stored.file.attribute;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.stored.file.JFileStoredData;

/**
 * Marker interface which specifies that a particular type has (or could have) defined {@link JAttributes}. Classes or
 * fields that inherit this interface can be marked with {@link JHasAttributes}. This is particularly important in the
 * structure of {@link JFileStoredData} and its associated element objects.
 *
 * @author jskinner
 * @since 1.0.0
 */
public interface IJAttributable {

    /**
     * @return Attributes associated with this object.
     */
    @NotNull
    JAttributes getAttributes();
}
