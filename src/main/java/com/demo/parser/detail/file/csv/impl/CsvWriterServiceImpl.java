package com.demo.parser.detail.file.csv.impl;

import static java.util.logging.Level.SEVERE;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

import com.demo.parser.domain.file.csv.CsvWriterService;
import com.demo.parser.domain.txt2word.model.Text;
import com.opencsv.CSVWriter;
import lombok.extern.java.Log;

/**
 * <p>
 * Implementation class for csv writer service. This class takes in data and
 * path value to write the csv file to.
 * </p>
 */
@Log
public class CsvWriterServiceImpl implements CsvWriterService {

    private static final String CSV_EXT = ".csv";

    @Override
    public void writeDataCsvToFile(Text data, Path path) {

        // Creating FileWriter object
        Path outputPath = path.getParent().resolve(path.getFileName() + CSV_EXT);
        log.info(() -> "Output path : " + outputPath);

        try {
            Writer fileWriter = new FileWriter(outputPath.toFile());
            CSVWriter writer = new CSVWriter(fileWriter);
            writer.writeAll(data.getSentenceListWithWordArray());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.log(SEVERE, "There was an error while performing operation on file " + outputPath, e);
            // Return error response DTO or throw exception
        }
    }
}
