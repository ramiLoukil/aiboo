package com.maltem.aiboo.utilities;

import opennlp.tools.tokenize.SimpleTokenizer;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


public class OpenNlpUtility {

    static private List<String> stopwords;

    private static void extractStopWords(String language) throws IOException {
        //stopwords = Files.readAllLines(Paths.get("classpath:stop_words_french.txt"));
        //stopwords = Files.readAllLines(Paths.get(getClass().getResource("stop_words_french.txt").toURI()));
        //File stopWordsFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "stop_words_french.txt");
        InputStream inputStream = OpenNlpUtility.class.getClassLoader().getResourceAsStream("stop_words_"+language+".txt");
        ///File stopWordsFile=new File(is);
        ///stopwords = Files.readAllLines(Paths.get(stopWordsFile.toURI()));

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        stopwords = reader.lines().collect(Collectors.toList());
    }

    public static List<String> extractTokens(String text, String language) {
        try {
            extractStopWords(language);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer
                .tokenize(text);

        List<String> tokensWithoutStopWords = new LinkedList<>(Arrays.asList(tokens));
        tokensWithoutStopWords.removeAll(stopwords);
        return tokensWithoutStopWords
                .stream()
                .map(t->t.toLowerCase())
                .collect(toList());
    }

    public static Map<String,Long> extractTokensWithOccurence(String text, String language) {
        try {
            extractStopWords(language);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer
                .tokenize(text);

        List<String> tokensWithoutStopWords = new LinkedList<>(Arrays.asList(tokens));
        tokensWithoutStopWords = tokensWithoutStopWords.stream()
                                .map(t->t.toLowerCase())
                                .collect(Collectors.toList());
        tokensWithoutStopWords.removeAll(stopwords);
        //remove specific words from tokens list
        tokensWithoutStopWords.removeAll(Collections.singleton("."));
        tokensWithoutStopWords.removeAll(Collections.singleton("·"));
        tokensWithoutStopWords.removeAll(Collections.singleton(":"));
        tokensWithoutStopWords.removeAll(Collections.singleton(","));
        tokensWithoutStopWords.removeAll(Collections.singleton("'"));
        tokensWithoutStopWords.removeAll(Collections.singleton("’"));

        Map<String, Long> tokensWithOccurence = tokensWithoutStopWords
                .stream()
                .map(t->t.toLowerCase())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return tokensWithOccurence;
    }


}

