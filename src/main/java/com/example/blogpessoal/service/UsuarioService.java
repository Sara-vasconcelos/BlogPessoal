package com.example.blogpessoal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.blogpessoal.model.UsuarioLogin;
import com.example.blogpessoal.model.usuario;
import com.example.blogpessoal.repository.UsuarioRepository;
import com.example.blogpessoal.security.JwtService;

@Service //Informa ao Spring que essa classe trata as regras de negócio
public class UsuarioService {

	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Autowired
	private JwtService jwtservice;
	
	@Autowired
	private AuthenticationManager  authenticationManager;//classe do security que tem gestão de autenticação
	//permite acessar metodos que podem entregar ao objeto as suas autoridades concedidas
	
	
	//definir as regras para permitir o cadastro do usuario
	public Optional<usuario>cadastrarUsuario(com.example.blogpessoal.model.usuario usuario){
	//temos : nome | usuario(email) | senha |foto
		if(usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())//verifica se o usuario(email)existe
			return Optional.empty();//verifica se está vazio
		
		usuario.setSenha(criptografarSenha(usuario.getSenha()));//senha que o usuario digitou está sendo criptografia
		return Optional.of(usuarioRepository.save(usuario));//salva informações 
	
	}
	//metodo que vai tratar para a senha ser criptografada antes de ser persistida no banco
	private String criptografarSenha(String senha) {//parametro que ele vai receber a senha e que vai ser uma string
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();//classe que trata criptgrafia , então conseguimos aplicar na senha , e o nome dela chamamos de encoder
		return encoder.encode(senha); //metodo encoder sendo aplicado na senha 
	}
	
	//evitar que dois usuarios tem o mesmo email
	public Optional<usuario> atualizarUsuario(usuario usuario){
		//o if abaixo valida se o id passado existe no banco de dados 
		if(usuarioRepository.findById(usuario.getId()).isPresent()) {
			
		//objeto Optional porque ele pode existir ou não
			//se existe ele preenche
			Optional<usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
			
			//se o usuario está presente , e já está preenchido, se o id já existe
			if(buscaUsuario.isPresent() && (buscaUsuario.get().getId()) !=usuario.getId())
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);//aparece esse erro, para que não tenha dados duplicados
			usuario.setSenha(criptografarSenha(usuario.getSenha()));//se não existir , ele criptografa a senha 

			return Optional.ofNullable(usuarioRepository.save(usuario));//perguntar 

		}
		return Optional.empty();

	}
	
	//objetivo garantir as regras o login 
	
public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {
        
        // Gera o Objeto de autenticação
	//variavel que guarda as credenciais do meu usuario
	//Objeto com os dados do usuario que tenta logar
		var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha());
		
        // Autentica o Usuario
		Authentication authentication = authenticationManager.authenticate(credenciais);
        
        // Se a autenticação foi efetuada com sucesso
		if (authentication.isAuthenticated()) {

            // Busca os dados do usuário
			Optional<usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());

            // Se o usuário foi encontrado
			if (usuario.isPresent()) {

                // Preenche o Objeto usuarioLogin com os dados encontrados 
			   
				usuarioLogin.get().setId(usuario.get().getId());
                usuarioLogin.get().setNome(usuario.get().getNome());//prenche os dados conforme as informações que estão no usuaroLogin
                usuarioLogin.get().setFoto(usuario.get().getFoto());//mesma coisa 
                usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario()));//mesma coisa 
                usuarioLogin.get().setSenha("");//a senha está passando vazia porque cada um vai ter uma , ela é criptografia e não teremos acesso
				
                 // Retorna o Objeto preenchido
			   return usuarioLogin;
			
			}

        } 
            
		return Optional.empty();//

    }


//metodo que vai la jwtservice e e gera o token do usuario 
	private String gerarToken(String usuario) {
		return "Bearer " + jwtservice.generateToken(usuario);
	}
}
