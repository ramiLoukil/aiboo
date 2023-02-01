package com.maltem.aiboo;

import com.maltem.aiboo.utilities.OpenNlpUtility;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenNlpUtilityTest {

    @Test
    public void given_English_SimpleTokenizer_whenTokenize_thenTokensAreDetected()
            throws Exception {

        List<String> tokens = OpenNlpUtility.extractTokens("Bob is a senior JAVA developper.","en");

        assertThat(tokens)
                .contains("bob", "senior", "java", "developper", ".");
    }

    @Test
    public void given_French_SimpleTokenizer_whenTokenize_thenTokensAreDetected()
            throws Exception {

        List<String> tokens = OpenNlpUtility.extractTokens("Bob est un Développeur Java."+
                "compétences: l'Organization des Sprint planning et le développement des US","fr");

        assertThat(tokens)
                .contains("bob", "développeur", "java", "compétences", "organization"
                        , "sprint", "planning", "développement", "us", ".", ":");
    }

    @Test
    public void given_English_SimpleTokenizer_whenTokenize_thenTokensWithOccurenceAreDetected()
            throws Exception {

        Map<String,Long> tokensWithOccurence = OpenNlpUtility.extractTokensWithOccurence("Bob is a senior JAVA " +
                "developper. He has Java " +
                "as a good competence."+
                "required competences: java, maven and git are requested"+
                "nice to have competences: Kafka, Mongodb."+
                "mission 's most required competence: java","en");

        assertThat(tokensWithOccurence.get("java"))
                .isEqualTo(4);

        assertThat(tokensWithOccurence.get("'"))
                .isEqualTo(1);

        assertThat(tokensWithOccurence.get("."))
                .isEqualTo(3);

        assertThat(tokensWithOccurence.get(":"))
                .isEqualTo(3);

        assertThat(tokensWithOccurence.get(","))
                .isEqualTo(2);

    }
}
