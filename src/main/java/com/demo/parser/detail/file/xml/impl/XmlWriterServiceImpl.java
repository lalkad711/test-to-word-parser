package com.demo.parser.detail.file.xml.impl;

import static java.util.logging.Level.SEVERE;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.demo.parser.domain.file.xml.XmlWriterService;
import com.demo.parser.domain.txt2word.model.Text;
import lombok.extern.java.Log;

/**
 * <p>
 * Implementation class for xml writer service. This class takes in data and
 * path value to write the xml file to.
 * </p>
 */
@Log
public class XmlWriterServiceImpl implements XmlWriterService {

    private static final String XML_EXT = ".xml";

    /**
     * <p>
     * XML writer. Uses JAXB lib and {@link XMLStreamWriter} to write xml file to the provided path.
     * </p>
     */
    @Override
    public void writeDataXmlToFile(Text text, Path path) {
        log.info("Starting to convert and write the Xml.");

        try {
            // Creating FileWriter object
            Path outputPath = path.getParent().resolve(path.getFileName() + XML_EXT);
            log.info(() -> "Output path : " + outputPath);
            Writer fileWriter = new FileWriter(outputPath.toFile());

            // Getting the XMLOutputFactory instance
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();

            // Creating XMLStreamWriter object from
            // xmlOutputFactory.
            XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(fileWriter);

            Marshaller marshaller = JAXBContext.newInstance(Text.class).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(text, xmlStreamWriter);

            xmlStreamWriter.flush();
            xmlStreamWriter.close();

        } catch (JAXBException e) {
            log.log(SEVERE, "Some error happened dusing marshalling : ", e);
        } catch (IOException e) {
            log.log(SEVERE, "Some error happened writing xml or opening the file : ", e);
        } catch (XMLStreamException e) {
            log.log(SEVERE, "Some error happened while writing xml - XMLStreamWriter : ", e);
        }
    }
}
