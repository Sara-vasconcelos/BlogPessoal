package com.example.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.blogpessoal.model.Postagem;

//postagem é o nome da model , porque eu quero ligar a repository com a model, e depoisda virgula é o tipo de dado do id(primary Key)
//JpaRepository, é uma classe da jpa , que é uma dependencia que incluimos na durante a construção da API
//Ela contem metodos que vão realizar querys no banco de dados 
//Ela assume o controle de criar automat. o SELECT * FROM tb_postagens , vai automatico
//Como ela está conectada com a model que é a postagem  , ela já sabe a tabela que está lá e o nome , além de todos os dados
//Long quer dizer que a chave primaria é do tipo LONG
//JpaRepository : classe jpa - metodos que vão realizar query no banco
public interface PostagemRepository  extends JpaRepository<Postagem ,Long>{
	
	public List <Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo")String titulo);
	//findAllByTitulo: vai fazer um select buscando por titulo 
	//exe:: SELECT * FROM tb_postagens WHERE titulo = "titulo";
// Containing : é uma busca NÃO exata , como se fosse o LIKE , que busca parcialmente o que vc quer e trás os resultados
	//findAllByTituloContainingIgnoreCase => SELECT * FROM tb_postagens WHERE titulo LIKE "%titulo passado pelo usuario, sem se importar com maiuscula ou minuscula%"
//IgnoreCase: Mesmo que eu escreva com maiuscula ou minuscula ele ignorar e respeitar somente o texto
	//IgnoreCase: não ignora acento , somente as letras
	
	//@Param ("titulo") String titulo): @Param está confirmando que vai receber um parametro titulo que é uma String Titulo
}
