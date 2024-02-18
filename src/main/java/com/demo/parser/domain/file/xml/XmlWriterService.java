package com.demo.parser.domain.file.xml;

import java.nio.file.Path;

import com.demo.parser.domain.txt2word.model.Text;

public interface XmlWriterService {
    void writeDataXmlToFile(Text text, Path path);
}
