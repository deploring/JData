package solar.rpg.jdata.data.file.xml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.file.generic.IJFileNode;
import solar.rpg.jdata.data.file.generic.IJFileTextNode;
import solar.rpg.jdata.data.stored.generic.JDataField;
import solar.rpg.jdata.data.variants.JDataType;
import solar.rpg.jdata.data.variants.JTextVariant;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an {@link IJFileNode} that acts as a text node in an XML document.
 * This type of XML node is distinctly for the storage of a specific type of value.
 * As a result, this type of node is always the child of an {@link JXMLElement} node.
 *
 * @author jskinner
 * @see IJFileNode
 * @see JXMLElement
 * @since 1.0.0
 */
public class JXMLTextNode extends JXMLNode implements IJFileTextNode {

    /**
     * Stores the value of this XML text node.
     */
    @NotNull
    private final JTextVariant value;

    /**
     * @param fieldInfo  Field metadata information relating to this XML element.
     * @param attributes Attributes for this XML element.
     * @param value      Text node value of this XML element. Can be null if children are provided.
     */
    public JXMLTextNode(
            @NotNull JDataField fieldInfo,
            @NotNull Map<String, String> attributes,
            @NotNull String value) {
        super(fieldInfo, attributes);
        this.value = new JTextVariant(value);
    }

    /**
     * @param dataType Type of data to convert the text node value to.
     * @return The value stored by this XML element, converted to the given data type.
     */
    @NotNull
    @Override
    public Serializable getValue(@NotNull JDataType dataType) {
        assert hasValue() : "Expected text node to store a value";
        return Objects.requireNonNull(value.getValueAs(dataType.getClassType()));
    }

    /**
     * Sets the text node value of the XML element.
     *
     * @param value New text node value.
     */
    @Override
    public void setValue(@Nullable Serializable value) {
        assert hasValue() : "Expected text node to store a value";
        this.value.setValue(value);
    }
}
