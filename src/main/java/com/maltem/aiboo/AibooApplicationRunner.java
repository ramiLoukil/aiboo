package com.maltem.aiboo;


import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@EnableBatchProcessing
//@Profile("PROD")
public class AibooApplicationRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AibooApplicationRunner.class);

    @Argument(alias = "type", description = "type", required = true)
    private static String type;

    private JobLauncher jobLauncher;

    private Job aibooImportJob;


    public AibooApplicationRunner(JobLauncher jobLauncher, Job aibooImportJob) {
        this.jobLauncher = jobLauncher;
        this.aibooImportJob = aibooImportJob;
    }

    @Override
    public void run(String[] args) throws Exception {
        JobParameters jobParameters = getJobParameters(args);
        jobLauncher.run(aibooImportJob, jobParameters);
        LOGGER.info("Done");
    }


    private static JobParameters getJobParameters(String[] args){
        Args.parseOrExit(AibooApplicationRunner.class, args);
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addJobParameter("type", new JobParameter(type, String.class));
        paramsBuilder.addJobParameter("startTime", new JobParameter(System.nanoTime(), Long.class));
        return paramsBuilder.toJobParameters();
    }
}
