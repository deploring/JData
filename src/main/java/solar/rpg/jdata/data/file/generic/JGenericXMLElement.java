package solar.rpg.jdata.data.file.generic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.file.xml.JXMLElement;
import solar.rpg.jdata.data.stored.generic.JDataField;
import solar.rpg.jdata.data.variants.JDataType;

import java.util.LinkedList;
import java.util.Map;

/**
 * This class can be used instead of extending {@link JXMLElement} where
 * the underlying XML element is only storing a single piece of data, or
 * where the structure does not matter.
 *
 * @author jskinner
 * @since 1.0.0
 */
public class JGenericXMLElement extends JXMLElement {

    /**
     * @param value      Text node value of this XML element. Can be null if children are provided.
     * @param attributes Attributes for this XML element.
     * @param children   All child elements of this XML element.
     */
    protected JGenericXMLElement(@Nullable String value, @NotNull Map<String, String> attributes, @NotNull LinkedList<JXMLElement> children, boolean root) {
        super(new JDataField("element", JDataType.STRING, false), value, attributes, children, root);
    }
}
