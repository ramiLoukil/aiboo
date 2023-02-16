package com.maltem.aiboo.config;

import com.maltem.aiboo.batch.listener.AibooJobListener;
import com.maltem.aiboo.batch.tasklets.DocumentsReaderTasklet;
import com.maltem.aiboo.batch.tasklets.DocumentsTokenizerTasklet;
import com.maltem.aiboo.batch.tasklets.DocumentsWriterTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@EnableCaching
public class AibooApplicationConfig {
    /*@Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    protected Step readLines(LinesReader linesReader) {
        return steps
                .get("readLines")
                .tasklet(linesReader)
                .build();
    }

    @Bean
    protected Step processLines(LinesProcessor linesProcessor) {
        return steps
                .get("processLines")
                .tasklet(linesProcessor)
                .build();
    }

    @Bean
    protected Step writeLines(LinesWriter linesWriter) {
        return steps
                .get("writeLines")
                .tasklet(linesWriter)
                .build();
    }

    @Bean
    public Job job(Step readLines,Step processLines,Step writeLines) {
        return jobs
                .get("taskletsJob")
                .start(readLines)
                .next(processLines)
                .next(writeLines)
                .build();
    }*/
    private AibooJobListener aibooJobListener;

    public AibooApplicationConfig(AibooJobListener aibooJobListener) {
        this.aibooJobListener = aibooJobListener;
    }

    @Bean
    public Tasklet documentsReader() {
        return new DocumentsReaderTasklet();
    }

    @Bean
    public Tasklet documentsTokenizer() {
        return new DocumentsTokenizerTasklet();
    }

    @Bean
    public Tasklet documentsWriter() {
        return new DocumentsWriterTasklet();
    }


    @Bean
    public Step readDocuments(JobRepository jobRepository, Tasklet documentsReader, PlatformTransactionManager transactionManager) {
        return new StepBuilder("readDocuments", jobRepository)
                .tasklet(documentsReader, transactionManager) // or .chunk(chunkSize, transactionManager)
                .build();
    }
    @Bean
    public Step tokenizeDocuments(JobRepository jobRepository, Tasklet documentsTokenizer, PlatformTransactionManager transactionManager) {
        return new StepBuilder("tokenizeDocuments", jobRepository)
                .tasklet(documentsTokenizer, transactionManager) // or .chunk(chunkSize, transactionManager)
                .build();
    }

    @Bean
    public Step writeDocuments(JobRepository jobRepository, Tasklet documentsWriter, PlatformTransactionManager transactionManager) {
        return new StepBuilder("writeDocuments", jobRepository)
                .tasklet(documentsWriter, transactionManager) // or .chunk(chunkSize, transactionManager)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step readDocuments,Step tokenizeDocuments,Step writeDocuments) {
        return new JobBuilder("aibooTaskletsJob", jobRepository)
                .listener(aibooJobListener)
                .start(readDocuments)
                .next(tokenizeDocuments)
                .next(writeDocuments)
                .build();
    }
}
