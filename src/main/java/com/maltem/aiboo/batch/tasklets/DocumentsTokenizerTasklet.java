package com.maltem.aiboo.batch.tasklets;

import com.maltem.aiboo.batch.model.Document;
import com.maltem.aiboo.utilities.OpenNlpUtility;
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
import org.springframework.stereotype.Component;

import java.util.List;

@JobScope
@Component
public class DocumentsTokenizerTasklet implements Tasklet,StepExecutionListener {

    private Logger logger = LoggerFactory.getLogger(
            DocumentsTokenizerTasklet.class);

    private List<Document> documents;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();
        this.documents = (List<Document>) executionContext.get("documents");
        logger.info("Documents Tokenizer initialized.");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution,
                                ChunkContext chunkContext) throws Exception {

        documents.forEach(document-> {
            document.setTokensWithOccurence(
                    OpenNlpUtility.extractTokensWithOccurence(
                        document.getContent(),
                        document.getLanguage()
                    )
            );
        });
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("Documents Tokenizer ended.");
        return ExitStatus.COMPLETED;
    }
}
