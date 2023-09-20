package com.teste.batch.batchTesteApp.springfiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.teste.batch.batchTesteApp.springbatch.Paciente;

public class CSVHelper {
    public static String Type = "text/csv";
    static String [] HEADERs = {"id","nome","cpf","idade","fone","email"};

    public static boolean hasCSVFormat(MultipartFile file ){
        if (!Type.equals(file.getContentType())){
            return false;
        }
        return true;
    }

    public static List<Paciente> csvToPacientes (InputStream is ){
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) { 

                List<Paciente> pacientes = new ArrayList<Paciente>();

                Iterable<CSVRecord> csvRecords = csvParser.getRecords();
                for (CSVRecord csvRecord : csvRecords) {
                    Paciente paciente= new Paciente(
                            Long.valueOf(csvRecord.get("id")),
                            csvRecord.get("nome"),
                            csvRecord.get("cpf"),
                            Integer.valueOf(csvRecord.get("idade")),
                            csvRecord.get("fone"),
                            csvRecord.get("email")
                        );

                        pacientes.add(paciente);
                }
                return pacientes;
                }catch (IOException e){
                    throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
                }
        }
    }
