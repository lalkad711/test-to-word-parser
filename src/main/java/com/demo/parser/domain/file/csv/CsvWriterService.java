package com.demo.parser.domain.file.csv;

import java.nio.file.Path;

import com.demo.parser.domain.txt2word.model.Text;

public interface CsvWriterService {
    void writeDataCsvToFile(Text data, Path path);
}
