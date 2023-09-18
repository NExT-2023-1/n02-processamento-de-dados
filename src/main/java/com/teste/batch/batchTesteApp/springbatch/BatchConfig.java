package com.teste.batch.batchTesteApp.springbatch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Bean
    public FlatFileItemReader<Paciente> reader() {
        return new FlatFileItemReaderBuilder<Paciente>()
        .name( "PacienteItemReader"  )
        .resource( new PathResource("C:/Users/Halley Pai/Desktop/New/Nova pasta/batchTesteApp/src/main/resources/Paciente.CSV") )
        .delimited()
        .names( "id", "nome", "cpf", "idade", "fone", "email" )
        .fieldSetMapper( new BeanWrapperFieldSetMapper<Paciente>() {{
            setTargetType( Paciente.class );
        }})
        .build();
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
    PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Paciente> writer) {
        return new StepBuilder( "step1", jobRepository)
        .<Paciente, Paciente> chunk( 10 , transactionManager )
        .reader( reader() )
        .processor( processor() )
        .writer( writer )
        .transactionManager( transactionManager )
        .build();
        
    }


}   

