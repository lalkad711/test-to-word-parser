package com.demo.parser.detail.text2word.impl;

import static java.lang.Character.isLetter;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import com.demo.parser.domain.txt2word.TextToSentenceConverterService;
import com.demo.parser.domain.txt2word.model.Sentence;
import com.demo.parser.domain.txt2word.model.Word;

/**
 * <p>
 * Implementation detail for {@link TextToSentenceConverterService}. This class
 * makes use of the {@link BreakIterator} from the {@linkplain java.text}
 * package. The BreakIterator class implements methods for finding the location
 * of boundaries in text.
 * </p>
 * <p>
 * This class first splits the input text data into sentences and then further
 * into words. The words in each sentence are then ordered in the ascending
 * order.
 * </p>
 */
public class TextToSentenceConverterServiceImpl implements TextToSentenceConverterService {

    /**
     * <p>
     * Titles to check when splitting the sentences between two boundaries.
     * </p>
     */
    private static final List<String> TITLES = List.of("Dr.", "Prof.", "Mr.", "Mrs.", "Ms.", "Jr.", "Ph.D.");

    // TODO: Make this method expose the ordering option
    @Override
    public List<Sentence> convertTextToSentence(String inputData) {
        return breakTextIntoSentence(inputData, true);
    }

    /**
     *
     * @param source        Source text data.
     * @param sentenceOrder Ordering for sentences. True indicating forward or the
     *                      order of occurrence and False indicating otherwise
     *                      respectively. Default true.
     * @return List of {@link Sentence}
     */
    private List<Sentence> breakTextIntoSentence(String source, boolean sentenceOrder) {
        BreakIterator bi = BreakIterator.getSentenceInstance(Locale.US);
        bi.setText(source);
        if (sentenceOrder) {
            return getEachSentenceInForwardDirection(bi, source);
        } else {
            return getEachSentenceInBackwardDirection(bi, source);
        }
    }

    /**
     * <p>
     * Looks at the input text data splits it into sentences in the order of
     * occurrence and then further uses {@link #getEachWord(BreakIterator, String)}
     * to get the words in ordered sequence.
     * </p>
     *
     * @param sentenceBoundry The {@link BreakIterator#getSentenceInstance(Locale)}
     *                        instance.
     * @param source          Source text data.
     * @return List of {@link Sentence}
     */
    private List<Sentence> getEachSentenceInForwardDirection(BreakIterator sentenceBoundry, String source) {
        List<Sentence> sentences = new ArrayList<>();
        int start = sentenceBoundry.first();
        int tempStart = start;
        for (int end = sentenceBoundry.next(); end != BreakIterator.DONE; start = end, end = sentenceBoundry.next()) {
            BreakIterator wordBoundry = BreakIterator.getWordInstance(Locale.US);
            String stringToExamine = source.substring(start, end);
            if (!hasTitles(stringToExamine)) {
                stringToExamine = source.substring(tempStart, end);
                tempStart = end;
                wordBoundry.setText(stringToExamine);
                Sentence sentence = new Sentence();
                sentence.setWord(getEachWord(wordBoundry, stringToExamine));
                sentences.add(sentence);
            }
        }
        return sentences;
    }

    /**
     * <p>
     * Looks at the input text data splits it into sentences in the last order of
     * occurrence and then further uses {@link #getEachWord(BreakIterator, String)}
     * to get the words in ordered sequence.
     * </p>
     *
     * @param sentenceBoundry The {@link BreakIterator#getSentenceInstance(Locale)}
     *                        instance.
     * @param source          Source text data.
     * @return List of {@link Sentence}
     */
    private List<Sentence> getEachSentenceInBackwardDirection(BreakIterator sentBoundary, String source) {
        List<Sentence> sentences = new ArrayList<>();
        int end = sentBoundary.last();
        for (int start = sentBoundary.previous();
                start != BreakIterator.DONE;
                end = start, start = sentBoundary.previous()) {
            BreakIterator wordBoundry = BreakIterator.getWordInstance(Locale.US);
            String stringToExamine = source.substring(start, end);
            wordBoundry.setText(stringToExamine);
            Sentence sentence = new Sentence();
            sentence.setWord(getEachWord(wordBoundry, stringToExamine));
            sentences.add(sentence);
        }
        return sentences;
    }

    /**
     * <p>
     * Takes in the {@link BreakIterator#getWordInstance(Locale)} instance and
     * splits the source sentence into the words and returns a sorted set.
     * </p>
     *
     * @param wordBoundry    The {@link BreakIterator#getWordInstance(Locale)}
     *                       instance.
     * @param sourceSentence Source text data.
     * @return Set of {@link Word} sorted in the ascending order.
     */
    private SortedSet<Word> getEachWord(BreakIterator wordBoundry, String sourceSentence) {
        SortedSet<Word> words = new TreeSet<>();
        int start = wordBoundry.first();
        for (int end = nextWordStartAfter(wordBoundry, sourceSentence);
                end != BreakIterator.DONE;
                start = end, end = wordBoundry.next()) {
            String word = sourceSentence.substring(start, end).trim();
            if (isAllowed(word)) {
                words.add(new Word(word));
            }
        }
        return words;
    }

    /**
     * <p>
     * This method checks for the word boundries in special cases like e.g. "Mr.".
     * Here the dot is not really and end of sentence but it's a part of the word.
     * So, this utility method helps in fetching the actual boundries when splitting
     * a sentence into words.
     * </p>
     *
     * @param wordBoundry The {@link BreakIterator#getWordInstance(Locale)}
     *                    instance.
     * @param sentence    Source sentence in which the words are being checked.
     * @return int position of the boundry.
     */
    private int nextWordStartAfter(BreakIterator wordBoundry, String sentence) {
        int last = wordBoundry.following(wordBoundry.current());
        int current = wordBoundry.next();
        while (current != BreakIterator.DONE) {
            for (int p = last; p < current; p++) {
                if (isLetter(sentence.codePointAt(p))) return last;
            }
            last = current;
            current = wordBoundry.next();
        }
        return BreakIterator.DONE;
    }

    /**
     * <p>
     * Utility method checking if the word is a special character. i.e. amongst "",
     * ".", ",", ")", "(", ":", "!", "?", "-".
     * </p>
     *
     * @param value
     * @return
     */
    private boolean isAllowed(String value) {
        // TODO: Check preview not working on IDE
        // return switch (value) {
        // case null, "", ".", ",", ")", "(", ":" -> false;
        // default -> true;
        // };

        switch (value) {
            case "", ".", ",", ")", "(", ":", "!", "?", "-":
                return false;
            default:
                return true;
        }
    }

    private static boolean hasTitles(String sentence) {
        if (isBlank(sentence)) {
            return false;
        }
        return TITLES.stream().anyMatch(abbr -> sentence.contains(abbr));
    }
}
