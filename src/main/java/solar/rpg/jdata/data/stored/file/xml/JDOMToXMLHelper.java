package solar.rpg.jdata.data.stored.file.xml;

import org.w3c.dom.Node;

/**
 * {@code JDOMToXMLHelper} is a helper class that will parse an XML file into {@link Node} objects to perform a logical
 * translation of the contents into {@link JXMLNode} objects.
 *
 * @author jskinner
 * @see JXMLToDOMHelper
 * @since 1.0.0
 */
public class JDOMToXMLHelper {

    /*@TestOnly
    public JDOMToXMLHelper(@NotNull ByteArrayInputStream inputData) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            XMLDoc = builder.parse(inputData);
            XMLDoc.normalize();
        } catch (Exception e) {
            throw new RuntimeException("Unable to read XML file: " + e.getMessage());
        }
    }

    /**
     * Processes the XML document and produces a single {@link IJFileElement} which represents the root element. It is
     * logically linked to all the other child elements in the document. This allows logical a one-way depth-first
     * traversal of the XML document.
     *
     * @return The root (document) element, linked to all other child elements.
     *
    public IJFileElement read(@NotNull Path filePath) throws IOException {
        File theFile = filePath.toFile();
        if (!theFile.exists()) throw new IOException("File does not exist: " + filePath);
        if (!theFile.canRead()) throw new IOException("Cannot read file: " + filePath);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document XMLDoc = builder.parse(theFile);
            XMLDoc.normalize();

            JXMLNode rootNode = processNode(XMLDoc.getDocumentElement());
            if (!(rootNode instanceof IJFileElement rootElement)) throw new Illegal
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new IOException("Unable to load XML file: " + e.getMessage());
        }
    }

    /**
     * Reads the XML node's attributes and places them in a {@code TreeMap}.
     *
     * @param element Element that is currently being read.
     * @return Attributes represented by a {@code Store}.
     *
    private Map<String, String> readAttributes(Node element) {
        Map<String, String> result = new TreeMap<>();
        if (element.getAttributes() != null)
            result = IntStream.range(0, element.getAttributes().getLength()).mapToObj(i -> element.getAttributes().item(
                    i)).collect(Collectors.toMap(Node::getNodeName, Node::getNodeValue, (a, b) -> b, TreeMap::new));
        return result;
    }

    private IJFileElement processElementNode(Node node) {
        if (Arrays.asList(Node.ELEMENT_NODE, Node.DOCUMENT_NODE).contains(node.getNodeType()))
            throw new IllegalArgumentException("Node is not an element node");
    }

    private JXMLTextNode processTextNode(Node node) {
        if (node.getNodeType() != Node.TEXT_NODE) throw new IllegalArgumentException("Node is not a text node");
    }

    /**
     * Collects all the information required to construct a {@code LinkedXMLNode}.
     * Constructs any child nodes that this node is a parent of prior to constructing
     * itself.
     *
     * @param element XML document node to process.
     * @return Logical representation of XML document node as a {@code LinkedXMLNode}.
     *
    private JXMLNode processNode(Node node) {
        String nodeName = node.getNodeName();
        Map<String, String> attributes = readAttributes(node);

        switch (node.getNodeType()) {
            case Node.DOCUMENT_NODE:
            case Node.ELEMENT_NODE: {
            }
            case Node.TEXT_NODE: {
                String textContent = node.getTextContent();
                return new JXMLTextNode()
            }
            default:
                throw new UnsupportedOperationException(String.format("Unsupported node type %d", node.getNodeType()));
        }
        return new JXMLNode(element, tagValue, attributes, children);
    }*/
}
