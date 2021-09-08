package solar.rpg.jdata.data.stored.file.xml;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.file.xml.JXMLElement;
import solar.rpg.jdata.data.schema.file.xml.JXMLElementSchema;
import solar.rpg.jdata.data.stored.file.JFileStoredData;

public class JXMLStoredData extends JFileStoredData<JXMLElement> {

    private JXMLElementSchema schema;

    /**
     * @param rootElement   Root element of the XML stored data structure.
     * @param directoryPath Path of the directory where the XML file will be created/stored.
     * @param fileName      Name of the file where the XML stored data is located.
     */
    protected JXMLStoredData(JXMLElement rootElement, String directoryPath, String fileName) {
        super(rootElement, directoryPath, fileName);
    }

    @Override
    @NotNull
    public JXMLElementSchema getSchema() {
        return schema;
    }
}
