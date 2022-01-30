package solar.rpg.jdata.data.stored.file.factory;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.stored.JUtils;
import solar.rpg.jdata.data.stored.file.JFileStoredData;
import solar.rpg.jdata.data.stored.file.attribute.IJFileElementModel;
import solar.rpg.jdata.data.stored.file.attribute.JAttributes;

/**
 * Classes implementing this interface are capable of initialising {@link JFileStoredData} objects and their respective
 * data fields from a given data source. Child elements should also be created and initialised using the data source.
 *
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JFileElementFactory {

    /**
     * Initialises the data fields of a given {@link JFileStoredData} object. This should also create and initialise any
     * child elements.
     *
     * @param storedData The stored data to initialise.
     */
    public abstract void initialiseStoredData(@NotNull JFileStoredData storedData);

    /**
     * Assigns attributes to a given attributable object.
     *
     * @param target          The attributable object to assign the attributes to.
     * @param classAttributes Attributes retrieved from class-level annotations.
     * @param fieldAttributes Attributes retrieved from field-level annotations.
     * @throws IllegalArgumentException Attributes provided from the field and class level.
     */
    protected <E extends IJFileElementModel> void initialiseAttributes(
        @NotNull E target,
        @NotNull JAttributes classAttributes,
        @NotNull JAttributes fieldAttributes)
    {
        Class<? extends IJFileElementModel> targetClass = target.getClass();

        if (!classAttributes.isEmpty() && !fieldAttributes.isEmpty())
            throw new IllegalArgumentException(String.format(
                "Element %s has multiple attribute definitions",
                target.getClass().getSimpleName()
            ));

        try {
            JUtils.writePrivateField(
                target,
                targetClass.getDeclaredField("attributes"),
                classAttributes.isEmpty() ? fieldAttributes : classAttributes
            );
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(
                String.format(
                    "Cannot set attributes on %s",
                    targetClass.getSimpleName()
                ),
                e
            );
        }
    }
}
