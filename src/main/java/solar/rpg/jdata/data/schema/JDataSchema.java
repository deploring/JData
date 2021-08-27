package solar.rpg.jdata.data.schema;

import solar.rpg.jdata.data.stored.generic.IJStoredData;

/**
 * {@link JDataSchema} curates and maintains field information related to any
 * {@link IJStoredData} types under the given schema.
 * Field information can be added, modified, or removed as the schema evolves.
 * This schema can also generate {@link IJStoredData} classes that match what
 * the provided field information, in all supported file mediums (provided that
 * the storage medium is capable of storing the given data).
 *
 * @author jskinner
 * @since 1.0.0
 */
public class JDataSchema {
}