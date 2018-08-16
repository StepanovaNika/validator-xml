import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ValidXML {
    int tr = 0;
    int fs = 0;
    String[][] reportTable = {{}};


    public static List<String> validateXMLByXSDAndGetErrors(File xml, File xsd) throws SAXException, IOException {
        List<String> exceptions = new ArrayList<>();
        try {
            Validator validator = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(xsd)
                    .newValidator();
            validator.setErrorHandler(new XSDValidatorErrorHandler(exceptions));
            validator.validate(new StreamSource(xml));
        } catch (Exception e) {
        }
        return exceptions;
    }
    public String parseXML(File xmlX) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(xmlX);
        Element root = doc.getDocumentElement();
        Element elem = (Element) root.getElementsByTagName("GUID").item(0);
        Element elem1 = (Element) root.getElementsByTagName("OrgCode").item(0);
        Element elem2 = (Element) root.getElementsByTagName("OrFKCode").item(0);

        String s0 = elem.getTextContent();
        String s1 = elem1.getTextContent();
        String s2 = elem2.getTextContent();


        //System.out.println(s + ";" + s1 + ";");
        //System.out.println(s0 + ";" + s1 + ";" + s2 + ";");
        return s0 + ";" + s1 + ";" + s2 + ";";
    }
    String validXmlforXsd(File xml, File xsd) throws IOException, SAXException, XPathExpressionException, ParserConfigurationException {
        List<String> list = ValidXML.validateXMLByXSDAndGetErrors(xml, xsd);
        String str = "";
        if (list.isEmpty()){
            str = parseXML(xml) + "true" + ";" + xml.getAbsolutePath();
            tr++;
        }else {
            str = parseXML(xml) + "false" + ";" + xml.getAbsolutePath() + ";" + String.join(";", list);
            fs++;
        }

       // System.out.println(str);
        return str;

    }


    public void checkForXml(File pathXsd, File... pathXml) {

        File fileReport = new File("report.txt");
        try {
        FileWriter writer = new FileWriter(fileReport);
        writer.write("GUID;OrgCode;OrFKCode;Result;Path file;Error\r\n");
        String s = "";
        if (pathXml[0].isDirectory()) {
            for (File file : pathXml[0].listFiles()) {
                s = validXmlforXsd(file, pathXsd);
                writer.write(s + "\r\n");
            }
        }else {
            for (int i = 0; i < pathXml.length; i++) {
                s = validXmlforXsd(pathXml[i], pathXsd);
                writer.write(s + "\r\n");
            }

        }
        writer.flush();
        writer.close();
        } catch (Exception e) {

        }


    }
}
