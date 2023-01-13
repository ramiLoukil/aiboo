package com.maltem.aiboo.batch.listener;

import com.maltem.aiboo.batch.tasklets.DocumentsReaderTasklet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class AibooJobListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AibooJobListener.class);
    @Value("${aiboo.input}")
    private String input;
    @Value("${aiboo.archive}")
    private String archive;
    @Value("${aiboo.error}")
    private String error;

    public AibooJobListener() {
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // Do nothing
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        ExitStatus exitStatus = jobExecution.getExitStatus();
        boolean moveSuccess;
        if (ExitStatus.FAILED.equals(exitStatus)) {
            moveSuccess = moveInputFiles(true);
        } else {
            moveSuccess = moveInputFiles(false);
        }
        /*if (!moveSuccess) {
            processManager.changeExitCode(exitCodeProperties.getError());
        }*/
    }

    private boolean moveInputFiles(boolean globalFailure) {

        List<Resource> resources = getResources();
        String archiveDirectory = archive + File.separator+ getDocumentType();
        String failureDirectory = error + File.separator+ getDocumentType();
        new File(archiveDirectory).mkdir();
        new File(failureDirectory).mkdir();

        boolean allMoveSuccess = true;
        for (Resource resource : resources) {
            try {
                File file = resource.getFile();
                String fileName = file.getName();
                boolean moveSuccess;
                if (globalFailure) {
                    moveSuccess = moveFileTo(file, failureDirectory);
                } else {
                    moveSuccess = moveFileTo(file, archiveDirectory);
                }
                allMoveSuccess = allMoveSuccess && moveSuccess;
            } catch (IOException e) {
                LOGGER.error("Cannot move file" + resource.getFilename(), e);
                allMoveSuccess = false;
            }
        }
        return allMoveSuccess;
    }

    private static boolean moveFileTo(File file, String directory) {
        String fileName = file.getName();
        boolean success = file.renameTo(new File(directory + File.separator + fileName));
        if (!success) {
            LOGGER.error("Cannot move file {} to {}", fileName, directory);
        }
        return success;
    }

    private List<Resource> getResources() {
        return DocumentsReaderTasklet.getFilesRessources();
    }
    private String getDocumentType(){
        return DocumentsReaderTasklet.getDocumentType();
    }
}
