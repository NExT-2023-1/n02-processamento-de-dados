package com.teste.batch.batchTesteApp.springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    
    private final JdbcTemplate jdbcTemplate;

    //@Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus()== BatchStatus.COMPLETED){
            log.info("!!! JOB FINISHED! Time to verify the results");

            jdbcTemplate.query("SELECT * FROM paciente", (rs, row) -> new Paciente(
                rs.getLong("id"),
                rs.getString("nome"), 
                rs.getString("cpf"), 
                rs.getInt("idade"), 
                rs.getString("fone"), 
                rs.getString("email"))).forEach(paciente ->log.info("Found <{{}}> in the database.",paciente)); {
                
            }
        }
        
    }
}
