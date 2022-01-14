package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.JUtils;
import solar.rpg.jdata.data.stored.file.attribute.IJAttributable;
import solar.rpg.jdata.data.stored.file.attribute.JAttributedField;
import solar.rpg.jdata.data.stored.file.attribute.JAttributes;
import solar.rpg.jdata.data.stored.file.attribute.JHasAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * This factory object is used for creating new {@link JFileElement} objects. Any nested element objects will also be
 * created and instantiated automatically. All declared fields in all instantiated elements will be instantiated as null
 * by default, except primitive numeric fields which are initialised as 0.
 *
 * @author jskinner
 * @since 1.0.0
 */
public final class JFileElementFactory {

    /**
     * Creates a new {@link JAttributes} object associated with the given attributable object. Each attribute is
     * initialised as null. If the attributable object is not annotated with {@link JHasAttributes}, an empty object is
     * returned instead.
     *
     * @param attributable The type of attributable object.
     * @return Attributes object (<em>empty if N/A</em>).
     * @throws IllegalArgumentException Provided class does not implement {@link IJAttributable}.
     */
    @NotNull
    public JAttributes newAttributes(@NotNull Class<?> attributable) {
        if (IJAttributable.class.isAssignableFrom(attributable))
            throw new IllegalArgumentException("Provided class is not attributable");
        if (!attributable.isAnnotationPresent(JHasAttributes.class)) return new JAttributes();

        JHasAttributes attributes = attributable.getAnnotation(JHasAttributes.class);
        return new JAttributes(List.of(attributes.names()), List.of(attributes.types()));
    }

    /**
     * Creates a new instance of the given {@code IJFileElement} class.
     *
     * @param elementClass           An instance of the element class.
     * @param <T>                    The type of element to create.
     * @param elementFieldAttributes Attributes that this element will inherit from a field annotation (otherwise null).
     * @return New instance of the given element type, with all fields initialised.
     * @throws IllegalArgumentException Element has attribute definitions at both the class and field level,
     */
    @NotNull
    public <T extends JFileElement> T newElement(
            @NotNull Class<T> elementClass,
            @Nullable JAttributes elementFieldAttributes) {
        try {
            JAttributes elementClassAttributes = newAttributes(elementClass);
            if (!elementClassAttributes.isEmpty() && elementFieldAttributes != null)
                throw new IllegalArgumentException("Element has multiple attribute definitions");

            T element = elementClass.getDeclaredConstructor(JAttributes.class).newInstance(elementClassAttributes);

            for (Field field : elementClass.getDeclaredFields()) {
                Class<?> fieldType = field.getType();
                if (!IJAttributable.class.isAssignableFrom(fieldType)) continue;

                JAttributes fieldAttributes = newAttributes(fieldType);

                if (JAttributedField.class.isAssignableFrom(fieldType)) {
                    JUtils.writePrivateField(element, field, JAttributedField.create(fieldAttributes));
                } else if (JFileElementGroup.class.isAssignableFrom(fieldType)) {
                    JUtils.writePrivateField(element, field, newElement(JFileElementGroup.class, fieldAttributes));
                } else if (JFileElement.class.isAssignableFrom(fieldType)) {
                    //TODO: Cast wildcard type to T extends JFileElement.
                }
            }
            return element;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("Unable to create new child element", e);
        }
    }

    @NotNull
    public <T extends JFileElement> T newElement(@NotNull Class<T> elementClass) {
        return newElement(elementClass, null);
    }
}
