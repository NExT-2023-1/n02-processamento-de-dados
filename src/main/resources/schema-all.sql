DROP TABLE Paciente IF EXISTS;

CREATE TABLE Paciente (
    id long IDENTITY NOT NULL PRIMARY KEY,
    nome VARCHAR(50),
    cpf VARCHAR(20),
    idade int(10),
    telefone VARCHAR(20),
    email VARCHAR(50)
);
