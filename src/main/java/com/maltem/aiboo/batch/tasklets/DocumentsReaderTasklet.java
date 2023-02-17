package com.maltem.aiboo.batch.tasklets;

import com.maltem.aiboo.batch.model.Document;
import com.maltem.aiboo.utilities.TikaUtility;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import static org.apache.logging.slf4j.SLF4JLoggerContextFactory.LOGGER;

@JobScope
@Component
public class DocumentsReaderTasklet implements Tasklet, StepExecutionListener {
    private final Logger logger = LoggerFactory
            .getLogger(DocumentsReaderTasklet.class);
    private static final String TYPE = "type";
    private static String documentType;
    @Value("${aiboo.input}")
    private String input;
    private static List<Resource> fileResources = new ArrayList<>();
    private List<Document> documents;
    private Document document;

    private TikaUtility tikaUtility;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        documents= new ArrayList<>();
        tikaUtility= new TikaUtility();
        logger.info("Documents Reader initialized.");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution,
                                ChunkContext chunkContext) throws IOException,Exception {

        documentType=getType(chunkContext);
        //Recuperate the list of Documents of specified type (AO or CV or Other)
        try (Stream<Path> files = Files.list(Paths.get(input+ File.separator+ documentType))) {
            fileResources = files
                    .map(FileSystemResource::new)
                    .filter(FileSystemResource::isFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        fileResources.forEach(file -> {
            document =new Document();
            document.setName(file.getFilename());
            document.setType(documentType);
            document.setExtractionDate(LocalDate.now());
            document.setValid(true);
            try {
                document.setContent(tikaUtility.extractContentUsingFacade(file.getInputStream()));
                document.setLanguage(tikaUtility.extractContentLanguage(file.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TikaException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
            documents.add(document);
        });

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        stepExecution
                .getJobExecution()
                .getExecutionContext()
                .put("documents", this.documents);
        logger.info("Documents Reader ended.");
        return ExitStatus.COMPLETED;
    }

    private static Map<String, Object> getJobParameters(ChunkContext chunkContext) {
        return chunkContext.getStepContext().getJobParameters();
    }

    private static boolean isNotAString(Object o) {
        return !(o instanceof String);
    }

    private static String getType(ChunkContext chunkContext) {
        Map<String, Object> jobParameters = getJobParameters(chunkContext);

        Object type = jobParameters.get(TYPE);
        if (type == null) throw new IllegalStateException(TYPE + " parameter must be set to execute this job");
        if (isNotAString(type)) throw new IllegalArgumentException(TYPE + " parameter must be a String");

        return (String) type;
    }
    public static List<Resource> getFilesRessources(){
        return fileResources;
    }

    public static String getDocumentType(){
        return documentType;
    }
}
