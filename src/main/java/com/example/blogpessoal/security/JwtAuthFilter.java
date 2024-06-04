package com.example.blogpessoal.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



//Objetivo da classe : Trazer as validações do token feitas na JWTService
//Confirmar se o token está chegando pelo HEader quando o usuário já estiver logado
//tratar o token
@Component
public class JwtAuthFilter  extends OncePerRequestFilter{

	
	
	 	@Autowired
	    private JwtService jwtService; //injeção de dependencia 

	    @Autowired
	    private UserDetailsServiceImpl userDetailsService;//injeção de dependencia 

		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
				FilterChain filterChain) throws ServletException, IOException {
		
			String authHeader = request.getHeader("Autorization");//String que monta parte do Header para conseguir trabalhar ,informando ao isominia que o token vem via header com a nomenclatura "Authorization"
			String token = null; //está como null porque ainda não tenho essa informação , pois que ainda será informado
			String username = null;
			
			 try{
		            if (authHeader != null && authHeader.startsWith("Bearer ")) {//verificando que o authHeader não está nulo , e se se inicia com a palavra "Bearer"
		                token = authHeader.substring(7);//está cortando a string e está retirando os 7 primeiros caracteres
		                username = jwtService.extractUsername(token);//extrai somente o nome
		            }
                          //SecurityContextHolder : Classe que tem as autorizações do usuario
		            //validação se existe um username que foi extraido do token e se não temos regras configuradas de autorizações
		            
		            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		            	//validando se o usuario extraido do token existe no banco de dados 
		                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		                  
		                //validateToken: verifica se o token expirou
		                  if (jwtService.validateToken(token, userDetails)) {
		                    //UsernamePasswordAuthenticationToken: Ajuda com a implementação de usuário e senha
		                	  UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());//verifica se o usuario existe novamente , e passa as autorizações necessárias para o usuario
		                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		                    SecurityContextHolder.getContext().setAuthentication(authToken);
		                }
		            
		            }
		            filterChain.doFilter(request, response);
//caso as informações acima não derem certo cai no catch abaixo, e return um HttpStatus
		        }catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException 
		                | SignatureException | ResponseStatusException e){
		            response.setStatus(HttpStatus.FORBIDDEN.value());
		            return;
		        }
		    }
		}



