package com.example.blogpessoal.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blogpessoal.model.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long>{
	//seria como se fosse isso : SELECT * FROM tb_temass WHERE descricao LIKE "%descricao%";
	//find: Select
	//all: *
	//By: WHERE
	//Descricao :Atributo da Classe Tema
	//Containing: LIKE "%descricao%"
	//IgnoreCase: Ignorando letras maiúsculas ou minúsculas
	//@Param("descricao") :  Define a variável String descricao como um parâmetro da consulta. Esta anotação é obrigatório em consultas do tipo Like.
	//String descricao : Parâmetro do Método contendo a descrição que você deseja procurar.
	public List<Tema> findAllByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);

}
