package solar.rpg.jdata.data.stored.file.saver;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import solar.rpg.jdata.data.stored.JUtils;
import solar.rpg.jdata.data.stored.file.JFileElement;
import solar.rpg.jdata.data.stored.file.JFileElementGroup;
import solar.rpg.jdata.data.stored.file.JFileStoredData;
import solar.rpg.jdata.data.stored.file.attribute.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.lang.reflect.Field;

/**
 * Saves a {@link JFileStoredData} object's structure and values in the XML format. This converts classes into element
 * nodes and their fields into text nodes or other element nodes depending on the type of data.
 */
public class JXMLStoredDataSaver {

    @NotNull
    private final File file;
    @NotNull
    private final Document document;

    public JXMLStoredDataSaver(@NotNull File file, @NotNull JFileStoredData storedData)
    {
        this.file = file;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = processElement(storedData, false);
            document.appendChild(root);
        } catch (ParserConfigurationException e) {
            throw new IllegalArgumentException("Unable to build XML file", e);
        }
    }

    public void save()
    {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Result output = new StreamResult(file);
            Source input = new DOMSource(document);

            transformer.transform(input, output);
        } catch (TransformerException e) {
            throw new IllegalArgumentException("Unable to save XML file", e);
        }
    }

    private void writeAttributes(@NotNull Element element, @NotNull JAttributes attributes)
    {
        for (String attributeName : attributes)
            element.setAttribute(attributeName, JDataEncoder.toString(attributes.get(attributeName)));
    }

    private <E extends IJFileElementModel> Element processElement(
        @NotNull E elementToSave,
        boolean hasFieldAttributes)
    {
        Class<? extends IJFileElementModel> elementClass = elementToSave.getClass();
        Element result = document.createElement(elementClass.getSimpleName());

        boolean hasAttributes =
            hasFieldAttributes || elementToSave.getClass().isAnnotationPresent(JHasAttributes.class);
        if (hasAttributes) writeAttributes(result, elementToSave.getAttributes());

        for (Field field : elementClass.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            if (fieldType.isAssignableFrom(elementClass))
                throw new IllegalArgumentException("Class cannot contain itself as a data field");

            Element childElement = document.createElement(field.getName());

            if (!IJFileElementModel.class.isAssignableFrom(fieldType))
                childElement.appendChild(
                    document.createTextNode(JDataEncoder.toString(JUtils.readPrivateField(elementToSave, field)))
                );
            else if (JAttributedField.class.isAssignableFrom(fieldType)) {
                JAttributedField<?> attributedField = JUtils.readPrivateField(elementToSave, field);
                childElement.appendChild(document.createTextNode(JDataEncoder.toString(attributedField.get())));

                if (field.isAnnotationPresent(JHasAttributes.class))
                    writeAttributes(childElement, attributedField.getAttributes());
            } else if (JFileElementGroup.class.isAssignableFrom(fieldType)) {
                JFileElementGroup<?> elementGroup = JUtils.readPrivateField(elementToSave, field);
                for (JFileElement elementGroupChild : elementGroup)
                    childElement.appendChild(processElement(elementGroupChild, false));
            } else if (JFileElement.class.isAssignableFrom(fieldType))
                childElement.appendChild(processElement(
                    JUtils.readPrivateField(elementToSave, field),
                    field.isAnnotationPresent(JHasAttributes.class)
                ));
            else throw new UnsupportedOperationException("Unsupported field type");

            result.appendChild(childElement);
        }

        return result;
    }
}
