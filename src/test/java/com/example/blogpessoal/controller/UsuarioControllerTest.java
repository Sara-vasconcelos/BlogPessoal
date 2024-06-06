package com.example.blogpessoal.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import com.example.blogpessoal.model.usuario;
import com.example.blogpessoal.repository.UsuarioRepository;
import com.example.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)//vai jogar uma porta aleatória para rodar , que não seja a mesma em que a API esteja rodando
@TestInstance(TestInstance.Lifecycle.PER_CLASS)// Roda a classe inteira de uma vez , ciclo de vida é pela classe , ele considera que será executado todos os métodos em uma só classe de uma vez , mas posso executar separadamente
public class UsuarioControllerTest {

	
	@Autowired
	private UsuarioRepository  usuarioRepository;//está acessando a usuarioRepostory que tem acesso com o banco de dados 
	
	@Autowired
	private UsuarioService usuarioService; //quero acessar a Service porque tem a lógica do usuário, 
	
	@Autowired
	private TestRestTemplate  testRestTemplate; //consegue simular uma requisição rest , ou seja , a mesma coisa que fizemos no insominia


@BeforeAll //antes de tudo/antes de fazer qualquer coisa roda o que tem abaixo
void start() { //void : sem retorno
	
	
	usuarioRepository.deleteAll(); //antes de começar vai no banco de dados e deleta tudo que tem lá antes de rodar, no caso o banco de dados H2 , não é o MySQL
	
	usuarioService.cadastrarUsuario(new usuario(0L, "Root","root@root.com", "rootroot", " "));//cria um novo usuário sempre. Depois que apagar tudo ele garante que seja criado um usuario para testar
//0L : é o campo do id , L porque é long , e 0 porque é padrão , e como ele é increment o primeiro que for criado será com o id 1 
}


//Vai testar se a resposta de CREATED vai aparecer quando criar um usuário novo 
@Test //indica que é um test
@DisplayName("Deve cadastrar um novo usuário") //uma mensagem , como eu quero que aparece no meu relatório
public void DeveCriarUmNovoUsuario(){
	//HttpEntity: objeto sendo passado pelo json , como no insominia
	
	HttpEntity<usuario> corpoRequisicao =  new HttpEntity<usuario>(new usuario(0L,"Sara","sara@gmail.com","12345678", "  ")); //está criando um novo usando como se eu estivesse criando no insomnia direto no json
	
	//exchange: envia e recebe algo
	ResponseEntity<usuario> corpoResposta = testRestTemplate //resposta do retorno
		.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, usuario.class); //envia para o end point(usuario/cadastrar), usando o metodo post , envia o corporequisicao que criamos em cima ,  e a classe de usuario,  que será o padrão do retorno da classe de usuario 
		
	assertEquals(HttpStatus.CREATED,corpoResposta.getStatusCode());//aqui ele está comparando o que ele quer receber, no caso ele informa que espera receber um CREATED, enviando o corpoResposta
}


//vai tentar cadastrar um usuario que já existe , 
@Test //indica que é um test
@DisplayName("Não deve criar um novo usuário") //uma mensagem , como eu quero que aparece no meu relatório
public void NaoDeveCriarUmNovoUsuario(){
	//HttpEntity: objeto sendo passado pelo json , como no insominia
	
	usuarioService.cadastrarUsuario(new usuario(0L, "Natalia", "natalia@gmail.com","12345678", "  "));//criou um usuário 
	
	HttpEntity<usuario> corpoRequisicao =  new HttpEntity<usuario>(new usuario(0L,"Natalia","natalia@gmail.com","12345678", "  ")); //está tentando criar  um novo usuário com o mesmo email do anterior . Esse comando é como se eu estivesse criando um usuário no insomnia direto no json

	//exchange: envia e recebe algo
	ResponseEntity<usuario> corpoResposta = testRestTemplate //resposta do retorno
		.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, usuario.class); //envia para o end point(usuario/cadastrar), usando o metodo post , envia o corporequisicao que criamos em cima ,  e a classe de usuario,  que será o padrão do retorno da classe de usuario 
		
	assertEquals(HttpStatus.BAD_REQUEST,corpoResposta.getStatusCode());//aqui ele está comparando o que ele quer receber, no caso ele informa que espera receber um CREATED, enviando o corpoResposta
}

//Deve atualizar um Usuário

@Test //indica que é um test
@DisplayName("Atualizar um usuário")
public void  deveAtualizarUmUsuario() {
	
	Optional<usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new usuario(0L, "Juliana Paes", "Juliana@gmail.com","juliana123", " "));//para armazenar o resultado da persistência de um Objeto da Classe Usuario no Banco de dados, através do Método cadastrarUsuario() da Classe UsuarioService.

	usuario usuarioUpdate = new usuario(usuarioCadastrado.get().getId(), "Juliana Paes Silva","julianapaes@gmail.com", "juliana123", " "); // utilizado para atualizar os dados que estão  no Objeto usuarioCadastrado

	HttpEntity<usuario> corpoRequisicao =  new HttpEntity<usuario>(usuarioUpdate); //nesse comando ele está tentando atualizar os dados , basicamente igual o insomnia faz internamente
// a Requisição HTTP será enviada através do Método exchange() da Classe TestRestTemplate
	//e a Resposta da Requisição (Response) será recebida pelo objeto corpoResposta do tipo ResponseEntity
	ResponseEntity<usuario> corpoResposta = testRestTemplate
			.withBasicAuth("root@root.com", "rootroot") //efetua login com um email e senha de um usuario que já existe no banco de dados(o primeiro que criamos), se não ele não consegue realizar o teste
			.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, usuario.class);
	
	assertEquals(HttpStatus.OK,corpoResposta.getStatusCode());//se o httpStatus for Ok , o teste passou
}

//Listar todos os Usuários

@Test //indica que é um test
@DisplayName("Listar todos os usuários")
public void deveMostrarTodosUsuarios() {

	usuarioService.cadastrarUsuario(new usuario(0L, "Gabriel Vasconcelos", "biel@gmail.com", "gabriel123", " "));//cadastrou um novo usuário
	usuarioService.cadastrarUsuario(new usuario(0L, "Mateus Vasconcelos", "mateus@gmail.com", "mateus123", " "));//cadastrou um novo usuário
	
	//Nesse teste passamos que o tipo seria String , porque se colocasse usuario , como nos outros , ele viria como um Array o que daria mais trabalho em um ambiente de teste, dessa forma co a String fica mais fácil , e por isso também colocamos String.class que é o conteúdo esperado no Corpo da Resposta (Response Body)
	//paramêtros são : URL, o método HTTP,  Objeto HttpEntity,e O conteúdo esperado no Corpo da Resposta (Response Body)
	ResponseEntity<String> Resposta = testRestTemplate//A resposta esperada é do tipo String
			.withBasicAuth("root@root.com", "rootroot")	
			.exchange("/usuarios/all", HttpMethod.GET, null, String.class);//Requisições do tipo GET não enviam Objeto no corpo da requisição,por isso O objeto será nulo (null)
	// Neste exemplo como o objeto da requisição é nulo, a resposta esperada será do tipo String (String.class)
	
	//Ao criar uma requisição do tipo GET no Insomnia é enviado apenas a URL do endpoint. Esta regra também vale para o Método DELETE.
	assertEquals(HttpStatus.OK,Resposta.getStatusCode());//compara e verifica se o retorno da resposta será OK
	
}




}
