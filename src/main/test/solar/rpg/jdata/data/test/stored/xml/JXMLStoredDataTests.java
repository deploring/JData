package solar.rpg.jdata.data.test.stored.xml;

import org.junit.jupiter.api.Test;
import solar.rpg.jdata.data.test.stored.xml.classes.Identity;

import java.nio.file.Paths;
import java.util.UUID;

public class JXMLStoredDataTests {

    @Test
    public void FileStoredDataTests() {
        Identity identity = new Identity();
        identity.initialise(Paths.get("/Users/Joshua/Desktop/test.xml"));

        identity.getUuid();
        identity.setUuid(UUID.randomUUID());
    }
}
