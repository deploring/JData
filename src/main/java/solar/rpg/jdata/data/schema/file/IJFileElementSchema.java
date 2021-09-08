package solar.rpg.jdata.data.schema.file;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.file.generic.IJFileElement;

public interface IJFileElementSchema extends IJFileNodeSchema {

    @NotNull
    IJFileNodeSchema[] getChildNodeSchemas();

    boolean isStructural();

    @Override
    @NotNull
    IJFileElement build();
}
