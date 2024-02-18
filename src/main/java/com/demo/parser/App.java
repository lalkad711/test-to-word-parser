package com.demo.parser;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import com.demo.parser.detail.file.csv.impl.CsvWriterServiceImpl;
import com.demo.parser.detail.file.xml.impl.XmlWriterServiceImpl;
import com.demo.parser.detail.parser.impl.ParserServiceImpl;
import com.demo.parser.detail.text2word.impl.TextToSentenceConverterServiceImpl;
import com.demo.parser.domain.parser.ParserService;

/**
 * <p>
 * This application reads the text data from a file and tries to split it into
 * sentences. Further splitting them into words. Eventually, writing this data
 * into a xml and a csv file.
 * </p>
 * <p>
 * When the input file is very large then it tries to split it into smaller
 * chunks and performs the above operation.
 * </p>
 */
public class App {

    public static void main(String[] args) throws IOException {

        // Trivially creating the context
        ParserService parserService = new ParserServiceImpl(
                new CsvWriterServiceImpl(), new XmlWriterServiceImpl(), new TextToSentenceConverterServiceImpl());

        // Get the full file path i.e. absolute path as input from the user
        try (Scanner userInputScanner = new Scanner(System.in); ) {
            System.out.println("Please provide an full or absolute file path to be parsed : ");
            String filePath = userInputScanner.nextLine().trim();
            do {
                // System.out.println(filePath);

                if (isNotValidateUserInput(filePath)) {
                    System.out.println("Please provide a valid file path to be parsed or press X to exit");
                    filePath = userInputScanner.nextLine().trim();
                } else {
                    // Parse the text in input file and create xml and csv out of it.
                    parserService.parseAndCreateFiles(Paths.get(filePath));
                    break;
                }

                // Exit when the user hits 'x' or 'X'
                if (isExit(filePath)) {
                    break;
                }

            } while (true);
            System.out.println("Exited successfully. Thanks");
        }
    }

    /**
     * <p>
     * Validate if the input path is not empty and is readable i.e. accessible for
     * this application.
     * </p>
     *
     * @param inputPath User supplied path.
     * @return Boolean indicator indicating if the supplied path is invalid.
     */
    private static boolean isNotValidateUserInput(String inputPath) {
        return isBlank(inputPath) || !isValidPath(inputPath);
    }

    private static boolean isValidPath(String filePath) {
        return Files.isReadable(Paths.get(filePath));
    }

    private static boolean isExit(String filePath) {
        return equalsIgnoreCase(filePath, "x");
    }
}
