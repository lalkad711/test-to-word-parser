package com.demo.parser.domain.txt2word;

import java.util.List;

import com.demo.parser.domain.txt2word.model.Sentence;

/**
 * <p>
 * Service contract for splitting the supplied string data into logical and
 * meaningful sentences.
 * </p>
 */
public interface TextToSentenceConverterService {
    List<Sentence> convertTextToSentence(String inputData);
}
