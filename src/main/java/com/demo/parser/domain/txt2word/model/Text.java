package com.demo.parser.domain.txt2word.model;

import static java.util.stream.Collectors.toList;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.opencsv.bean.CsvBindAndJoinByName;
import lombok.Data;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Text {

    @XmlElement
    @CsvBindAndJoinByName(elementType = List.class)
    private List<Sentence> sentence;

    public List<String[]> getSentenceListWithWordArray() {
        return this.sentence.stream().map(Sentence::getWordAsArray).collect(toList());
    }
}
