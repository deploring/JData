package solar.rpg.jdata.data.stored.file.factory;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import solar.rpg.jdata.data.stored.JUtils;
import solar.rpg.jdata.data.stored.file.JFileElement;
import solar.rpg.jdata.data.stored.file.JFileElementGroup;
import solar.rpg.jdata.data.stored.file.JFileStoredData;
import solar.rpg.jdata.data.stored.file.attribute.IJAttributable;
import solar.rpg.jdata.data.stored.file.attribute.JAttributedField;
import solar.rpg.jdata.data.stored.file.attribute.JAttributes;
import solar.rpg.jdata.data.stored.file.attribute.JHasAttributes;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This factory class is used in the process of instantiating file elements and stored data from existing file data.
 * Nested elements are also instantiated from existing file data.
 *
 * @author jskinner
 * @since 1.0.0
 */
public class JXMLElementFactory {

    @NotNull
    private final Element documentElement;

    public JXMLElementFactory(@NotNull Path filePath)
    {
        File theFile = filePath.toFile();
        if (!theFile.exists())
            throw new IllegalStateException("File does not exist: " + filePath);
        if (!theFile.canRead())
            throw new IllegalStateException("Cannot read file: " + filePath);
        try {
            Document XMLDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(theFile);
            XMLDoc.normalize();
            documentElement = XMLDoc.getDocumentElement();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new IllegalArgumentException("Unable to parse XML file", e);
        }
    }

    public final Element getDocumentElement() {
        return documentElement;
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
            "Node name %s does not match class name %s",
            nodeName,
            expectedName
        ));
    }

    @NotNull
    public JAttributes createAttributes(@NotNull Node node, @NotNull Class<?> attributable)
    {
        if (!IJAttributable.class.isAssignableFrom(attributable))
            throw new IllegalArgumentException("Provided class is not attributable");
        if (!attributable.isAnnotationPresent(JHasAttributes.class))
            return new JAttributes();
        if (!node.hasAttributes()) throw new IllegalArgumentException("Attributes not found");

        JHasAttributes attributes = attributable.getAnnotation(JHasAttributes.class);

        if (attributes.names().length != attributes.types().length)
            throw new IllegalArgumentException("Number of attribute names and types do not match");

        return new JAttributes(
            List.of(attributes.names()),
            IntStream.range(0, attributes.names().length).mapToObj(i -> {
                Node attribute = node.getAttributes().item(i);
                validateNodeName(attribute, attributes.names()[i]);
                return attribute.getNodeValue();
            }).collect(Collectors.toList()),
            List.of(attributes.types()));
    }

    /**
     * @param parentNode     The parent node which contains the node we are looking for.
     * @param targetNodeName The name of the target node to return.
     * @return The matching target node.
     * @throws IllegalArgumentException A single result was not found.
     */
    private Node getTargetNode(@NotNull Node parentNode, @NotNull String targetNodeName)
    {
        return IntStream.range(0, parentNode.getChildNodes().getLength())
            .mapToObj(i -> parentNode.getChildNodes().item(i))
            .filter(node -> node.getNodeName().equalsIgnoreCase(targetNodeName))
            .collect(JUtils.singletonCollector());
    }

    /**
     * Initialises all data fields for a file element or stored data, using existing XML data.
     *
     * @param parentNode       The XML node which contains the data fields.
     * @param dataFieldsObject The object with data fields to initialise.
     */
    private void initialiseDataFields(@NotNull Node parentNode, @NotNull Object dataFieldsObject)
    {
        for (Field field : dataFieldsObject.getClass().getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            if (fieldType.isAssignableFrom(dataFieldsObject.getClass())) {
                throw new IllegalArgumentException("Class cannot contain itself as a data field");
            }

            Node targetNode = getTargetNode(parentNode, field.getName());

            if (!IJAttributable.class.isAssignableFrom(fieldType)) {
                JUtils.writePrivateField(dataFieldsObject, field, targetNode.getNodeValue());
            }

            JAttributes fieldAttributes = createAttributes(targetNode, fieldType);

            if (JAttributedField.class.isAssignableFrom(fieldType)) {
                JUtils.writePrivateField(
                    dataFieldsObject,
                    field,
                    JAttributedField.create(targetNode.getNodeValue(), fieldAttributes));
            } else if (JFileElementGroup.class.isAssignableFrom(fieldType)) {
                JUtils.writePrivateField(
                    dataFieldsObject,
                    field,
                    createElement(targetNode, JFileElementGroup.class, fieldAttributes));
            } else if (JFileElement.class.isAssignableFrom(fieldType)) {
                JUtils.writePrivateField(
                    dataFieldsObject,
                    field,
                    createElement(targetNode, fieldType, fieldAttributes));
            }
        }
    }


    @NotNull
    public <T> T createElement(
        @NotNull Node parentNode,
        @NotNull Class<T> elementClass,
        @NotNull JAttributes elementFieldAttributes)
    {
        if (!JFileElement.class.isAssignableFrom(elementClass))
            throw new IllegalArgumentException("Class does not inherit from JFileElement");

        try {
            JAttributes elementClassAttributes = createAttributes(parentNode, elementClass);

            T element;
            boolean hasClassAttributes = !elementClassAttributes.isEmpty();
            boolean hasFieldAttributes = !elementFieldAttributes.isEmpty();

            if (hasClassAttributes && hasFieldAttributes)
                throw new IllegalArgumentException("Element has multiple attribute definitions");

            try {
                element = elementClass.getDeclaredConstructor(JAttributes.class).newInstance(hasFieldAttributes
                                                                                             ? elementFieldAttributes
                                                                                             : elementClassAttributes);
            } catch (NoSuchMethodException e) {
                element = elementClass.getDeclaredConstructor().newInstance();
            }

            initialiseDataFields(parentNode, element);
            return element;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("Unable to create new child element", e);
        }
    }

    public <T extends JFileStoredData> void initialiseStoredData(T fileStoredData)
    {
        validateNodeName(documentElement, fileStoredData.getClass());

        JAttributes classAttributes = createAttributes(documentElement, fileStoredData.getClass());
        initialiseDataFields(documentElement, fileStoredData);
    }
}