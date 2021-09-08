package solar.rpg.jdata.data.schema.file.xml;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.file.xml.JXMLNode;
import solar.rpg.jdata.data.schema.file.IJFileNodeSchema;
import solar.rpg.jdata.data.stored.generic.JDataField;
import solar.rpg.jdata.data.variants.JDataType;

/**
 * Represents a schema for any given {@link JXMLNode} in an XML document.
 *
 * @author jskinner
 * @see JXMLNode
 * @since 1.0.0
 */
public class JXMLNodeSchema implements IJFileNodeSchema {

    @NotNull
    private final JDataField fieldInfo;

    @NotNull
    private final JDataField[] attributesFieldInfo;

    protected JXMLNodeSchema(
            @NotNull String nodeName,
            @NotNull JDataType nodeStoredType,
            @NotNull JDataField[] attributesFieldInfo) {
        this.fieldInfo = new JDataField(nodeName, nodeStoredType);
        this.attributesFieldInfo = attributesFieldInfo;
    }

    /**
     * @return Field information relating to each attribute stored under this XML node.
     */
    @NotNull
    public JDataField[] getAttributesFieldInfo() {
        return attributesFieldInfo;
    }

    /**
     * @return Field information relating to this XML node.
     */
    @Override
    @NotNull
    public JDataField getFieldInfo() {
        return fieldInfo;
    }

    @Override
    public JXMLNode build() {
        return null;
    }
}
