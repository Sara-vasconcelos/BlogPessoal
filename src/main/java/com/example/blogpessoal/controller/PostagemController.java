package com.example.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogpessoal.model.Postagem;
import com.example.blogpessoal.repository.PostagemRepository;

//todas as anotações estão sendo importadas

@RestController //anotação indicando para a Spring que essa classe é uma controller
@RequestMapping ("/postagens") //rota para chegar nessa classe no insomnia 
@CrossOrigin(origins = "*", allowedHeaders = "*") //libera o acesso a outras maquinas, o * indica que qualquer máquina pode acessar, sem essa anotação , e não consiga acessar depois 
public class PostagemController {                  // allowedHeaders , libera a passagem de parametros no header 

	
	@Autowired //injeção de dependencia, é a mesma coisa de instanciar a classe PostagemRepository. Como estou fazendo isso , toda vez que eu quiser usar a PostagemRepository eu chamo esse Autowired
	private PostagemRepository postagemRepository;  //A primeira Postagem , é a classe que estou instanciado , e a segundo é o nome do objeto.


	@GetMapping //Verbo HTTP
	public ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll()); // ele vai dar um ok caso de certo a requisição , retornando um numero 200 ok , que é a indicação que deu certo
		
		//List<Postagem> : Informa que terá uma lista de postagem
		//
		//getAll: retorna um tipo de dado  , que retorna se deu certo a requisição
		//começa com 4 é erro, 5 também
		//ResponseEntity : é um formato de retorno de dados em HTTP , que retorna se deu certo ou não
}
}
	
