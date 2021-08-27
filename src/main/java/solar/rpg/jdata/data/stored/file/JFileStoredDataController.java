package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.stored.generic.JStoredDataController;

public class JFileStoredDataController extends JStoredDataController<JFileStoredData> {

    @Override
    public void commit() {

    }

    @Override
    public void clear() {

    }

    @NotNull
    @Override
    public JFileStoredData getStoredData(Class<JFileStoredData> storedDataClass, String[] keyValues) {
        return null;
    }
}
