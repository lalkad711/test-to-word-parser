package com.demo.parser.detail.parser.impl;

import static java.nio.file.StandardOpenOption.READ;
import static java.util.logging.Level.SEVERE;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.demo.parser.domain.file.csv.CsvWriterService;
import com.demo.parser.domain.file.xml.XmlWriterService;
import com.demo.parser.domain.parser.ParserService;
import com.demo.parser.domain.txt2word.TextToSentenceConverterService;
import com.demo.parser.domain.txt2word.model.Sentence;
import com.demo.parser.domain.txt2word.model.Text;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

/**
 * <p>
 * Main parser service class containing the core orchestration logic for the
 * application.
 * </p>
 * <p>
 * Has all injectible transitive dependency components. Allowing the flexibility
 * to change the implementations of any of those underlying components.
 * </p>
 */

// TODO: Check if some stuff can be parallelized using virtual threads - writes csv and xml in parallel
@Log
@RequiredArgsConstructor
public class ParserServiceImpl implements ParserService {

    private static final int MAX_BUFFER_SIZE = 2000;

    private final CsvWriterService csvWriterService;
    private final XmlWriterService xmlWriterService;
    private final TextToSentenceConverterService textToSentenceConverterService;

    @Override
    public void parseAndCreateFiles(Path filePath) {
        log.info(() -> "Starting parsing of the received file : " + filePath.getFileName());
        List<Sentence> sentences = null;
        try (SeekableByteChannel ch = Files.newByteChannel(filePath, READ)) {
            //        	System.out.println(ch.size());
            //        	long noOfChunks = ch.size()/(16 * 1024);
            // TODO: Create buffer size based on file size and jvm param -Xmx32m
            // to check the effect of flip
            // Setting 16 MB as buffer size
            ByteBuffer bf = ByteBuffer.allocate(MAX_BUFFER_SIZE);
            while (ch.read(bf) > 0) {
                // TODO: check the flip functionality when buffer size is small compared to file
                // size. Seems to repeat the data again from beginning.
                bf.flip();

                // TODO: Should return a DTO
                sentences = textToSentenceConverterService.convertTextToSentence(new String(bf.array()));

                // TODO: Use mapstruct mapper to send in data as DTO contracts to the writer
                // services instead of below.
                Text data = new Text();
                data.setSentence(sentences);

                // Creates a csv file in same directory as source file with similar name.
                csvWriterService.writeDataCsvToFile(data, filePath);

                // Creates a xml file in same directory as source file with similar name.
                xmlWriterService.writeDataXmlToFile(data, filePath);
                bf.clear();

                // Can return success response DTO indicating both files created successfully
                log.info("Done parsing of the received file successfully :" + filePath.getFileName());
            }
        } catch (IOException e) {
            log.log(SEVERE, "Something went wrong while reading file : " + filePath.getFileName(), e);
            // Return error response DTO or throw exception
        }
    }

    /**
     * To split the file into chunks based on it's size.
     * @param size
     * @return
     */
    //    private static long getNumberOfChunks(long size) {
    //    	long modChunks = size % MAX_BUFFER_SIZE;
    //    	long chunks = modChunks/MAX_BUFFER_SIZE;
    //    	if(modChunks == 0) {
    //    		return chunks;
    //    	} else {
    //    		return ++chunks;
    //    	}
    //    }
}
