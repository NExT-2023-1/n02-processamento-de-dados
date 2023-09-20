package com.teste.batch.batchTesteApp.springfiles;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teste.batch.batchTesteApp.springbatch.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente,Long> {
    
}
