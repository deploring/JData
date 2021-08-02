package solar.rpg.jdata.data.file.xml;

import org.jetbrains.annotations.TestOnly;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.LinkedList;

/**
 * {@code XMLReader} is a helper class that will read an XML file
 * and transform it into a logical representation using the class
 * {@code LinkedXMLNode}.
 */
public class XMLReader {

    private final Document XMLDoc;

    public XMLReader(String filePath) {
        File theFile = new File(filePath);
        if (!theFile.exists()) throw new IllegalArgumentException("File does not exist: " + filePath);
        if (!theFile.canRead()) throw new IllegalArgumentException("Cannot read file: " + filePath);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            XMLDoc = builder.parse(theFile);
            XMLDoc.normalize();
        } catch (Exception e) {
            throw new RuntimeException("Unable to read XML file: " + e.getMessage());
        }
    }

    @TestOnly
    public XMLReader(ByteArrayInputStream inputData) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            XMLDoc = builder.parse(inputData);
            XMLDoc.normalize();
        } catch (Exception e) {
            throw new RuntimeException("Unable to read XML file: " + e.getMessage());
        }
    }

    /**
     * Processes the XML document and produces a single {@code LinkedXMLNode}.
     * This {@code LinkedXMLNode} is the root, and is logically linked to all
     * of the other nodes in the document. This allows logical traversal of the
     * XML document.
     *
     * @return The root (document) node, logically represented and linked to all other nodes.
     */
    public JXMLElement read() {
        JXMLElement root = processNode(XMLDoc.getDocumentElement());
        root.post();
        return root;
    }

    /**
     * Reads a list of child nodes and processes them recursively.
     * Creates the sibling-level node links in a left-to-right order, where
     * siblings that are read first come before those that are read after.
     *
     * @param children Node's list of child nodes.
     * @return List of {@code LinkedXMLNodes} that belong to the parent node.
     * @see #processNode(Node)
     */
    private LinkedList<JXMLElement> readChildren(NodeList children) {
        LinkedList<JXMLElement> result = new LinkedList<>();
        JXMLElement previous;
        JXMLElement current = null;
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;

            previous = current;
            current = processNode(node);

            // Create sibling links.
            if (previous != null) {
                previous.setNext(current);
                current.setPrevious(previous);
            }

            result.add(current);
        }
        return result;
    }

    /**
     * Collects all the information required to construct a {@code LinkedXMLNode}.
     * Constructs any child nodes that this node is a parent of prior to constructing
     * itself.
     *
     * @param current XML document node to process.
     * @return Logical representation of XML document node as a {@code LinkedXMLNode}.
     */
    private JXMLElement processNode(Node current) {
        String tagName = current.getNodeName();
        Store<String, String> attributes = readAttributes(current);
        LinkedList<JXMLElement> children = readChildren(current.getChildNodes());
        String tagValue = children.size() == 0 ? current.getTextContent() : null;
        JXMLElement result = new JXMLElement(tagName, tagValue, attributes, children);

        // Create parent links. Child link is automatically created upon construction.
        children.forEach(child -> {
            child.setParent(result);
            child.post();
        });
        return result;
    }

    /**
     * Reads the XML node's attributes and places them in a {@code Store}.
     *
     * @param current Node that is currently being read.
     * @return Attributes represented by a {@code Store}.
     */
    private Store<String, String> readAttributes(Node current) {
        Store<String, String> result = new Store<>();
        if (current.getAttributes() != null)
            for (int i = 0; i < current.getAttributes().getLength(); i++) {
                Node attribute = current.getAttributes().item(i);
                result.set(attribute.getNodeName(), attribute.getNodeValue());
            }
        return result;
    }
}
