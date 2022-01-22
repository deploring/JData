package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.stored.file.attribute.IJAttributable;
import solar.rpg.jdata.data.stored.file.attribute.JAttributedField;
import solar.rpg.jdata.data.stored.file.attribute.JAttributes;

/**
 * Represents an element node in a file structure. Elements can contain instances of other elements, or of data, usually
 * stored in the form of text nodes. Where an element's only purpose is to store a list of other elements with a common
 * structure, {@link JFileElementGroup} should be used instead. Use the {@link JAttributedField} class to declare fields
 * for the element, passing in the desired type to store.
 *
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JFileElement implements IJAttributable {

    @NotNull
    private final JAttributes attributes;

    /**
     * Constructs a new {@code JFileElement} instance.
     *
     * @param attributes The attributes associated with this element.
     */
    public JFileElement(@NotNull JAttributes attributes)
    {
        this.attributes = attributes;
    }

    /**
     * Constructs a new {@code JFileElement} instance with no attributes.
     */
    public JFileElement()
    {
        this.attributes = new JAttributes();
    }

    @NotNull
    @Override
    public JAttributes getAttributes()
    {
        return attributes;
    }
}