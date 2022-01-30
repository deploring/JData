package solar.rpg.jdata.data.stored.file.factory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import solar.rpg.jdata.data.stored.JUtils;
import solar.rpg.jdata.data.stored.file.JFileElement;
import solar.rpg.jdata.data.stored.file.JFileElementGroup;
import solar.rpg.jdata.data.stored.file.JFileStoredData;
import solar.rpg.jdata.data.stored.file.attribute.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This factory class is used in the process of instantiating file elements and stored data from existing XML data.
 * Nested elements are also instantiated from existing XML data.
 *
 * @author jskinner
 * @since 1.0.0
 */
public class JXMLElementFactory extends JFileElementFactory {

    @NotNull
    private final Element documentElement;

    public JXMLElementFactory(@NotNull File file)
    {
        if (!file.exists())
            throw new IllegalStateException("File does not exist: " + file);
        if (!file.canRead())
            throw new IllegalStateException("Cannot read file: " + file);
        try {
            Document XMLDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            XMLDoc.normalize();
            documentElement = XMLDoc.getDocumentElement();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new IllegalArgumentException("Unable to parse XML file", e);
        }
    }

    @Override
    public void initialiseStoredData(@NotNull JFileStoredData fileStoredData)
    {
        Class<? extends JFileStoredData> storedDataClass = fileStoredData.getClass();
        validateNodeName(documentElement, storedDataClass);

        JAttributes attributes = createAttributes(documentElement, storedDataClass.getAnnotation(JHasAttributes.class));
        initialiseAttributes(fileStoredData, attributes, new JAttributes());

        initialiseDataFields(documentElement, fileStoredData);
    }

    /**
     * Validates the name of an XML node to check that it matches the given class (case-insensitive).
     *
     * @param node  XML node to check.
     * @param clazz An instance of the class type.
     * @param <T>   The class type to check against.
     * @throws IllegalArgumentException Node name does not match class name.
     */
    private <T> void validateNodeName(@NotNull Node node, @NotNull Class<T> clazz)
    {
        validateNodeName(node, clazz.getSimpleName());
    }

    /**
     * Validates the name of an XML node to check that it matches the given name.
     *
     * @param node         XML node to check.
     * @param expectedName The name that the node is expected to have.
     * @throws IllegalArgumentException Node name does not expected name.
     */
    private void validateNodeName(@NotNull Node node, @NotNull String expectedName)
    {
        String nodeName = node.getNodeName();
        if (!nodeName.equalsIgnoreCase(expectedName)) throw new IllegalArgumentException(String.format(
            "Name %s does not match expected name %s",
            nodeName,
            expectedName
        ));
    }

    @NotNull
    public JAttributes createAttributes(@NotNull Element element, @Nullable JHasAttributes attributes)
    {
        if (attributes == null) return new JAttributes();

        if (!element.hasAttributes()) throw new IllegalArgumentException(String.format(
            "Attributes for %s not found",
            element.getNodeName()
        ));

        if (attributes.names().length != attributes.types().length)
            throw new IllegalArgumentException("Number of attribute names and types do not match");

        return new JAttributes(
            List.of(attributes.names()),
            IntStream.range(0, attributes.names().length).mapToObj(i -> {
                Node attribute = element.getAttributes().item(i);
                validateNodeName(attribute, attributes.names()[i]);
                return JDataEncoder.fromString(attribute.getTextContent(), attributes.types()[i]);
            }).collect(Collectors.toList()),
            List.of(attributes.types())
        );
    }

    /**
     * @param parentNode     The parent node which contains the node we are looking for.
     * @param targetNodeName The name of the target node to return.
     * @return The matching target node.
     * @throws IllegalArgumentException A single result was not found.
     */
    private Node getTargetNode(@NotNull Node parentNode, @NotNull String targetNodeName)
    {
        try {
            return IntStream.range(0, parentNode.getChildNodes().getLength())
                .mapToObj(i -> parentNode.getChildNodes().item(i))
                .filter(node -> node.getNodeName().equalsIgnoreCase(targetNodeName))
                .collect(JUtils.singletonCollector());
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(String.format(
                "Node %s not found in element %s",
                targetNodeName,
                parentNode.getNodeName()));
        }
    }

    /**
     * Initialises all data fields for a file element or stored data, using existing XML data.
     *
     * @param parentElement    The XML node which contains the data fields.
     * @param dataFieldsObject The object with data fields to initialise.
     */
    @SuppressWarnings("unchecked") // JFileElementGroup element type is guaranteed to be a descendant of JFileElement.
    private void initialiseDataFields(@NotNull Element parentElement, @NotNull Object dataFieldsObject)
    {
        for (Field field : dataFieldsObject.getClass().getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            if (fieldType.isAssignableFrom(dataFieldsObject.getClass())) {
                throw new IllegalArgumentException("Class cannot contain itself as a data field");
            }

            Node targetNode = getTargetNode(parentElement, field.getName());
            validateNodeName(targetNode, field.getName());

            if (!IJFileElementModel.class.isAssignableFrom(fieldType)) {
                JUtils.writePrivateField(
                    dataFieldsObject,
                    field,
                    JDataEncoder.fromString(targetNode.getTextContent(), fieldType)
                );
                continue;
            }

            if (!(targetNode instanceof Element element))
                throw new IllegalArgumentException(String.format(
                    "Node %s is not an element (is it missing attributes?)",
                    field.getName()
                ));

            JAttributes fieldAttributes =
                field.isAnnotationPresent(JHasAttributes.class)
                ? createAttributes(element, field.getAnnotation(JHasAttributes.class))
                : new JAttributes();

            if (JAttributedField.class.isAssignableFrom(fieldType)) {
                JUtils.writePrivateField(
                    dataFieldsObject,
                    field,
                    JAttributedField.create(
                        JDataEncoder.fromString(
                            targetNode.getTextContent(),
                            (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]
                        ),
                        fieldAttributes
                    )
                );
            }

            if (JFileElementGroup.class.isAssignableFrom(fieldType)) {
                JUtils.writePrivateField(
                    dataFieldsObject,
                    field,
                    createElementGroup(
                        element,
                        (Class<? extends JFileElement>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0],
                        fieldAttributes)
                );
            } else if (JFileElement.class.isAssignableFrom(fieldType)) {
                JUtils.writePrivateField(
                    dataFieldsObject,
                    field,
                    createElement(element, fieldType, fieldAttributes));
            }
        }
    }

    private <E extends JFileElement> JFileElementGroup<E> createElementGroup(
        @NotNull Element parentElement,
        @NotNull Class<E> elementClass,
        @NotNull JAttributes fieldAttributes)
    {
        List<E> children = new ArrayList<>();
        NodeList childElements = parentElement.getElementsByTagName(elementClass.getSimpleName());

        for (int i = 0; i < childElements.getLength(); i++) {
            Element childElement = (Element) childElements.item(i);
            children.add(createElement(childElement, elementClass, new JAttributes()));
        }

        return new JFileElementGroup<>(elementClass, children, fieldAttributes);
    }

    @NotNull
    public <T> T createElement(
        @NotNull Element parentElement,
        @NotNull Class<T> elementClass,
        @NotNull JAttributes fieldAttributes)
    {
        if (!JFileElement.class.isAssignableFrom(elementClass))
            throw new IllegalArgumentException("Class does not inherit from JFileElement");

        try {
            JAttributes elementClassAttributes = createAttributes(
                parentElement,
                elementClass.getAnnotation(JHasAttributes.class)
            );

            T element;
            boolean hasClassAttributes = !elementClassAttributes.isEmpty();
            boolean hasFieldAttributes = !fieldAttributes.isEmpty();

            if (hasClassAttributes && hasFieldAttributes)
                throw new IllegalArgumentException("Element has multiple attribute definitions");

            try {
                element = elementClass.getDeclaredConstructor(JAttributes.class).newInstance(hasFieldAttributes
                                                                                             ? fieldAttributes
                                                                                             : elementClassAttributes);
            } catch (NoSuchMethodException e) {
                if (hasClassAttributes || hasFieldAttributes)
                    throw new IllegalArgumentException(String.format(
                        "Constructor %s(JAttributes) not found",
                        elementClass.getSimpleName())
                    );
                element = elementClass.getDeclaredConstructor().newInstance();
            }

            initialiseDataFields(parentElement, element);
            return element;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("Unable to create new child element", e);
        }
    }
}