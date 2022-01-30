package solar.rpg.jdata.data.stored.file;

import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.JStoredDataState;
import solar.rpg.jdata.data.stored.file.attribute.IJFileElementModel;
import solar.rpg.jdata.data.stored.file.attribute.JAttributes;
import solar.rpg.jdata.data.stored.file.factory.JNewFileElementFactory;
import solar.rpg.jdata.data.stored.file.factory.JXMLElementFactory;
import solar.rpg.jdata.data.stored.generic.JStoredData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * {@code JFileStoredData} is a sub-implementation of {@link JStoredData} where the underlying storage medium is based
 * around files. Each instance represents stored data saved in a file, based on the structure of that instance's class.
 *
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JFileStoredData extends JStoredData implements IJFileElementModel {

    //TODO: Subscriber of changes? Detect changes in some way

    @Nullable
    private File file;
    @Nullable
    private JAttributes attributes;

    /**
     * Initialises the stored data object using the supplied file path. If the file does not exist, only the structure
     * of nested elements is created and any data fields are initialised as null.
     *
     * @param filePath The file path of the stored data (or where it will be created).
     */
    public final void initialise(@NotNull Path filePath)
    {
        if (getStoredDataState() != JStoredDataState.UNITIALISED)
            throw new IllegalStateException("Already initialised");
        this.file = filePath.toFile();

        if (file.exists()) initialiseExisting();
        else initialiseNew();
    }

    private void initialiseNew()
    {
        JNewFileElementFactory fileElementFactory = new JNewFileElementFactory();
        fileElementFactory.initialiseStoredData(this);
        setStoredDataState(JStoredDataState.CREATED);

        try {
            if (!getFile().createNewFile())
                throw new IOException();
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Unable to create stored data file %s", getFile()), e);
        }
    }

    private void initialiseExisting()
    {
        String fileExtension = FilenameUtils.getExtension(getFile().getName()).toLowerCase();
        switch (fileExtension) {
            case "xml" -> {
                JXMLElementFactory xmlElementFactory = new JXMLElementFactory(getFile());
                //TODO: Should we make an overloaded method that automatically uses the document element?
                xmlElementFactory.initialiseStoredData(this);
            }
            default -> throw new IllegalArgumentException(String.format(
                "Unsupported file extension .%s",
                fileExtension));
        }
        setStoredDataState(JStoredDataState.UNCHANGED);
    }

    /**
     * @return {@link File} object representing stored data file on the filesystem.
     */
    @NotNull
    public final File getFile()
    {
        if (file == null) throw new IllegalStateException("Not initialised");
        return file;
    }

    @NotNull
    @Override
    public final JAttributes getAttributes()
    {
        if (attributes == null) throw new IllegalStateException("Not initialised");
        return attributes;
    }

    @Override
    public final void onCommit()
    {
        //TODO: Commit logic.
    }

    @Override
    public final void onRefresh()
    {
        //TODO: Refresh logic.
    }

    @Override
    public final void onDelete()
    {
        onDelete();
        //TODO: Deletion logic.
    }
}
