package com.example.blogpessoal.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//Objetivo da classe : sobrescrever a configuração padrão da Spring Security, ou seja , agora o Security segue a configuração nova
//Liberar links que não precisam de login


@Configuration// informa que a classe é de configuração
@EnableWebSecurity //Está aplicando toda a configuração dessa classe , em todo o projeto
public class BasicSecurityConfig {

	@Autowired //injeção de depedencias que trás o JwtAuthFilter
    private JwtAuthFilter authFilter;

	
	// Beans: Um Bean é um objeto que é instanciado, montado e gerenciado pelo Spring.
	//Bean: No Spring, os objetos que formam a espinha dorsal da sua aplicação e que são gerenciados pelo Spring são chamados de Beans.
	
	 //ajustes de usuario e senha , trás o usuario que já validamos 
    @Bean
    UserDetailsService userDetailsService() {

        return new UserDetailsServiceImpl();
    }

   
    //criptografia da senha 
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    //garantir que consegimos validar usuario e senha 
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    //implementar a parte de gerenciamento de autenticação
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    	http
	        .sessionManagement(management -> management//gerenciamento da sessão entre o usuario e a aplicação
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        		.csrf(csrf -> csrf.disable())
	        		.cors(withDefaults());

    	//autorização das requisões
    	//para permitir que a pessoa consegue visualizar todas as postagens é preciso colocar mais uma permissão
    	//exemplo : repito o .requestMatchers(/postagens).permitAll
    	//criar outro requestMatchers pro getall e dar um permit all
    	http
	        .authorizeHttpRequests((auth) -> auth
	                .requestMatchers("/usuarios/logar").permitAll()//primeiro eu coloquei as rotas usuario e logar para permitir que todos os usuarios consigam acessar sem logar	               
	                .requestMatchers("/usuarios/cadastrar").permitAll()//liberando as rotas usuarios e cadastrar permitindo acesso ao cadastro
	                .requestMatchers("/error/**").permitAll()//permitindo acesso aos erros
	                .requestMatchers(HttpMethod.OPTIONS).permitAll()//permitindo os metodos http
	                .anyRequest().authenticated())//trás as autorizações de acesso
	        .authenticationProvider(authenticationProvider())
	        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
	        .httpBasic(withDefaults());

    	
//    	.requestMatchers(HttpMethod.GET,"/produtos").permitAll() - Esse eu usaria caso eu queira que qualquer método GET que tenha o caminho /produtos 
//        .requestMatchers(HttpMethod.GET,"/produtos/**").permitAll() - Esse libera somente os GET , que tem o caminho end point /produtos que tenha qualquer coisa depois por isso (**)
//        .requestMatchers(HttpMethod.GET,"/categorias").permitAll() - Esse libera somente os métodos GET , que possuem o end point /categorias , sendo assim o usuário consegue buscar e ver categorias
//        .requestMatchers(HttpMethod.GET,"/categorias/**").permitAll() -- Permite o acesso de todos os GET , com o end point /categorias e que tenha qualquer coisa depois
		return http.build();//retorna o corpo do erro http 

    }
	
	
}
