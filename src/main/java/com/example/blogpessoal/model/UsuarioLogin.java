package com.example.blogpessoal.model;

public class UsuarioLogin {//classe que carrega os dados do usuario , e com isso consegue carregar um token
	
	//classe auxiliar para trasportar os dados , sem colocar os dados do usuario para em um banco de dados , ou seja , fica mais seguro
//não podemos guardar o token no banco de dados
	
	private Long id;
	private String nome;
	private String usuario;
	private String senha;
	private String foto;
	private String token;
	
	
	
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	

}
