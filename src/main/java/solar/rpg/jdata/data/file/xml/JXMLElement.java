package solar.rpg.jdata.data.file.xml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import solar.rpg.jdata.data.JTextVariant;
import solar.rpg.jdata.data.file.IJFileElement;
import solar.rpg.jdata.data.file.JFileReadStatus;
import solar.rpg.jdata.data.file.generic.IJFileElement;
import solar.rpg.jdata.data.file.generic.IJFileStructure;

import java.util.LinkedList;
import java.util.Map;

/**
 * Represents a single {@link Element} in a DOM document.
 * The attributes of the element are also mapped to strings.
 * Since elements have knowledge of their children, calling code
 * is able to traverse the DOM structure from the top-down.
 *
 * @author jskinner
 * @since 1.0.0
 */
public class JXMLElement implements IJFileElement {

    /**
     * Denotes the tag name of the {@link Element}.
     */
    @NotNull
    private String tagName;

    /**
     * Denotes the inner value of the {@link Element}.
     * This should only be used for text nodes.
     */
    @NotNull
    private JTextVariant value;

    /**
     * Attributes are mapped by name to their respective values.
     */
    @NotNull
    private final Map<String, String> attributes;

    /**
     * The XML element has access to its children.
     */
    @NotNull
    private final LinkedList<JXMLElement> children;

    /**
     * @param tagName    Name of the tag that this XML element represents.
     * @param value      Text node value of this XML element. Can be null if children are provided.
     * @param attributes Attributes for this XML element.
     * @param children   All child elements of this XML element.
     */
    JXMLElement(
            @NotNull String tagName,
            @Nullable String value,
            @NotNull Map<String, String> attributes,
            @NotNull LinkedList<JXMLElement> children) {
        this.tagName = tagName;
        this.value = new JTextVariant(value);
        this.children = children;
        this.attributes = attributes;
        fileReadStatus = JFileReadStatus.READ_WRITE;
    }

    /**
     * Returns the name of the tag that this XML element represents.
     * e.g. &lt;tagName>value&lt;/tagName>
     */
    @NotNull
    public String getTagName() {
        return tagName;
    }

    /**
     * Sets the name of the tag that this XML element represents.
     *
     * @param tagName Tag name of the XMl element.
     */
    public void setTagName(@NotNull String tagName) {
        this.tagName = tagName;
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
     * Sets the text node value of the XML element.
     *
     * @param value Text node value.
     */
    public void setValue(@Nullable String value) {
        this.value.setValue(value);
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
}
