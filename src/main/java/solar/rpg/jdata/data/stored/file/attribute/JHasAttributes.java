package solar.rpg.jdata.data.stored.file.attribute;

import solar.rpg.jdata.data.stored.file.JFileStoredData;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classes and fields marked with this annotation should inherit from the {@link IJFileElementModel} interface. This allows
 * {@link JAttributes} to be loaded for an object, or created fresh at runtime, notably for {@link JFileStoredData}
 * objects.
 *
 * @author jskinner
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface JHasAttributes {

    /**
     * @return The names of all attributes.
     */
    String[] names();

    /**
     * @return The types of all attributes.
     */
    Class<? extends Serializable>[] types();
}
