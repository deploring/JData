package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import solar.rpg.jdata.data.file.generic.IJFileElement;
import solar.rpg.jdata.data.file.generic.IJFileStoredData;
import solar.rpg.jdata.data.file.xml.JXMLElement;
import solar.rpg.jdata.data.stored.JStoredDataState;
import solar.rpg.jdata.data.stored.generic.JDataField;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Represents a piece of complex multivariate data stored under a file path.
 * Changes in memory can be persisted into a file, or reloaded from a file as needed.
 *
 * @author jskinner
 * @since 1.0.0
 */
public class JFileStoredData implements IJFileStoredData {

    /**
     * File path information.
     */
    private final File directory, file;

    /**
     * File stored objects have one root element to represent the entire file structure.
     * All defined fields are contained within the child elements, in the expected order.
     */
    private final IJFileElement rootElement;

    /**
     * @see JStoredDataState
     */
    private JStoredDataState dataState;

    /**
     * @param rootElement   Root element of the file stored data structure.
     * @param directoryPath Path of the directory where the file will be created/stored.
     * @param fileName      Name of the file where the stored data is located.
     */
    protected JFileStoredData(IJFileElement rootElement, String directoryPath, String fileName) {
        this.rootElement = rootElement;
        this.directory = Paths.get(directoryPath).toFile();
        this.file = Paths.get(directoryPath + File.pathSeparator + fileName).toFile();
    }

    /**
     * @return Array containing all fields for this file stored data object and their associated information.
     */
    @NotNull
    @Override
    public JDataField[] getFields() {
        return Objects.requireNonNull(rootElement.getChildFieldInfo());
    }

    /**
     * @param fieldName Name that uniquely identifies the name of the field that stores the given value.
     * @return Value of the given field under the given field name.
     */
    @NotNull
    @Override
    public Serializable getValue(String fieldName) {
        return rootElement.getChildren().stream().filter(child -> child.getFieldInfo().fieldName().equals(fieldName)).findFirst().orElseThrow();
    }

    /**
     * @param fieldIndex Index of the child element to get the associated value from.
     * @return Value of the given child element at the given index.
     */
    @NotNull
    @Override
    public Serializable getValue(int fieldIndex) {
        JXMLElement child = (JXMLElement) rootElement.getChildren().get(fieldIndex);
        return child.getValue();
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

    @Override
    public File getDirectory() {
        return directory;
    }

    @Override
    public File getFile() {
        return file;
    }
}
