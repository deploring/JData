package solar.rpg.jdata.data.test.stored.xml;

import org.junit.jupiter.api.Test;
import solar.rpg.jdata.data.stored.file.saver.JXMLStoredDataSaver;
import solar.rpg.jdata.data.test.stored.xml.classes.Identity;
import solar.rpg.jdata.data.test.stored.xml.classes.Phone;

import java.io.File;
import java.nio.file.Paths;

public class JXMLStoredDataTests {

    @Test
    public void FileStoredDataTests()
    {
        /*Identity identity = new Identity();
        identity.initialise(Paths.get("/Users/Joshua/Desktop/test.xml"));

        identity.getUuid();
        identity.setUuid(UUID.randomUUID());*/

        Identity identity2 = new Identity();
        identity2.initialise(Paths.get("/Users/Joshua/Desktop/identity.xml"));

        identity2.getPhones().get(1).getAttributes().set("phoneType", Phone.PhoneType.OTHER);

        JXMLStoredDataSaver xmlSaver = new JXMLStoredDataSaver(
            new File("/Users/Joshua/Desktop/identity_out.xml"),
            identity2
        );
        xmlSaver.save();

        Phone newPhone = identity2.getPhones().newChild();
    }
}
