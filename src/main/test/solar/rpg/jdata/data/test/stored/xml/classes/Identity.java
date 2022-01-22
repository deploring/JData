package solar.rpg.jdata.data.test.stored.xml.classes;

import solar.rpg.jdata.data.stored.file.JFileElement;
import solar.rpg.jdata.data.stored.file.JFileElementGroup;
import solar.rpg.jdata.data.stored.file.JFileStoredData;
import solar.rpg.jdata.data.stored.file.attribute.JHasAttributes;

import java.util.UUID;

@JHasAttributes(names = "version",
                types = Integer.class)
public class Identity extends JFileStoredData {

    private UUID uuid;
    private Name name;
    private int age;
    private JFileElementGroup<Phone> phones;

    public UUID getUuid()
    {
        return uuid;
    }

    public void setUuid(UUID uuid)
    {
        this.uuid = uuid;
    }

    public Name getName()
    {
        return name;
    }

    public void setName(Name name)
    {
        this.name = name;
    }

    public JFileElementGroup<Phone> getPhones()
    {
        return phones;
    }

    public void setPhones(JFileElementGroup<Phone> phones)
    {
        this.phones = phones;
    }

    public static class Name extends JFileElement {

        private String givenNames;
        private String familyName;
    }
}
