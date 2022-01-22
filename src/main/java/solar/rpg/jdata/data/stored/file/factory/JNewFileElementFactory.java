package solar.rpg.jdata.data.stored.file.factory;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.stored.JUtils;
import solar.rpg.jdata.data.stored.file.JFileElement;
import solar.rpg.jdata.data.stored.file.JFileElementGroup;
import solar.rpg.jdata.data.stored.file.attribute.IJAttributable;
import solar.rpg.jdata.data.stored.file.attribute.JAttributedField;
import solar.rpg.jdata.data.stored.file.attribute.JAttributes;
import solar.rpg.jdata.data.stored.file.attribute.JHasAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * This factory class is used in the process of instantiating new file elements and stored data. Any nested element
 * fields will be created and instantiated automatically. All declared fields in all instantiated elements will also be
 * instantiated as null by default, except primitive numeric fields which are initialised as 0.
 *
 * @author jskinner
 * @since 1.0.0
 */
public class JNewFileElementFactory {

    /**
     * Creates a new {@link JAttributes} object associated with the given attributable object. Each attribute value is
     * initialised as null. If the attributable object is not annotated with {@link JHasAttributes}, an empty object is
     * returned instead.
     *
     * @param attributable The type of attributable object.
     * @return Attributes object (<em>empty if N/A</em>).
     * @throws IllegalArgumentException Provided class does not implement {@link IJAttributable}.
     * @throws IllegalArgumentException Number of attribute names and types do not match.
     */
    @NotNull
    public JAttributes createAttributes(@NotNull Class<?> attributable)
    {
        if (!IJAttributable.class.isAssignableFrom(attributable))
            throw new IllegalArgumentException("Provided class is not attributable");
        if (!attributable.isAnnotationPresent(JHasAttributes.class)) return new JAttributes();

        JHasAttributes attributes = attributable.getAnnotation(JHasAttributes.class);

        if (attributes.names().length != attributes.types().length)
            throw new IllegalArgumentException("Number of attribute names and types do not match");

        return new JAttributes(List.of(attributes.names()), List.of(attributes.types()));
    }

    /**
     * Initialises all data fields for a given file element or stored data. Any nested elements are also created and
     * have their data fields initialised as well.
     *
     * @param dataFieldsObject The object with data fields to initialise.
     * @throws IllegalArgumentException Class cannot contain itself as a data field.
     */
    public void initialiseDataFields(@NotNull Object dataFieldsObject)
    {
        for (Field field : dataFieldsObject.getClass().getDeclaredFields()) {
            Class<?> fieldType = field.getType();

            if (fieldType.isAssignableFrom(dataFieldsObject.getClass()))
                throw new IllegalArgumentException("Class cannot contain itself as a data field");

            // Regular fields are already instantiated as null.
            if (!IJAttributable.class.isAssignableFrom(fieldType)) continue;

            JAttributes fieldAttributes = createAttributes(fieldType);

            if (JAttributedField.class.isAssignableFrom(fieldType)) {
                JUtils.writePrivateField(
                    dataFieldsObject,
                    field,
                    JAttributedField.create(null, fieldAttributes)
                );
            } else
                if (JFileElementGroup.class.isAssignableFrom(fieldType)) {
                    JUtils.writePrivateField(
                        dataFieldsObject,
                        field,
                        createElement(JFileElementGroup.class, fieldAttributes)
                    );
                } else
                    if (JFileElement.class.isAssignableFrom(fieldType)) {
                        JUtils.writePrivateField(dataFieldsObject, field, createElement(fieldType, fieldAttributes));
                    }
        }
    }

    /**
     * Creates a new instance of a given {@link JFileElement} class.
     *
     * @param elementClass           The class of the element type to create.
     * @param <T>                    The type of element to create.
     * @param elementFieldAttributes Field level attributes that this element will inherit (otherwise null).
     * @return New instance of the given element type, with all fields initialised.
     * @throws IllegalArgumentException Class does not inherit from JFileElement.
     * @throws IllegalArgumentException Element has attribute definitions at both the class and field level.
     */
    @NotNull
    public <T> T createElement(@NotNull Class<T> elementClass, @NotNull JAttributes elementFieldAttributes)
    {
        if (!JFileElement.class.isAssignableFrom(elementClass))
            throw new IllegalArgumentException("Class does not inherit from JFileElement");

        try {
            JAttributes elementClassAttributes = createAttributes(elementClass);

            T element;
            boolean hasClassAttributes = !elementClassAttributes.isEmpty();
            boolean hasFieldAttributes = !elementFieldAttributes.isEmpty();

            if (hasClassAttributes && hasFieldAttributes)
                throw new IllegalArgumentException("Element has multiple attribute definitions");

            try {
                element = elementClass.getDeclaredConstructor(JAttributes.class).newInstance(hasFieldAttributes
                                                                                             ? elementFieldAttributes
                                                                                             : elementClassAttributes);
            } catch (NoSuchMethodException e) {
                element = elementClass.getDeclaredConstructor().newInstance();
            }

            initialiseDataFields(element);
            return element;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("Unable to create new child element", e);
        }
    }

    /**
     * Creates a new instance of the given {@code IJFileElement} class with no field attributes.
     *
     * @param elementClass An instance of the element class.
     * @param <T>          The type of element to create.
     * @return New instance of the given element type, with all fields initialised.
     * @throws IllegalArgumentException Element has attribute definitions at both the class and field level,
     */
    @NotNull
    public <T extends JFileElement> T createElement(@NotNull Class<T> elementClass)
    {
        JAttributes elementFieldAttributes = createAttributes(elementClass);
        return createElement(elementClass, elementFieldAttributes);
    }
}
