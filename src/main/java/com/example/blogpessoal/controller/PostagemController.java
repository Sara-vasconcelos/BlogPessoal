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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.blogpessoal.model.Postagem;
import com.example.blogpessoal.repository.PostagemRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

//todas as anotações estão sendo importadas

@RestController //anotação indicando para a Spring que essa classe é uma controller
@RequestMapping ("/postagens") //rota para chegar nessa classe no insomnia 
@CrossOrigin(origins = "*", allowedHeaders = "*") //libera o acesso a outras maquinas, o * indica que qualquer máquina pode acessar / allowedHeaders = liberar passagem de parametros no header
public class PostagemController {                  // allowedHeaders , libera a passagem de parametros no header 

	
	@Autowired //injeção de dependencia, é a mesma coisa de instanciar a classe PostagemRepository. Como estou fazendo isso , toda vez que eu quiser usar a PostagemRepository eu chamo esse Autowired
	private PostagemRepository postagemRepository;  //A primeira Postagem , é a classe que estou instanciado , e a segundo é o nome do objeto.

	//LISTA todas as postagens

	@GetMapping //Verbo HTTP
	public ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll()); // ele vai dar um ok caso de certo a requisição , retornando um numero 200 ok , que é a indicação que deu certo
		
		//List<Postagem> : Garante  que terá uma lista de postagem
		
		//getAll: nome do nosso método
		//começa com 4 é erro, 5 também
		//ResponseEntity : é um formato de retorno de dados em HTTP , que retorna se deu certo ou não
}
	
		
	
	
	//BUSCAR a postagem por ID
	
	@GetMapping("/{id}")// dessa forma {id} , informa que o  dado é esperado uma variavel, nesse caso vai mostrar  o numero do id , ele sabe que não vai receber um texto e sim um número
	public ResponseEntity<Postagem> getById(@PathVariable Long id){
		// findById = SELECT * FROM tb_postagens WHERE id = 1;
		return postagemRepository.findById(id)//  está sendo retornado no findById TUDO (findById:ele retorna tudo)
				.map(resposta -> ResponseEntity.ok(resposta))//vai mapear as respotas e vai dar uma mensagem de retorno caso dê ok 
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());//build : não estou passando nenhum corpo , somente a informação vai aparecer dizendo que não achou e finaliza
	// @PathVariable Long id: Esta anotação insere o valor enviado no endereço do endpoint e Long porque é o tipo do dado Id
	// getById : Vai buscar a postagem  por id mencionado
		//finByid: é a mesma coisa que :  SELECT * FROM tb_postagem WHERE id = 1 
		//.orElse : Caso o usuario coloque um id que não existe , ele vai aparecer a mensagem do HTTPStatus "not found" não encontrado
		
	
	}
	
	//SELECIONAR por titulo 
	
	// ou seja : SELECT * FROM tb_postagens WHERE titulo = "titulo";

	@GetMapping ("/titulo/{titulo}")//ficará assim : localhost:8080/postagens/titulo/nome do titulo -- Nesse caso o /titulo é fixo e o {titulo} , é uma variavel porque cada postagem tem um diferente
	public  ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){//String porque é um texto
		
		return  ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
		//esse retorna o titulo , com o metodo personalizado que que criamos na repository
	}
	
	
	//CADASTRAR uma postagem 
	
	// Ou seja seria isso no banco: INSERT INTO tb_postagens (titulo, texto, data) VALUES ("Título", "Texto", "2024-12-31 14:05:01");

	// post: nome do método , mas poderia ser qualquer um 
	//ResponseEntity : é o retorno em formato HTTP  que irá verificar a <Postagem>
	//Valid : essa anotação coloca em prática todas as validações que eu coloquei quando eu criei a classe Postagem , exemplo @Size(min=5,max=1000. Dessa forma é validado na hora do cadastro
	//RequestBody : Quer dizer que essa requisição vai ter um corpo , não somente uma resposta 
	
	@PostMapping //cria uma requisição
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
		//status(HttpStatus.CREATED: Vai informar em formato Http que foi criado a postagem
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(postagemRepository.save(postagem));//save : metodo da repository , que vai fazer um INSERT INTO , e mostrar , ou seja , ele salva a postagem e retorna a mesma , então nesse caso o ResponseEntity tem um corpo
		
		//TESTANDO:
		//no INSOMNIA depois de colocar o método Post , e o endereço do link , clico em BODY e seleciono JSON
		//depois entre {} eu coloco os parametros que quero receber , como o id é auto increment , não precisa 
		// data também não porque quando criamos a Postagem , a data era padrão e gerava automatica 
		// então precisamos colocar somente o titulo e o texto da postagem exemplo : 
//		{
//			"titulo" : "Filmes que indico",
//			"texto" : "Espera de um milagre"	
//		}
		
		//a resposta do servidor é : 
//		{
//			"id": 3,
//			"titulo": "Filmes que indico",
//			"texto": "Espera de um milagre",
//			"data": "2024-05-22T10:49:05.673005"
//		}
		
		//Verbos HTTP :
		//GET: Recupera dados do banco de dados , quando eu estou trazendo algo do banco de dados 
		//POST : Faz a inserção , ele guarda a informação dentro de um banco de dados 
		//Put : UPDATE : ele altera os dados , atualiza
		//Delete: Deleta algum registro do banco de dados 
	}
	
	
	//ATUALIZAR postagem
	
	//put : nome do metodo que eu criei
	//ResponseEntity : vai verificar o Postagem , e retorna a mensagem em formato HTTP
	//Valid: Valida as informações para verificar se está tudo ok 
	//RequestBody: Informa que o retorno vai ter um corpo que no caso é uma postagem da classe Postagem
	
	 
	@PutMapping //altera e atualiza
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
		return postagemRepository.findById(postagem.getId())//está buscando um id (FindByAll) , e vai buscar pelo id que eu passar (postagem.id)
				.map(resposta-> ResponseEntity.status(HttpStatus.OK)//caso eu encontre o id mencionado ele vai mapear , e retornar um status de ok 
				.body(postagemRepository.save(postagem)))//vai salvar a alteração ,e retornar um  corpo que no caso é postagem
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());//caso não encontre o id passado , cai nesse orElse, e retorna um status NOT FOUND(Não encontrado) , e não vai ter corpo somente a mensagem (build)
	}
	
	//DELETAR postagem
	//Fazer os imports
	//optional : não quebra a aplicação e retorna algo sem quebrar o código mesmo que o dado esteja vazio
	
	@DeleteMapping("/{id}") //aqui ele vai garantir que vai pedir um numero de id para deletar(SEGURANÇA)
	public void delete(@PathVariable Long id) {//metodo void , ou seja não tem retorno , nesse caso preciso usar o ResponseStatusException para uma saída especifica
		Optional<Postagem> postagem = postagemRepository.findById(id);//vai trazer a linha que eu mencionei
		if(postagem.isEmpty())//objeto  postagem está vazio?
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);//retorna um status de exception , caso não encontre o id , com a mensagem NOT FOUND
		//ele executa o response status de excessão, ele vai apenas colocar o botão como not found
		postagemRepository.deleteById(id);//caso encontre vai pegar o objeto postagem com o id mencionado 
	}
}
	
