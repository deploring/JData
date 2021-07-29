package solar.rpg.jdata.data.file.xml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import solar.rpg.jdata.data.file.IJFileElement;
import solar.rpg.jdata.data.file.JFileReadStatus;

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
    private String name;

    /**
     * Denotes the tag value of the {@link Element}.
     */
    @Nullable
    private String value;

    /**
     * Attributes are mapped by name to their respective values.
     */
    @NotNull
    private final Map<String, String> attributes;

    /**
     * The linked element has access to its immediate siblings, parents, and children.
     */
    @NotNull
    private final LinkedList<JXMLElement> children;

    /**
     * @see JFileReadStatus
     */
    private JFileReadStatus fileReadStatus;

    /**
     * @param name       Name of the tag that this XML element represents.
     * @param value      Value of this XML element, can be empty.
     * @param attributes Attributes for this element.
     * @param children   All child elements of this element.Z
     */
    JXMLElement(
            @NotNull String name,
            @Nullable String value,
            @NotNull Map<String, String> attributes,
            @NotNull LinkedList<JXMLElement> children) {
        this.name = name;
        this.value = value;
        this.children = children;
        this.attributes = attributes;
        fileReadStatus = JFileReadStatus.READ_WRITE;
    }

    /**
     * Returns the name of the tag that this XML element represents.
     * e.g. &lt;tagName>value&lt;/tagName>
     */
    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    /**
     * Returns the value of the tag that this XML element represents.
     * e.g. &lt;tagName>value&lt;/tagName>
     * Can be null, e.g. &lt;tagName/>
     * &lt;first>&lt;second>value&lt;/second>&lt;/first> (first does not have a value, but has a child element with a value)
     */
    @Nullable
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(@Nullable String value) {
        this.value = value;
    }

    @Override
    public void setReadStatus(@NotNull JFileReadStatus fileReadStatus) {
        this.fileReadStatus = fileReadStatus;
    }

    @NotNull
    @Override
    public JFileReadStatus getReadStatus() {
        return fileReadStatus;
    }

    /**
     * Returns the attributes of this element.
     */
    @NotNull
    public Map<String, String> getAttributes() {
        return attributes;
    }
}
