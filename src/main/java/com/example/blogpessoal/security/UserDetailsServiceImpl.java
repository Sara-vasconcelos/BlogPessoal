package com.example.blogpessoal.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.blogpessoal.model.usuario;
import com.example.blogpessoal.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	//objeto da classe : verificar se o usuario existe no banco de dados
	

	@Autowired // injeção de dependencia
	private UsuarioRepository usuarioRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// esse metodo vai validar se o usuario existe 
		Optional<usuario> usuario = usuarioRepository.findByUsuario(username);//buscando o usuario
		
		
		//estamos validando que o usuario  existe - e que foi encontrado pelo metodo finByUsuario
		
		if(usuario.isPresent()) {//verifica se está presente 
			
			return new UserDetailsImpl(usuario.get());//retorna todas as informações do usuario que foi encontrado, 
			//esse UserDetailsImpl foi um metodo criado na classe UserDetailsImpl, onde passando o usuario ele já retorna o nome e a senha . 
			//O usuario retornado é o foi encontrado pelo findByUsuario
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);//retorna erro caso não encontre
			//FORBIDDEN : significa Proibido 
		}
	
	}
	
}
