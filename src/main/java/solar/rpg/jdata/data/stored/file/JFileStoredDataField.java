package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.file.IJFileElement;
import solar.rpg.jdata.data.stored.generic.JStoredDataField;

public class JFileStoredDataField extends JStoredDataField<IJFileElement> {

    /**
     * @param fieldName Field name identifier.
     * @param isPrimary True, if this field is a primary identifier of the stored data object.
     */
    public JFileStoredDataField(@NotNull String fieldName, boolean isPrimary) {
        super(fieldName, isPrimary);
    }
}
