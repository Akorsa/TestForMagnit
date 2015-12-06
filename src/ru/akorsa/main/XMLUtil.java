package ru.akorsa.main;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;

import javax.xml.stream.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class XMLUtil {

    // method for create new XML file using StAX
    public void writeToXml(Path path, int[] data) throws IOException, XMLStreamException {
        try (OutputStream os = Files.newOutputStream(path)) {
            XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
            XMLStreamWriter writer = null;
            try {
                writer = outputFactory.createXMLStreamWriter(os, "utf-8");
                writer = new IndentingXMLStreamWriter(writer);
                writeElem(writer, data);
            } finally {
                if (writer != null)
                    writer.close();
            }
        }
    }

    private void writeElem(XMLStreamWriter writer, int[] data) throws XMLStreamException {
        writer.writeStartDocument("utf-8", "1.0");

        writer.writeStartElement("entries");
        for (int i = 0; i < data.length; i++)
            writeElem(writer, data[i]);
        writer.writeEndElement();

        writer.writeEndDocument();
    }

    private void writeElem(XMLStreamWriter writer, int field) throws XMLStreamException {
        writer.writeStartElement("entry");
        writer.writeStartElement("field");
        writer.writeCharacters(Integer.toString(field));
        writer.writeEndElement();
        writer.writeEndElement();
    }

    //method for transforming XML with XSLT
    public void transformXML(String path) {
        try {
            TransformerFactory tf =
                    TransformerFactory.newInstance();

            //установка используемого XSL-преобразования
            Transformer transformer =
                    tf.newTransformer(new StreamSource(path));

            //установка исходного XML-документа и конечного XML-файла
            transformer.transform(
                    new StreamSource("1.xml"),
                    new StreamResult("2.xml"));
            System.out.println("Transform complete");
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    //method for parsing XML using StAX
    public List<Integer> parseXML(String fileName) {
        List<Integer> empList = new ArrayList<>();

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(fileName));
            int event = xmlStreamReader.getEventType();
            while(true){
                switch(event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if(xmlStreamReader.getLocalName().equals("entry")){
                            empList.add(Integer.parseInt(xmlStreamReader.getAttributeValue(0)));
                        }
                    case XMLStreamConstants.CHARACTERS:
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        break;
                }
                if (!xmlStreamReader.hasNext())
                    break;
                event = xmlStreamReader.next();
            }
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
        return empList;
    }


}
