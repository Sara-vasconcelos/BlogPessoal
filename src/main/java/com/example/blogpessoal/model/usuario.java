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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="tb_usuario")
public class usuario {
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@NotBlank(message = "O atributo nome é obrigatório")
	private String nome;
	
	
	@NotNull(message = "O atributo email é obrigatório e não pode estar vazio")
	@Email(message = "O atributo email é obrigatório e não pode estar vazio") //valida se a informação está sendo usada como email , ela exige que tenha @ , ponto , etc
	private String usuario;
	
	@NotBlank(message = "O atributo senha é obrigatório")
	@Size(min = 8,  message = "O campo senha precisa ter no minimo 8 caracteres")
	private String senha;
	
	@Size( max = 5000, message = "O campo foto precisa ter no máximo 5000 caracteres")
	private String foto;
	
	//notnull: não pode ser nulo
	//notblank : não pode deixar vazio
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("usuario")
	private List<Postagem> postagem;
	
	
//esse objeto construtor está sendo usado apenas para fazer os testes
	public usuario(Long id, String nome,String usuario, String senha,String foto) {
		this.id = id;
		this.nome = nome;
		this.usuario = usuario;
		this.senha = senha;
		this.foto = foto;
	}
	
	public usuario() { }; //construtor vazio // esse também está sendo usado para os testes

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}
		
	
	
	
	

}
