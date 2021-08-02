package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.generic.IJStoredData;
import solar.rpg.jdata.data.stored.generic.JDataField;
import solar.rpg.jdata.data.stored.generic.JDataParameter;

public abstract class JFileStoredData implements IJStoredData {

    private final JDataField[] dataFields;

    /**
     * @param dataFields
     */
    protected JFileStoredData(JDataField[] dataFields) {
        this.dataFields = dataFields;
    }

    @Nullable
    @Override
    public JDataField getField(@NotNull String fieldName) {
        return null;
    }

    @NotNull
    @Override
    public JDataField getField(int fieldIndex) {
        return null;
    }

    @Override
    public void commit() {

    }

    @Override
    public void refresh() {

    }

    @NotNull
    @Override
    public JDataParameter[] getPrimaryFieldSearchParams() {
        return new JDataParameter[0];
    }

    @NotNull
    @Override
    public JDataParameter[] getPrimaryFieldSearchParams(@NotNull String[] keyValues) {
        return new JDataParameter[0];
    }

    @Override
    public boolean canCommit() {
        return false;
    }
}
