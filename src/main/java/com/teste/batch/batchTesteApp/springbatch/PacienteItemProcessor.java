package com.teste.batch.batchTesteApp.springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;




public class PacienteItemProcessor implements ItemProcessor<Paciente, Paciente> {

    private static final Logger log = LoggerFactory.getLogger(PacienteItemProcessor.class);

    @Override
    public Paciente process(final @NonNull Paciente paciente) throws Exception {
        final String nome = paciente.getNome().toUpperCase();
        final String cpf = paciente.getCpf();
        final int idade=paciente.getIdade();
        final String fone=paciente.getFone();
        final String email=paciente.getEmail();

        final Paciente novoPaciente = new Paciente((long)Math.random(), nome, cpf, idade, fone, email);

        log.info(   "Converting (" + novoPaciente+")into("+ novoPaciente +    ")");

        return novoPaciente;


    }
    
}
