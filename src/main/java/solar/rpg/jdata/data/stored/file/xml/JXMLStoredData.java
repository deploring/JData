package solar.rpg.jdata.data.stored.file.xml;

import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.file.xml.JXMLElement;
import solar.rpg.jdata.data.stored.file.JFileStoredData;

import java.io.Serializable;

public class JXMLStoredData extends JFileStoredData {

    /**
     * @param rootElement   Root element of the file stored data structure.
     * @param directoryPath Path of the directory where the file will be created/stored.
     * @param fileName      Name of the file where the stored data is located.
     */
    protected JXMLStoredData(JXMLElement rootElement, String directoryPath, String fileName) {
        super(rootElement, directoryPath, fileName);
    }
}
