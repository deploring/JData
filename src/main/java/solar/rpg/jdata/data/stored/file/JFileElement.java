package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.file.attribute.IJFileElementModel;
import solar.rpg.jdata.data.stored.file.attribute.JAttributedField;
import solar.rpg.jdata.data.stored.file.attribute.JAttributes;
import solar.rpg.jdata.data.stored.file.factory.JFileElementFactory;

/**
 * Represents an element node in a file structure. Elements can contain instances of other elements, or of data, usually
 * stored in the form of text nodes. Where an element's only purpose is to store a list of other elements with a common
 * structure, {@link JFileElementGroup} should be used instead. Use the {@link JAttributedField} class to declare fields
 * for the element, passing in the desired type to store.
 *
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JFileElement implements IJFileElementModel {

    @Nullable
    private JAttributes attributes;

    protected JFileElement(@NotNull JAttributes attributes)
    {
        this.attributes = attributes;
    }

    /**
     * Constructs a new {@code JFileElement} instance. The attributes must be instantiated separately by an {@link
     * JFileElementFactory}.
     */
    public JFileElement()
    {
    }

    @NotNull
    @Override
    public JAttributes getAttributes()
    {
        if (attributes == null) throw new IllegalStateException("Not initialised");
        return attributes;
    }
}