package com.example.blogpessoal.model;


import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity    //diz que essa classe vai se tornar uma entidade(tabela) no banco de dados 
  @Table(name="tb_postagem")        //possibilita dar um nome para a tabela
public class Postagem {
	
	//cada dado aqui vai para a tabela 
	//todos os comandos estão sendo importados
	
	@Id // torna o id uma chave primaria
	@GeneratedValue(strategy=GenerationType.IDENTITY) // faz com que a chave seja auto increment
	private Long id;
	
	@NotBlank(message = "O atributo TITULO é obrigatorio")//validation , vai validar o atributo NOT NULL e não deixa ir vazio
	@Size(min=5,max=100, message  = "O atributo titulo deve ter no minimo 5 caracteres e no maximo 100") // tamanho min e max de caracter, caso o usuario não cumpra essa regra aparece essa mensagem
	private String  titulo;
	
	
	@NotBlank(message = "O atributo TEXTO é obrigatório")
	@Size(min=5,max=1000, message  = "O atributo texto deve ter no minimo 5 caracteres e no maximo 1000")
	private String texto;
	
	@UpdateTimestamp // ele vai identificar sozinho o horario e a data da postagem e preencher sozinho
	private LocalDateTime data; //  pradronizar a data do banco

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}


	
	

}
