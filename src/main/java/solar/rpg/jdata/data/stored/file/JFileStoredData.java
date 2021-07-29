package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.generic.IJDataParameter;
import solar.rpg.jdata.data.stored.generic.IJStoredData;
import solar.rpg.jdata.data.stored.generic.JStoredDataField;

import java.util.Iterator;

public class JFileStoredData implements IJStoredData<JFileStoredDataField> {

    @Nullable
    @Override
    public JStoredDataField<?> getFieldByName(@NotNull String fieldName) {
        return null;
    }

    @Override
    public void commit() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public Iterator<JFileStoredDataField> iterator() {
        return null;
    }

    @NotNull
    @Override
    public IJDataParameter[] getPrimaryFieldSearchParams() {
        return new IJDataParameter[0];
    }

    @NotNull
    @Override
    public IJDataParameter[] getPrimaryFieldSearchParams(@NotNull String[] keyValues) {
        return new IJDataParameter[0];
    }
}
