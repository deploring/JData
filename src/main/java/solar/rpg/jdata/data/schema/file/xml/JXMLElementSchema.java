package solar.rpg.jdata.data.schema.file.xml;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.file.xml.JXMLElement;
import solar.rpg.jdata.data.schema.file.IJFileElementSchema;
import solar.rpg.jdata.data.stored.generic.JDataField;
import solar.rpg.jdata.data.variants.JDataType;

public class JXMLElementSchema extends JXMLNodeSchema implements IJFileElementSchema {

    private final boolean structural;
    private final JXMLNodeSchema[] childNodeSchemas;

    public JXMLElementSchema(
            @NotNull String elementName,
            @NotNull JDataField[] attributesFieldInfo,
            @NotNull JXMLElementSchema childElementSchema) {
        super(elementName, JDataType.NULL, attributesFieldInfo);
        this.structural = true;
        this.childNodeSchemas = new JXMLNodeSchema[]{childElementSchema};
    }

    public JXMLElementSchema(
            @NotNull String elementName,
            @NotNull JDataField[] attributesFieldInfo,
            @NotNull JXMLNodeSchema... childNodeSchemas) {
        super(elementName, JDataType.NULL, attributesFieldInfo);
        this.structural = false;
        this.childNodeSchemas = childNodeSchemas;
    }

    @NotNull
    @Override
    public JXMLNodeSchema[] getChildNodeSchemas() {
        return childNodeSchemas;
    }

    @Override
    public boolean isStructural() {
        return structural;
    }

    @Override
    @NotNull
    public JXMLElement build() {
        return null;
    }
}
