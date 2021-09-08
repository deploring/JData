package solar.rpg.jdata.data.file.xml;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.file.generic.IJFileElement;
import solar.rpg.jdata.data.file.generic.IJFileNode;
import solar.rpg.jdata.data.stored.generic.JDataField;
import solar.rpg.jdata.data.variants.JDataType;

import java.util.Map;

/**
 * Represents an {@link IJFileNode} that acts as element node in an XML document.
 * This XML element can store other types of {@link JXMLNode} objects as children.
 * Consequently, calling code is able to traverse the DOM structure depth-first.
 *
 * @author jskinner
 * @see IJFileNode
 * @since 1.0.0
 */
public class JXMLElement extends JXMLNode implements IJFileElement {

    /**
     * Stores all child XML nodes belonging to this XML element.
     */
    @NotNull
    private final JXMLNode[] children;

    /**
     * True, if this the root XML element.
     */
    private final boolean root;

    /**
     * @param fieldInfo  Field metadata information relating to this XML element.
     * @param attributes Map of this XML element's attributes.
     * @param children   Child XML nodes of this XML element.
     * @param root       True, if this is the root XML element.
     * @throws AssertionError Provided field type must be {@link JDataType#NULL}.
     */
    protected JXMLElement(
            @NotNull JDataField fieldInfo,
            @NotNull Map<String, String> attributes,
            @NotNull JXMLNode[] children,
            boolean root) {
        super(fieldInfo, attributes);
        assert fieldInfo.fieldType() == JDataType.NULL : String.format("XML element field type must be %s", JDataType.NULL);
        this.children = children;
        this.root = root;
    }

    /**
     * @return True, if this the root XML element.
     */
    @Override
    public boolean isRoot() {
        return root;
    }

    /**
     * @return All child XML nodes belonging to this XML element, as an array.
     */
    @NotNull
    @Override
    public JXMLNode[] getChildren() {
        return children;
    }
}
