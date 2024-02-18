package com.demo.parser.domain.txt2word.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Word implements Comparable<Word> {

    @XmlElement
    @CsvBindByName
    private String word;

    @Override
    public int compareTo(Word o) {
        return this.word.toLowerCase().compareTo(o.word.toLowerCase());
    }
}
