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

    String getFilePath();

    default File getFile() {
        return new File(getFilePath());
    }
}
