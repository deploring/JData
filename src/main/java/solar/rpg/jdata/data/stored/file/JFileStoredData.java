package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.stored.JStoredDataState;
import solar.rpg.jdata.data.stored.file.attribute.IJAttributable;
import solar.rpg.jdata.data.stored.file.attribute.JAttributes;
import solar.rpg.jdata.data.stored.generic.IJStoredData;

import java.io.File;
import java.nio.file.Paths;
import java.util.function.Predicate;

/**
 * Represents a piece of complex multivariate data stored under a file path.
 * Changes in memory can be persisted into a file, or reloaded from a file as needed.
 *
 * @author jskinner
 * @since 1.0.0
 */
public abstract class JFileStoredData implements IJStoredData, IJAttributable {

    //TODO: Subscriber of changes?

    @NotNull
    private final File directory, file;
    @NotNull
    private JStoredDataState dataState;
    @NotNull
    private JAttributes attributes;

    /**
     * @param directoryPath Path of the directory where the file will be created/stored.
     * @param fileName      Name of the file where the stored data is located.
     */
    protected JFileStoredData(String directoryPath, String fileName) {
        this.directory = Paths.get(directoryPath).toFile();
        this.file = Paths.get(directoryPath, fileName).toFile();
    }

    /**
     * @return {@link File} object representing the directory path containing the file (must exist).
     */
    public final File getDirectory() {
        return directory;
    }

    /**
     * @return {@link File} object representing the file path (may or may not exist).
     */
    public final File getFile() {
        return file;
    }

    /**
     * @return True, if the given directory path exists, is a directory, and the JVM has read+write access.
     */
    public boolean isDirectoryPathValid() {
        return isPathValid(getDirectory(), File::isDirectory);
    }

    /**
     * @return True, if the given file exists, is a file, and the JVM has read+write access.
     */
    public boolean isFilePathValid() {
        assert isDirectoryPathValid() : "Expected valid directory path";
        return isPathValid(getFile(), File::isFile);
    }

    /**
     * @param path          File path to validate.
     * @param pathTypeCheck Predicate to check the path type, e.g. file or directory.
     * @return True, if the file path exists, is the correct path type, and the JVM has read+write access.
     */
    private boolean isPathValid(File path, Predicate<File> pathTypeCheck) {
        return path.exists() && pathTypeCheck.test(path) && path.canRead() && path.canWrite();
    }

    @NotNull
    @Override
    public JAttributes getAttributes() {
        return attributes;
    }

    @NotNull
    @Override
    public JStoredDataState getStoredDataState() {
        return dataState;
    }

    @Override
    public void setStoredDataState(JStoredDataState dataState) {
        this.dataState = dataState;
    }

    @Override
    public void commit() {
        //TODO: Commit logic.
    }

    @Override
    public void refresh() {
        //TODO: Refresh logic.
    }

    @Override
    public void delete() {
        onDelete();
        //TODO: Deletion logic.
    }
}
