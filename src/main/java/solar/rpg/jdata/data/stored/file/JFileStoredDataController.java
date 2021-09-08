package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.file.generic.IJFileElement;
import solar.rpg.jdata.data.stored.generic.JStoredDataController;

public class JFileStoredDataController<T extends JFileStoredData<E>, E extends IJFileElement> extends JStoredDataController<T> {

    @Override
    public void commit() {

    }

    @Override
    public void clear() {

    }

    @NotNull
    @Override
    public T getStoredData(Class<T> fileStoredDataClass, String[] keyValues) {
        return null;
    }
}
