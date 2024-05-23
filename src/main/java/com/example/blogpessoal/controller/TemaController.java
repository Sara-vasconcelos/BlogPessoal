package com.example.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.blogpessoal.model.Tema;
import com.example.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController ////anotação indicando para a Spring que essa classe é uma controller
@RequestMapping("/temas") //rota para chegar nessa classe no insomnia 
@CrossOrigin(origins = "*", allowedHeaders = "*") ////libera o acesso a outras maquinas, o * indica que qualquer máquina pode acessar / allowedHeaders = liberar passagem de parametros no header
public class TemaController {

	
	 @Autowired  //injeção de dependencia, é quase a mesma coisa de instanciar a classe TemaRepository, trás tudo da TemaRepository e o Jpa
	    private TemaRepository temaRepository;
	 
	 //LISTA os temas 
	    
	    @GetMapping // verbo http que recupera os dados
	    public ResponseEntity<List<Tema>> getAll(){//pega tudo
	        return ResponseEntity.ok(temaRepository.findAll()); // faz a busca , e vai  dar um ok caso de certo a requisição , retornando um numero 200 ok , que é a indicação que deu certo
	   
	        //List<Tema> : Garante que será uma lista de temas
	      //getAll: nome do nosso método
			//ResponseEntity : é um formato de retorno de dados em HTTP , que retorna se deu certo ou não
	    }
	    
	    
	  //BUSCAR tema  por ID
	    @GetMapping("/{id}")//dessa forma {id} , informa que o  dado é esperado uma variavel, nesse caso vai mostrar  o numero do id , ele sabe que não vai receber um texto e sim um número
	    public ResponseEntity<Tema> getById(@PathVariable Long id){
	    	// findById = SELECT * FROM tb_temas WHERE id = 1;
	        return temaRepository.findById(id)//  está sendo retornado no findById TUDO (findById:ele retorna tudo)
	            .map(resposta -> ResponseEntity.ok(resposta))//vai mapear as respotas e vai dar uma mensagem de retorno caso dê ok 
	            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());//build : não estou passando nenhum corpo , somente a informação vai aparecer dizendo que não achou e finaliza
	        
	     // @PathVariable Long id: Esta anotação insere o valor enviado no endereço do endpoint e Long porque é o tipo do dado Id
	    	// getById : Vai buscar o tema  por id mencionado
	    		//finByid: é a mesma coisa que :  SELECT * FROM tb_temas WHERE id = 1 
	    		//.orElse : Caso o usuario coloque um id que não existe , ele vai aparecer a mensagem do HTTPStatus "not found" não encontrado
	    		
	    }
	    
	  //SELECIONAR por descrição
	    
	 // ou seja : SELECT * FROM tb_temas WHERE descricao = "descricao";
	    
	    @GetMapping("/descricao/{descricao}")
	    public ResponseEntity<List<Tema>> getByTitle(@PathVariable 
	    String descricao){
	        return ResponseEntity.ok(temaRepository
	            .findAllByDescricaoContainingIgnoreCase(descricao));
	      //esse retorna a descricao , com o metodo personalizado que que criamos na repository
	    }
	    
	    
	  //CADASTRAR
	    
	    @PostMapping
	    public ResponseEntity<Tema> post(@Valid @RequestBody Tema tema){
	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body(temaRepository.save(tema));
	    }
	    
	    
	  //ATUALIZAR 
	    @PutMapping
	    public ResponseEntity<Tema> put(@Valid @RequestBody Tema tema){
	        return temaRepository.findById(tema.getId())
	            .map(resposta -> ResponseEntity.status(HttpStatus.CREATED)
	            .body(temaRepository.save(tema)))
	            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	    }
	    
	  //DELETAR
	    
	    @ResponseStatus(HttpStatus.NO_CONTENT)
	    @DeleteMapping("/{id}")
	    public void delete(@PathVariable Long id) {
	        Optional<Tema> tema = temaRepository.findById(id);
	        
	        if(tema.isEmpty())
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	        
	        temaRepository.deleteById(id);              
	    }
}
