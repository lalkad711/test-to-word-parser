package com.demo.parser.domain.txt2word.model;

import java.util.SortedSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.opencsv.bean.CsvBindAndJoinByName;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Sentence {

    @XmlElement
    @CsvBindAndJoinByName(elementType = SortedSet.class)
    private SortedSet<Word> word;

    public String[] getWordAsArray() {
        return word.stream().map(Word::getWord).toArray(String[]::new);
    }
}
