package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.JStoredDataState;
import solar.rpg.jdata.data.stored.file.attribute.IJAttributable;
import solar.rpg.jdata.data.stored.file.attribute.JAttributes;
import solar.rpg.jdata.data.stored.generic.JStoredData;

import java.io.File;
import java.nio.file.Path;

/**
 * {@code JFileStoredData} is a sub-implementation of {@link JStoredData} where the underlying storage medium is based
 * around files. Each instance represents stored data saved in a file, based on the structure of that instance's class.
 *
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JFileStoredData extends JStoredData implements IJAttributable {

    //TODO: Subscriber of changes? Detect changes in some way

    @Nullable
    private Path filePath;
    @Nullable
    private JAttributes attributes;

    /**
     * Initialises the stored data object using the supplied file path. If the file does not exist, only the structure
     * of nested elements is created and any data fields are initialised as null.
     *
     * @param filePath The file path of the stored data (or where it will be created).
     */
    public final void initialise(@NotNull Path filePath) {
        if (getStoredDataState() != JStoredDataState.UNITIALISED)
            throw new IllegalStateException("Already initialised");
        this.filePath = filePath;

        File file = filePath.toFile();

        if (file.exists()) {
            throw new UnsupportedOperationException("");
        } else initialiseNew();

        /*String fileExtension = FilenameUtils.getExtension(file.getName()).toLowerCase();
        switch (fileExtension) {
            case "xml":
                // do something
                break;
            default:
                throw new IllegalArgumentException(String.format("Unsupported file extension .%s", fileExtension));
        }*/
    }

    private void initialiseNew() {
        setStoredDataState(JStoredDataState.CREATED);
        JFileElementFactory fileElementFactory = new JFileElementFactory();
        attributes = fileElementFactory.newAttributes(this.getClass());
        fileElementFactory.initialiseDataFields(this);
    }

    private void initialiseExisting(@NotNull Path filePath) {

    }

    /**
     * @return {@link Path} object representing the file system path to the stored data file.
     */
    @NotNull
    public final Path getFilePath() {
        if (filePath == null) throw new IllegalStateException("Not initialised");
        return filePath;
    }

    @NotNull
    @Override
    public final JAttributes getAttributes() {
        if (attributes == null) throw new IllegalStateException("Not initialised");
        return attributes;
    }

    @Override
    public final void onCommit() {
        //TODO: Commit logic.
    }

    @Override
    public final void onRefresh() {
        //TODO: Refresh logic.
    }

    @Override
    public final void onDelete() {
        onDelete();
        //TODO: Deletion logic.
    }
}
