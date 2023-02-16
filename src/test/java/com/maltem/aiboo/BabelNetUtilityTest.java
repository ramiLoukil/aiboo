package com.maltem.aiboo;

import com.maltem.aiboo.utilities.BabelNetUtility;
import it.uniroma1.lcl.babelnet.BabelSynset;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class BabelNetUtilityTest {
    @Test
    public void given_word_and_lang_when_conceptualize_then_synsets_are_detected() throws IOException {
        String firstSynsetID= new BabelNetUtility().extractFirstSynsetIdFromWordByDomainAndLang("java","COMPUTING", "en");
        //String firstSynsetID=babelSynset.get().getId().toString();
        System.out.println(firstSynsetID);
        assertThat(firstSynsetID).isNotNull();
    }
}
