package com.teste.batch.batchTesteApp.springbatch;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
//import lombok.ToString;

@Data
@Builder
@Entity
//@ToString
public class Paciente {
    
    @Id
    private Long id;
    private String nome;
    private String cpf;
    private int idade;
    private String fone;
    private String email;
    
    public Paciente(Long id, String nome, String cpf, int idade, String fone, String email) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.fone = fone;
        this.email = email;
    }
}
