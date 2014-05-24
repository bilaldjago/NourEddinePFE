package dz.nouri.tools;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FDPProcessor {

	public static HashMap<String, Integer> getFDPVertexIndices(String fileName) {
		HashMap<String, Integer> indices = new HashMap<>();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(fileName);

			NodeList listNodes = doc.getElementsByTagName("indices");
			for (int i = 0; i < listNodes.getLength(); i++) {
				Element element = (Element) listNodes.item(i);
				indices.put(((Element) element.getParentNode()).getAttribute("name"), Integer.parseInt(element.getFirstChild().getTextContent().trim()));
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return indices;
	}

}
