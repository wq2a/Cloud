package cloud.server;
import java.util.*;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import javax.xml.transform.OutputKeys;

import cloud.server.Path;

class PathManager{
	private ArrayList<Path> ps = new ArrayList<Path>();
	PathManager(){

	}

	public void add(Path p){
		ps.add(p);
	}

	public ArrayList<Path> getList(){
		return ps;
	}

	public ArrayList<Path> getSortedList(){
		Collections.sort(ps, new Path());
		return ps;
	}

	public String generateXML(){
		Collections.sort(ps, new Path());
		int level = -1;
		String xml = "";
		Element element,subElement;
		Attr attr;
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();

			int index = 0;
			for(Path p : ps){

				if(index == 0){
					element = doc.createElement("id"+p.getParent());
					doc.appendChild(element);
				}

				subElement = doc.createElement("id"+p.getID());
				
				element = (Element)((doc.getElementsByTagName("id"+p.getParent())).item(0));
				
				attr = doc.createAttribute("name");
				attr.setValue(p.getName());
				subElement.setAttributeNode(attr);
				attr = doc.createAttribute("parent");
				attr.setValue(Integer.toString(p.getParent()));
				subElement.setAttributeNode(attr);
				attr = doc.createAttribute("depth");
				attr.setValue(Integer.toString(p.getDepth()));
				subElement.setAttributeNode(attr);
				attr = doc.createAttribute("type");
				attr.setValue(Integer.toString(p.getType()));
				subElement.setAttributeNode(attr);

				element.appendChild(subElement);
				index++;
			}
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			xml = writer.getBuffer().toString();//.replaceAll("\n|\r", "");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return xml;
	}
}