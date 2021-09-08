package solar.rpg.jdata.data.file.xml;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.file.generic.IJFileNode;
import solar.rpg.jdata.data.stored.generic.JDataField;

import java.util.Map;

/**
 * Represents an {@link IJFileNode} that is capable of storing a tag name and attributes in an XML document.
 * The XML node's attributes are stored in a {@code Map<String, String>}.
 * This class and its extensions should be used where there is a defined, repeated, unchanging structure.
 *
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JXMLNode implements IJFileNode {

    /**
     * Holds field metadata information relating to this XML node.
     */
    @NotNull
    private final JDataField fieldInfo;

    /**
     * Attributes are mapped by name to their respective values.
     */
    @NotNull
    private final Map<String, String> attributes;

    /**
     * @param fieldInfo  Field metadata information relating to this XML node.
     * @param attributes Map of this XML node's attributes.
     */
    public JXMLNode(
            @NotNull JDataField fieldInfo,
            @NotNull Map<String, String> attributes) {
        this.fieldInfo = fieldInfo;
        this.attributes = attributes;
    }

    /**
     * @return Map of this XML node's attributes.
     * @throws AssertionError XML node has no attributes.
     * @see #hasAttributes()
     */
    @NotNull
    private Map<String, String> getAttributes() {
        assert hasAttributes() : "Expected XML node to have at least 1 attribute";
        return attributes;
    }

    /**
     * @return True, if there are any attributes recorded against this XML element.
     */
    public boolean hasAttributes() {
        return getAttributes().size() > 0;
    }

    /**
     * @param name Name of the attribute to retrieve the value from.
     * @return Value of the attribute found under the given name.
     * @throws AssertionError Attribute not found under the given name.
     * @see #hasAttribute(String)
     */
    @NotNull
    public String getAttribute(@NotNull String name) {
        assert hasAttribute(name) : "Expected attribute to exist";
        return getAttributes().get(name);
    }

    /**
     * @param name Name of attribute to find.
     * @return True, if an attribute is found under the given name.
     */
    public boolean hasAttribute(@NotNull String name) {
        return getAttributes().containsKey(name);
    }

    /**
     * @return Field metadata information relating to this XML node.
     */
    @NotNull
    @Override
    public JDataField getFieldInfo() {
        return fieldInfo;
    }
}
