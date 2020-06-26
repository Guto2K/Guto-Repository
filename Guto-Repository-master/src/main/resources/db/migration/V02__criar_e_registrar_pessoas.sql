/*
 * Se não tivesse embedado teria que criar 2 tabelas no banco, com embedeed cria 1, mas pra ficar mais organizado separa como se fosse 2 classes, mas são apenas uma.
 * CREATE TABLE endereco(
conteundo normal do endereço e sua primary key.


)


CREATE TABLE pessoa(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	codEndereco int,
	ativo BOOLEAN,
	constraint pk_pessoa primary key (codigo),
	constraint fk_endereco foreign key (codEndereco) references endereco (codigo)
	
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

*/



CREATE TABLE pessoa(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	logradouro VARCHAR(50),
	numero int,
	complemento VARCHAR(50),
	bairro VARCHAR(50),
	cep VARCHAR(15),
	cidade VARCHAR(50),
	estado VARCHAR(50),
	ativo BOOLEAN NOT NULL
	
)ENGINE=InnoDB DEFAULT CHARSET=utf8;






INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo ) values ('Ricardo', 'Rua Canario',128, null, 'Santa Celta','19875-455','Carmelita da Serra','SP',TRUE);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo ) values ('Ana', 'Rua Tomé',1886, null, 'Santa Celta','19875-455','Carmelita da Serra','SP',TRUE);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo ) values ('Paulo', 'Rua Castor',45, 'Bloco 3', 'Santa Celta','19875-455','Carmelita da Serra','SP',TRUE);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo ) values ('Marcos', 'Rua Daniva',5877, null, 'Santa Celta','19875-455','Carmelita da Serra','SP',TRUE);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo ) values ('Carol', 'Rua São do Zé',963, 'Bloco 32', 'Santa Celta','19875-455','Carmelita da Serra','SP',TRUE);
