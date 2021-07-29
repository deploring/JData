package solar.rpg.jdata.data.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

public interface IJFileElement {

    @NotNull
    String getName();

    void setName(@NotNull String name);

    @Nullable
    String getValue();

    void setValue(@Nullable String value);

    @NotNull
    JFileReadStatus getReadStatus();

    void setReadStatus(@NotNull JFileReadStatus fileReadStatus);

    @NotNull
    LinkedList<? extends IJFileElement> getChildren();

    /**
     * Returns the number of child elements belonging to this element.
     */
    default int getChildrenCount() {
        return getChildren().size();
    }

    /**
     * Returns true if this element has any child elements.
     */
    default boolean hasChildren() {
        return getChildren().size() > 0;
    }

    /**
     * Returns the child element with the specified name, otherwise null.
     *
     * @param name Name of the child element.
     */
    @Nullable
    default IJFileElement getChild(String name) {
        return getChildren().stream().filter(child -> child.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Returns the {@code n}th child element where {@code n} is the index.
     *
     * @param index Child element index. Starts from 0.
     */
    @Nullable
    default IJFileElement getChild(int index) {
        return getChildren().get(index);
    }

    /**
     * Returns true if there is a child element with the given tag name.
     *
     * @param name Name of the child element.
     */
    default boolean hasChild(String name) {
        return getChild(name) != null;
    }

    /**
     * Returns all child elements that this element is directly or indirectly a parent of.
     *
     * @see #findChildren(IJFileElement, LinkedList)
     */
    @NotNull
    default IJFileElement[] getAllChildren() {
        LinkedList<IJFileElement> result = new LinkedList<>();
        findChildren(this, result);
        return result.toArray(new IJFileElement[0]);
    }

    /**
     * Recursive search for all child elements that the supplied element is a parent of.
     *
     * @param current Supplied parent element.
     * @param result  Result list that is returned once the search is done.
     * @see #getAllChildren()
     */
    default void findChildren(@NotNull IJFileElement current, @NotNull LinkedList<IJFileElement> result) {
        for (IJFileElement child : current.getChildren()) {
            result.add(child);
            findChildren(child, result);
        }
    }
}
