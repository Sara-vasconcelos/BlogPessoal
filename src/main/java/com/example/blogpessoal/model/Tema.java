package com.example.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity  //indica uma criação de tabela(entidade)
@Table(name = "tb_temas")//nome da tabela 

public class Tema {
	
	@Id  // torna o id uma chave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // faz com que a chave seja auto increment
	private Long id;//tipo long no Java  e no banco de dados será criado como BIGINT

	//NotNull : Se não passar nada ele fica nulo sem nada
	//NotBlank : Ele fica em branco , mas não fica nulo
	
	@NotBlank(message = "O atributo TITULO é obrigatorio") //validation , vai validar o atributo NOT NULL e não deixa o campo vazio
	@Size(min=4,max=50, message  = "O atributo texto deve ter no minimo 4 caracteres e no maximo 50")
	private String descricao;//tipo string no Java e no banco será VARCHAR(255)
	
	//OneToMany :  indica que a Classe Tema será o lado 1:N
	//fetch: estratégia de busca e carregamento dos dados das entidades relacionadas durante uma busca, estratégia de busca e carregamento dos dados das entidades relacionadas durante uma busca 
	//FetchType.LAZY :  No projeto Blog Pessoal utilizaremos o tipo LAZY (preguiçosa), ou seja, ao carregarmos os dados de uma Postagem, ele não carregará os dados do Tema associado a cada Postagem até que os dados sejam solicitados.

	
	//fetch: Padrão de como eu quero trazer essas informações relacionadas, Lazy: preguiçoso , só trás se eu pedir. Eager : rápido , trás tudo de uma vez
	//fetchType.Lazy: Modo preguiçoso , trás somente se pedir , para não sobrecarregar
	
	//mappedBy: Ele determina por onde vai começar o mapeamento , no caso vai começar por tema 
	//cascade: qual o tipo de cascata , tem vários 
	//CascadeType.REMOVE: Se eu apagar o tema a postagem é apagada também, por isso em cascata 
	// @JsonIgnoreProperties: Evita 
	@OneToMany(fetch = FetchType.LAZY,mappedBy =  "tema" , cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("tema")//evita criar um loop //ignora sempre o cara que eu estou criando
	private List<Postagem> postagem;// terá uma Collection List contendo Objetos da Classe Postagem
	//postagem , nome do objeto 

	//get e set 
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}
	

	
}
