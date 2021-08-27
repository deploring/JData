package solar.rpg.jdata.data.file.generic;

import solar.rpg.jdata.data.stored.generic.IJStoredData;

import java.io.File;

/**
 * Represents an implementation of an {@link IJStoredData} object that uses a
 * file path to store data in a given format, <em>e.g. XML, JSON, etc.</em>
 *
 * @author jskinner
 * @since 1.0.0
 */
public interface IJFileStoredData extends IJStoredData {

    /**
     * @return {@code File} object representing the directory path containing the file (must exist).
     */
    File getDirectory();

    /**
     * @return {@code File} object representing the file path (may or may not exist).
     */
    File getFile();

    /**
     * @return True, if the given directory path exists, is a directory, and the JVM has read+write access.
     */
    default boolean isDirectoryPathValid() {
        File directory = getDirectory();
        return directory.exists() && directory.isDirectory() && directory.canRead() && directory.canWrite();
    }

    /**
     * @return True, if the given file exists, is a file, and the JVM has read+write access.
     */
    default boolean isFilePathValid() {
        assert isDirectoryPathValid() : "Expected valid directory path";
        File file = getFile();
        return file.exists() && file.isFile() && file.canRead() && file.canWrite();
    }
}
