package com.teste.batch.batchTesteApp.springbatch;

import java.io.File;
import java.io.IOException;

import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;



@RestController
@RequestMapping("api/v1/upload")
@RequiredArgsConstructor
public class JobController {
    private final JobLauncher jobLauncher;
    private final Job job;
    @Value("${temp.upload.path}")
    private String tempUploadPath;
    
    @PostMapping(path = "/upload")
    public void startBatch(@RequestParam MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            File fileToImport = new File(tempUploadPath + originalFileName);
            file.transferTo(fileToImport);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("fullPathFileName", tempUploadPath + originalFileName)
                    .addLong("startAt", System.currentTimeMillis()).toJobParameters();

            jobLauncher.run(job,jobParameters);
           // if(execution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED)){
                //delete the file from the temporary upload path
           //     Files.deleteIfExists(Paths.get(tempUploadPath + originalFileName));
          //  }
            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
            JobParametersInvalidException | IOException e) {
        e.printStackTrace(); 
    } 
    } 
    }
