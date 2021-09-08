package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.file.generic.IJFileElement;
import solar.rpg.jdata.data.file.generic.IJFileNode;
import solar.rpg.jdata.data.file.generic.IJFileTextNode;
import solar.rpg.jdata.data.schema.file.IJFileElementSchema;
import solar.rpg.jdata.data.stored.JStoredDataState;
import solar.rpg.jdata.data.stored.generic.IJStoredData;
import solar.rpg.jdata.data.stored.generic.JDataField;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Represents a piece of complex multivariate data stored under a file path.
 * Changes in memory can be persisted into a file, or reloaded from a file as needed.
 *
 * @param <T> Specific type of {@link IJFileElement} handled by this file stored object.
 * @author jskinner
 * @see IJFileNode
 * @since 1.0.0
 */
public abstract class JFileStoredData<T extends IJFileElement> implements IJStoredData {

    /**
     * File path information.
     */
    private final File directory, file;

    /**
     * File stored objects have one root element to represent the entire file structure.
     * All defined fields are contained within the child elements, defined by the schema.
     *
     * @see #getSchema()
     */
    private final T rootElement;

    /**
     * @see JStoredDataState
     */
    private JStoredDataState dataState;

    /**
     * @param rootElement   Root element of the stored data file.
     * @param directoryPath Path of the directory where the file will be created/stored.
     * @param fileName      Name of the file where the stored data is located.
     */
    protected JFileStoredData(T rootElement, String directoryPath, String fileName) {
        this.directory = Paths.get(directoryPath).toFile();
        this.file = Paths.get(directoryPath, fileName).toFile();
        this.rootElement = rootElement;
    }

    /**
     * @return Schema information that defines the structure of this particular {@link JFileStoredData} object.
     */
    public abstract IJFileElementSchema getSchema();

    /**
     * @return {@link File} object representing the directory path containing the file (must exist).
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * @return {@link File} object representing the file path (may or may not exist).
     */
    public File getFile() {
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

    /**
     * @return Array containing field information for all nodes belonging to the root element.
     */
    @NotNull
    @Override
    public JDataField[] getFields() {
        return Objects.requireNonNull(rootElement.getChildFieldInfo());
    }

    /**
     * @param fieldName Name that uniquely identifies the name of the top-level element to get a value from.
     * @return Value of the given element under the given field name.
     */
    @NotNull
    @Override
    public Serializable getValue(String fieldName) {
        return checkTextNodeValue(rootElement.getChild(fieldName));
    }

    /**
     * @param fieldIndex Index of the top-level text node to get the associated value from.
     * @return Value of the top-level element found at the given index.
     */
    @NotNull
    @Override
    public Serializable getValue(int fieldIndex) {
        return checkTextNodeValue(rootElement.getChildren()[fieldIndex]);
    }

    /**
     * @param node The node to retrieve the value from. This must be an {@link IJFileTextNode}.
     * @return The value stored by the given node, under the assumption that is an {@link IJFileTextNode}.
     * @throws AssertionError Given node was not an {@link IJFileTextNode}.
     */
    private Serializable checkTextNodeValue(IJFileNode node) {
        assert node instanceof IJFileTextNode : "Expected file text node";
        return ((IJFileTextNode) node).getValue();
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
