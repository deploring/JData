package solar.rpg.jdata.data.file.xml;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * {@code JDOMToXMLHelper} is a helper class that will parse an XML file into
 * {@link Node} objects to perform a logical translation of the contents into
 * a {@link JXMLElement} object.
 *
 * @author jskinner
 * @see JXMLToDOMHelper
 * @since 1.0.0
 */
public class JDOMToXMLHelper {

    @NotNull
    private final Document XMLDoc;

    public JDOMToXMLHelper(@NotNull Path filePath) throws IOException {
        File theFile = filePath.toFile();
        if (!theFile.exists()) throw new IOException("File does not exist: " + filePath);
        if (!theFile.canRead()) throw new IOException("Cannot read file: " + filePath);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            XMLDoc = builder.parse(theFile);
            XMLDoc.normalize();
        } catch (Exception e) {
            throw new IOException("Unable to read XML file: " + e.getMessage());
        }
    }

    /*@TestOnly
    public JDOMToXMLHelper(@NotNull ByteArrayInputStream inputData) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            XMLDoc = builder.parse(inputData);
            XMLDoc.normalize();
        } catch (Exception e) {
            throw new RuntimeException("Unable to read XML file: " + e.getMessage());
        }
    }*/

    /**
     * Processes the XML document and produces a single {@link JXMLElement}.
     * This resultant {@link JXMLElement} is the root, and is logically linked to all
     * the other elements in the document. This allows logical a one-way depth-first
     * traversal of the XML document.
     *
     * @return The root (document) element, logically represented and linked to all other elements.
     */
    public JXMLElement read() {
        return processElement(XMLDoc.getDocumentElement());
    }

    /**
     * Reads a list of child elements and processes them using {@link #processElement(Node)}.
     * Siblings are loaded in the order they are saved in the file.
     *
     * @param children Element's list of child nodes.
     * @return List of {@code JXMLElement} objects that belong to the parent element.
     * @see #processElement(Node)
     */
    private List<JXMLElement> processChildren(NodeList children) {
        return IntStream.range(0, children.getLength()).filter(i -> children.item(i).getNodeType() == Node.ELEMENT_NODE).mapToObj(i -> processElement(children.item(i))).toList();
    }

    /**
     * Collects all the information required to construct a {@code LinkedXMLNode}.
     * Constructs any child nodes that this node is a parent of prior to constructing
     * itself.
     *
     * @param element XML document node to process.
     * @return Logical representation of XML document node as a {@code LinkedXMLNode}.
     */
    private JXMLElement processElement(Node element) {
        String tagName = element.getNodeName();
        Map<String, String> attributes = readAttributes(element);
        List<JXMLElement> children = processChildren(element.getChildNodes());
        String tagValue = children.size() == 0 ? element.getTextContent() : null;
        return new JXMLElement(element, tagValue, attributes, children);
    }

    /**
     * Reads the XML node's attributes and places them in a {@code TreeMap}.
     *
     * @param element Element that is currently being read.
     * @return Attributes represented by a {@code Store}.
     */
    private Map<String, String> readAttributes(Node element) {
        Map<String, String> result = new TreeMap<>();
        if (element.getAttributes() != null)
            result = IntStream.range(0, element.getAttributes().getLength()).mapToObj(i -> element.getAttributes().item(i)).collect(Collectors.toMap(Node::getNodeName, Node::getNodeValue, (a, b) -> b, TreeMap::new));
        return result;
    }
}
