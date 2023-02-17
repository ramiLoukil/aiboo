package com.maltem.aiboo.batch.tasklets;

import com.maltem.aiboo.batch.model.Concept;
import com.maltem.aiboo.batch.model.ConceptOccurence;
import com.maltem.aiboo.batch.model.ConceptOccurenceId;
import com.maltem.aiboo.batch.model.Document;
import com.maltem.aiboo.batch.repository.ConceptRepository;
import com.maltem.aiboo.batch.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.maltem.aiboo.utilities.BabelNetUtility;


@JobScope
@Component
public class DocumentsWriterTasklet implements Tasklet, StepExecutionListener {

    private final Logger logger = LoggerFactory
            .getLogger(DocumentsWriterTasklet.class);

    private List<Document> documents;
    @Value("${aiboo.numberOfConcepts}")
    private int numberOfConcepts;
    @Value("${aiboo.numberMinOfOccurence}")
    private int numberMinOfOccurence;

    @Value("${aiboo.domain}")
    private String domain;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ConceptRepository conceptRepository;

    @Autowired
    private BabelNetUtility babelNetUtility;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();
        this.documents = (List<Document>) executionContext.get("documents");
        //this.babelNetUtility=new BabelNetUtility();
        //this.concepts = new ArrayList();
        //fu = new FileUtils("CV_SCORES.csv");
        logger.info("Documents Writer initialized.");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution,
                                ChunkContext chunkContext) throws Exception {

        documents.forEach(document-> {
            documentRepository.save(document);

            Map<String, Long> sortedAndFiltredTokensWithOccurence = document.getTokensWithOccurence().entrySet()
                    .stream()
                    .filter(k -> k.getValue() > numberMinOfOccurence)
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(numberOfConcepts)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            Set<Concept> concepts= new HashSet<>();

            sortedAndFiltredTokensWithOccurence.forEach((k,v)->{
                Concept concept= new Concept().setName(k);
                try {
                    String babelsetId = babelNetUtility.extractFirstSynsetIdFromWordByDomainAndLang(k, domain, document.getLanguage());
                    concept.setBabelSynsetId(babelsetId) ;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Set<ConceptOccurence> conceptOccurences = new HashSet<>();
                conceptOccurences.add(new ConceptOccurence(new ConceptOccurenceId(document.getName(), concept.getName()), document, concept, v));
                concept.setConceptOccurences(conceptOccurences);
                concepts.add(concept);
            });
            conceptRepository.saveAll(concepts);
        });

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("Documents Writer ended.");
        return ExitStatus.COMPLETED;
    }
}
