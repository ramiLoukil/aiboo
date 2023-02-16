package com.maltem.aiboo.utilities;

import it.uniroma1.lcl.babelnet.*;

import it.uniroma1.lcl.babelnet.data.BabelDomain;
import it.uniroma1.lcl.jlt.util.Language;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BabelNetUtility {

    private static BabelNet bn = BabelNet.getInstance();

    @Cacheable(cacheNames="BabelSynsetCache", key="#word+#domain+#lang", unless = "#result == null")
    public String extractFirstSynsetIdFromWordByDomainAndLang(String word, String domain, String lang) throws IOException {
        List<BabelSynset> synSets = bn.getSynsets(word, Language.valueOf(lang.toUpperCase()));
        Optional<BabelSynset> firtBabelSet = /*Optional.ofNullable(*/synSets.stream()
                .filter(sy -> sy.getDomains().containsKey(BabelDomain.valueOf(domain)))
                .findFirst();
        if (firtBabelSet.isPresent())
            return firtBabelSet.get().getId().toString();
        return "";
    }
}
