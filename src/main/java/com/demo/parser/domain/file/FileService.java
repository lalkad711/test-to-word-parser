package com.demo.parser.domain.file;

public interface FileService {

    void readFile(String filePath);

    void writeCsvFile(String data, String filePath);

    void writeXmlFile(String data, String filePath);
}
