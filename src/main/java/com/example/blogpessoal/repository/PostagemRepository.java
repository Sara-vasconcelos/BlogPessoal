package com.example.blogpessoal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blogpessoal.model.Postagem;

//postagem é o nome da model , porque eu quero ligar a repository com a model, e depoisda virgula é o tipo de dado do id(primary Key)
//JpaRepository, é uma classe da jpa , que é uma dependencia que incluimos na durante a construção da API
//Ela contem metodos que vão realizar querys no banco de dados 
//Ela assume o controle de criar automat. o SELECT * FROM tb_postagens , vai automatico
//Como ela está conectada com a model que é a postagem  , ela já sabe a tabela que está lá e o nome , além de todos os dados
//Long quer dizer que a chave primaria é do tipo LONG
public interface PostagemRepository  extends JpaRepository<Postagem ,Long>{

}
