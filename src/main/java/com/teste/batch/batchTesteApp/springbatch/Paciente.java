package com.teste.batch.batchTesteApp.springbatch;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

import lombok.Data;
//import lombok.ToString;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    
    @Id
    private Long id;
    private String nome;
    private String cpf;
    private int idade;
    private String fone;
    private String email;
    
    

}
