package solar.rpg.jdata.data.test.stored.xml.classes;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.stored.file.JFileElementGroup;
import solar.rpg.jdata.data.stored.file.JFileStoredData;
import solar.rpg.jdata.data.stored.file.attribute.JHasAttributes;

@JHasAttributes(names = "version", types = Integer.class)
public class Identity extends JFileStoredData {

    @NotNull
    private String name;

    @NotNull
    private JFileElementGroup<Phone> phones;

    public Identity(@NotNull String directoryPath, @NotNull String fileName) {
        super(directoryPath, fileName);
    }
}
