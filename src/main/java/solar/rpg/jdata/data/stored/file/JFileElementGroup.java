package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.JUtils;
import solar.rpg.jdata.data.stored.file.attribute.JAttributes;
import solar.rpg.jdata.data.stored.file.factory.JNewFileElementFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * This class is an extension of {@link JFileElement} with the sole purpose of acting as a container for a specific type
 * of element, <em>i.e., a "locations" group for a list of "location" elements</em>. Each element's lifecycle is managed
 * through this class, and new elements can be created on demand.
 *
 * @param <E> The type of element that is managed by this group.
 */
public final class JFileElementGroup<E extends JFileElement> extends JFileElement implements Iterable<E> {

    @NotNull
    private final Class<E> elementClass;
    @NotNull
    private final JNewFileElementFactory newFileElementFactory;
    @NotNull
    private final List<E> children;

    /**
     * Constructs a new {@code JFileElementGroup} instance.
     *
     * @param elementClass The class of the type of element stored.
     * @param children     The existing elements.
     * @param attributes   The attributes associated with this element.
     */
    public JFileElementGroup(@NotNull Class<E> elementClass, @NotNull List<E> children, @NotNull JAttributes attributes)
    {
        super(attributes);
        this.elementClass = elementClass;
        this.children = children;
        newFileElementFactory = new JNewFileElementFactory();
    }

    /**
     * Constructs a new {@code JFileElementGroup} instance without any child elements.
     *
     * @param elementClass The class of the type of element stored.
     * @param attributes   The attributes associated with this element.
     */
    public JFileElementGroup(@NotNull Class<E> elementClass, @NotNull JAttributes attributes)
    {
        this(elementClass, new ArrayList<>(), attributes);
    }

    /**
     * Creates a new child element which is placed into the list of child elements and returned. Any nested elements
     * inside the child element are also created.
     *
     * @return The new child element.
     */
    @NotNull
    public E newChild()
    {
        E newElement = newFileElementFactory.createElement(elementClass);
        children.add(newElement);
        return newElement;
    }

    /**
     * @param index The index of the child element.
     * @return The child element at the given index.
     */
    @NotNull
    public E get(int index)
    {
        return children.get(index);
    }

    /**
     * @param elementFilter A condition to match against a <u>single</u> child element.
     * @return The matched child element, otherwise -1 if no match was found.
     * @throws IllegalArgumentException Multiple matches were found.
     */
    @Nullable
    public E get(@NotNull Predicate<E> elementFilter)
    {
        int elementIndex = indexOf(elementFilter);
        return elementIndex == -1 ? null : get(elementIndex);
    }

    /**
     * @param elementFilter A condition to match against a <u>single</u> child element.
     * @return The index of the matched child element, otherwise -1 if no match was found.
     * @throws IllegalArgumentException Multiple matches were found.
     */
    public int indexOf(@NotNull Predicate<E> elementFilter)
    {
        int result = -1;
        for (int i = 0, childrenSize = children.size(); i < childrenSize; i++) {
            E element = children.get(i);
            if (elementFilter.test(element)) {
                if (result != -1) throw new IllegalArgumentException("Multiple elements found");
                else result = i;
            }
        }
        return result;
    }

    /**
     * @param elementFilter A condition to match against any child element.
     * @return True if any match was found, otherwise false.
     */
    public boolean contains(@NotNull Predicate<E> elementFilter)
    {
        return children.stream().anyMatch(elementFilter);
    }

    /**
     * Removes the child element at the given index.
     *
     * @param index The index of the child element.
     */
    public void remove(int index)
    {
        children.remove(index);
    }

    /**
     * Removes any child elements that meet the specified condition.
     *
     * @param elementFilter A condition to match against any child element.
     */
    public void removeIf(@NotNull Predicate<E> elementFilter)
    {
        children.removeIf(elementFilter);
    }

    public void clear()
    {
        children.clear();
    }

    @NotNull
    @Override
    public Iterator<E> iterator()
    {
        return children.iterator();
    }
}
