package solar.rpg.jdata.data.schema.file;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.file.generic.IJFileNode;
import solar.rpg.jdata.data.stored.generic.JDataField;

public interface IJFileNodeSchema {

    /**
     * @return
     */
    @NotNull
    JDataField getFieldInfo();

    @NotNull
    IJFileNode build();
}
