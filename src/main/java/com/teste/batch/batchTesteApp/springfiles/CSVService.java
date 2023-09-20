package com.teste.batch.batchTesteApp.springfiles;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.teste.batch.batchTesteApp.springbatch.BatchConfig;
import com.teste.batch.batchTesteApp.springbatch.Paciente;

@Service
public class CSVService {
    @Autowired
    PacienteRepository pacienteRepository;
    BatchConfig batchConfig;

    public void save(MultipartFile file){
        try{
            List<Paciente> pacientes= CSVHelper.csvToPacientes(file.getInputStream());
            pacienteRepository.saveAll(pacientes);
        } catch (IOException e){
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
    public List<Paciente> getAllPacientes(){
        return pacienteRepository.findAll();
    }
}
