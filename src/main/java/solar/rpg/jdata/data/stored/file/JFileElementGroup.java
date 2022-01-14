package solar.rpg.jdata.data.stored.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.JUtils;
import solar.rpg.jdata.data.stored.file.attribute.JAttributes;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * This class is an extension of {@link JFileElement} with the sole purpose of acting as a container for a specific type
 * of element, <em>i.e., a "locations" group for a list of "location" elements</em>. Each element's lifecycle is managed
 * through this class, and new elements can be created on demand.
 *
 * @param <E> The type of element that is ma
 */
public final class JFileElementGroup<E extends JFileElement> extends JFileElement {

    @NotNull
    private final List<E> children;

    /**
     * Constructs a new {@code JFileElementGroup} instance.
     *
     * @param children   The existing elements.
     * @param attributes The attributes associated with this element.
     */
    public JFileElementGroup(@NotNull List<E> children, @NotNull JAttributes attributes) {
        super(attributes);
        this.children = children;
    }

    /**
     * Constructs a new {@code JFileElementGroup} instance without any child elements.
     *
     * @param attributes The attributes associated with this element.
     */
    public JFileElementGroup(@NotNull JAttributes attributes) {
        super(attributes);
        this.children = new ArrayList<>();
    }

    //TODO: Is this OK??
    @NotNull
    public E newChild() {
        Class<E> elementClass = JUtils.getGenericType(this.getClass());

        try {
            E newElement = elementClass.getDeclaredConstructor().newInstance();
            children.add(newElement);
            return newElement;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(String.format("Unable to create new child element: %s", e.getMessage()));
        }
    }

    /**
     * @param index The index of the child element.
     * @return The child element at the given index.
     */
    @NotNull
    public E get(int index) {
        return children.get(index);
    }

    /**
     * @param elementFilter A condition to match against a <u>single</u> child element.
     * @return The matched child element, otherwise -1 if no match was found.
     * @throws IllegalArgumentException Multiple matches were found.
     */
    @Nullable
    public E get(@NotNull Predicate<E> elementFilter) {
        int elementIndex = indexOf(elementFilter);
        return elementIndex == -1 ? null : get(elementIndex);
    }

    /**
     * @param elementFilter A condition to match against a <u>single</u> child element.
     * @return The index of the matched child element, otherwise -1 if no match was found.
     * @throws IllegalArgumentException Multiple matches were found.
     */
    public int indexOf(@NotNull Predicate<E> elementFilter) {
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
    public boolean contains(@NotNull Predicate<E> elementFilter) {
        return children.stream().anyMatch(elementFilter);
    }

    /**
     * Removes the child element at the given index.
     *
     * @param index The index of the child element.
     */
    public void remove(int index) {
        children.remove(index);
    }

    /**
     * Removes any child elements that meet the specified condition.
     *
     * @param elementFilter A condition to match against any child element.
     */
    public void removeIf(@NotNull Predicate<E> elementFilter) {
        children.removeIf(elementFilter);
    }

    public void clear() {
        children.clear();
    }
}
