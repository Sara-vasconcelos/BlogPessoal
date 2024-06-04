package com.example.blogpessoal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blogpessoal.model.usuario;

public interface UsuarioRepository extends JpaRepository<usuario ,Long>{

	public Optional<usuario>findByUsuario(String usuario);//vai buscar o usuario
	//aqui não precisa do @Param , porque esse aqui não será um parametro de solicitação , e nesse caso aqui ele é um dado interno
	//Select * from tb_usuario WHERE usuario = "sara@gmail.com"
	//optional é uma forma de evitar erros caso não encontre o usuário
	
}
