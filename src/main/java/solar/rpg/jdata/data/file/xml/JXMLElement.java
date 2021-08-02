package solar.rpg.jdata.data.file.xml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import solar.rpg.jdata.data.variants.JDataType;
import solar.rpg.jdata.data.variants.JTextVariant;
import solar.rpg.jdata.data.file.generic.IJFileElement;
import solar.rpg.jdata.data.stored.generic.JDataField;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a single {@link Element} in a DOM document.
 * The attributes of the element are also mapped to strings.
 * The element can hold a {@link JTextVariant} value, or, more elements as children.
 * Since elements have knowledge of their children, calling code
 * is able to traverse the DOM structure from the top-down.
 *
 * @author jskinner
 * @since 1.0.0
 */
public class JXMLElement implements IJFileElement {

    /**
     * Holds field metadata information relating to this {@link JXMLElement}.
     */
    @NotNull
    private final JDataField fieldInfo;

    /**
     * Denotes the inner value of the {@link Element}.
     * This should only be used for text nodes.
     */
    @NotNull
    private final JTextVariant value;

    /**
     * Attributes are mapped by name to their respective values.
     */
    @NotNull
    private final Map<String, String> attributes;

    /**
     * The XML element has access to its children.
     * This should only be used for elements with child nodes.
     */
    @NotNull
    private final LinkedList<JXMLElement> children;

    /**
     * @param fieldInfo  Field metadata information relating to this XML element.
     * @param value      Text node value of this XML element. Can be null if children are provided.
     * @param attributes Attributes for this XML element.
     * @param children   All child elements of this XML element.
     */
    JXMLElement(
            @NotNull JDataField fieldInfo,
            @Nullable String value,
            @NotNull Map<String, String> attributes,
            @NotNull LinkedList<JXMLElement> children) {
        this.fieldInfo = fieldInfo;
        this.value = new JTextVariant(value, fieldInfo.getFieldType());
        this.children = children;
        this.attributes = attributes;
        //TODO: fileReadStatus = JFileReadStatus.READ_WRITE;
    }

    /**
     * Returns the name of the tag that this XML element represents.
     * e.g. &lt;tagName>value&lt;/tagName>
     */
    @NotNull
    public String getTagName() {
        return fieldInfo.getFieldName();
    }

    /**
     * Returns the text node value of the tag that this XML element represents.
     * e.g. &lt;tagName>value&lt;/tagName>
     * Can be null, e.g. &lt;tagName/>
     * &lt;first>&lt;second>value&lt;/second>&lt;/first> (first does not have a value, but has a child element with a value)
     *
     * @see JTextVariant
     */
    @NotNull
    public JTextVariant getValue() {
        return value;
    }

    /**
     * @return Read-only list of all child elements.
     */
    @NotNull
    @Override
    public List<IJFileElement> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Sets the text node value of the XML element.
     *
     * @param value    Text node value.
     * @param dataType Type of data stored under the text node.
     */
    public void setValue(@Nullable String value, @NotNull JDataType dataType) {
        this.value.setValue(value, dataType);
    }

    /**
     * Returns the attributes of this element.
     */
    @NotNull
    Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * @return True, if there are any attributes recorded against this XML element.
     */
    public boolean hasAttributes() {
        return getAttributes().size() > 0;
    }

    /**
     * @param name Name of attribute to find.
     * @return True, if an attribute is found under the given name.
     */
    public boolean hasAttribute(String name) {
        return getAttributes().containsKey(name);
    }

    /**
     * @param name Name of the attribute to find.
     * @return Attribute found
     */
    public String getAttribute(@NotNull String name) {
        assert hasAttribute(name) : "Expected attribute to exist";
        return getAttributes().get(name);
    }

    /**
     * @return Number of child elements, 0 if there is a value.
     */
    @Override
    public int getChildrenCount() {
        return children.size();
    }

    @NotNull
    @Override
    public JDataField getFieldInfo() {
        return fieldInfo;
    }

    @Nullable
    @Override
    public Serializable getValue(JDataType dataType) {
        return value.getValue(dataType);
    }

    @Override
    public boolean hasValue() {
        return !value.isNull();
    }
}
