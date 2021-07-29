package solar.rpg.jdata.data.file;

/**
 * Denotes the different writing restrictions that a {@link IJFileElement} may be subject to:
 * <ul>
 *     <li><strong>READ_ONLY</strong> - No modifications can be made.</li>
 *     <li><strong>WRITE_ONCE</strong> - Modifications can be made once, but cannot be overwritten.</li>
 *     <li><strong>READ_WRITE</strong> - No restrictions to modifications.</li>
 * </ul>
 *
 * @author jskinner
 * @since 1.0.0
 */
public enum JFileReadStatus {
    READ_ONLY,
    WRITE_ONCE,
    READ_WRITE
}
