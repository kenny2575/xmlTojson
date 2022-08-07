import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static void read(Node node, List<Employee> employeeList) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                if (node_.getParentNode().getNodeName().equals("employee")){
                    NodeList nodeList_ = node.getChildNodes();
                    List<String> nodeString = new ArrayList<>();
                    for (int j = 0; j < nodeList_.getLength(); j++) {
                        if (Node.ELEMENT_NODE == nodeList_.item(j).getNodeType()) {
                            Element element = (Element) nodeList_.item(j);
                            nodeString.add(element.getTextContent());
                        }
                    }
                    employeeList.add(new Employee(Integer.parseInt(nodeString.get(0)), nodeString.get(1), nodeString.get(2), nodeString.get(3), Integer.parseInt(nodeString.get(4))));
                    return;
                }
            }
            read(node_, employeeList);
        }
    }


    static List<Employee> parseXML(String fileName ) {
        List<Employee> returningList = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            Node root = doc.getDocumentElement();
            read(root, returningList);
        } catch (ParserConfigurationException | IOException| SAXException e) {
            System.out.println(e);
        }
        return returningList;
    }

    static String listToJson(List<Employee> list){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    static void writeString(String json){
        try (FileWriter file = new
                FileWriter("new_data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<Employee> list = parseXML("data.xml");
        String json = listToJson(list);
        System.out.println(json);
        writeString(json);
    }
}