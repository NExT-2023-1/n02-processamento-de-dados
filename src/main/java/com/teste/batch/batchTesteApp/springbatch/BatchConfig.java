package com.teste.batch.batchTesteApp.springbatch;
import java.io.File;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    
@Bean
@StepScope
public FlatFileItemReader<Paciente> itemReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFIle) {
    FlatFileItemReader<Paciente> flatFileItemReader = new FlatFileItemReader<>();
    flatFileItemReader.setResource(new FileSystemResource(new File(pathToFIle))); 
    flatFileItemReader.setName("CSV-Reader");
    flatFileItemReader.setLinesToSkip(1);
    flatFileItemReader.setLineMapper(lineMapper());
    return flatFileItemReader;
}
private LineMapper<Paciente> lineMapper(){ 
    DefaultLineMapper<Paciente> lineMapper = new DefaultLineMapper<>();

    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
    lineTokenizer.setDelimiter(",");
    lineTokenizer.setStrict(false);
    lineTokenizer.setNames( "id","nome","cpf","idade","fone","email");


    BeanWrapperFieldSetMapper<Paciente> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(Paciente.class);
    lineMapper.setLineTokenizer(lineTokenizer);
    lineMapper.setFieldSetMapper(fieldSetMapper);

    return lineMapper;
    }




    @Bean
    public PacienteItemProcessor processor() {
        return new PacienteItemProcessor();
        
    }

    @Bean
    public JdbcBatchItemWriter<Paciente> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Paciente>()
        .itemSqlParameterSourceProvider( new BeanPropertyItemSqlParameterSourceProvider<>() )
        .sql( "INSERT INTO paciente (id, nome, cpf, idade, fone, email) VALUES (:id, :nome, :cpf, :idade, :fone, :email)" )
        .dataSource( dataSource )
        .build();
        
    }
    @Bean
    public Job importPacienteJob(JobRepository jobRepository,JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder( "importPacienteJob", jobRepository)
        .incrementer( new RunIdIncrementer() )
        .listener( listener )
        .flow( step1 )
        .end()
        .build();
    
}
    @Bean
    public Step step1(JobRepository jobRepository,
    PlatformTransactionManager transactionManager,FlatFileItemReader<Paciente> itemReader, JdbcBatchItemWriter<Paciente> writer) {
        return new StepBuilder( "step1", jobRepository)
        .<Paciente, Paciente> chunk( 10 , transactionManager )
        .reader(itemReader)
        .processor( processor() )
        .writer( writer )
        .faultTolerant()
        .transactionManager( transactionManager )
        .build();
        
    }


}   

