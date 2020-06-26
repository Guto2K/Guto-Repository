package com.example.algamoney.api.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//para que o programa faça a ligação com o banco da tabela com mesmo nome da classe, podendo persistir os dados dessa classe no banco e trazidos tmb.diz tmb que é uma entidade essa classe.
@Entity
//nome que esta na tabela do banco
@Table(name ="categoria")
public class Categoria {
	
	//diz q é o id
	@Id
	// diz que o id é gerado automaticamente no banco n necessita do hibernate, jpa se preocupar para criar o id. ou seja ele ignora esse campo na hora de criação.
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	//só é possivel pois foi adicionado no pomxml o spring data jpa ele vem com hibernate validator e por ser spring boot ja esta configurado. isso é o bean validation
	@NotNull
	@Size(min = 3, max =20)
	private String nome;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	

}
