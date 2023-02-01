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

import java.util.*;
import java.util.stream.Collectors;


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
    //private List<Concept> concepts;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ConceptRepository conceptRepository;
    //private FileUtils fu;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();
        this.documents = (List<Document>) executionContext.get("documents");
        //this.concepts = new ArrayList();
        //fu = new FileUtils("CV_SCORES.csv");
        logger.info("Documents Writer initialized.");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution,
                                ChunkContext chunkContext) throws Exception {
        /*for (Line line : lines) {
            fu.writeLine(line);
            logger.debug("Wrote line " + line.toString());
        }*/
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
                Set<ConceptOccurence> conceptOccurences = new HashSet<>();
                conceptOccurences.add(new ConceptOccurence(new ConceptOccurenceId(document.getName(), concept.getName()), document, concept, v));
                concept.setConceptOccurences(conceptOccurences);
                concepts.add(concept);
            });


            /*Concept concept1= new Concept().setName("développeur");
            Set<ConceptOccurence> conceptOccurences1 = new HashSet<>();
            conceptOccurences1.add(new ConceptOccurence(new ConceptOccurenceId(document.getName(),concept1.getName()),document,concept1, 1));
            //conceptOccurences1.add(new ConceptOccurence(concept1, 2));
            concept1.setConceptOccurences(conceptOccurences1);


            Concept concept2= new Concept().setName("java");
            Set<ConceptOccurence> conceptOccurences2 = new HashSet<>();
            conceptOccurences2.add(new ConceptOccurence(new ConceptOccurenceId(document.getName(),concept2.getName()),document,concept2, 2));
            //conceptOccurences2.add(new ConceptOccurence(concept2, 2));
            concept2.setConceptOccurences(conceptOccurences2);

            Set<Concept> concepts= new HashSet<>();
            concepts.add(concept1);
            concepts.add(concept2);*/
            conceptRepository.saveAll(concepts);
            ////document.setConcepts(concepts);
            //documentRepository.save(document);
        });

        //documentRepository.saveAll(documents);
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        //fu.closeWriter();
        logger.info("Lines Writer ended.");
        return ExitStatus.COMPLETED;
    }
}
