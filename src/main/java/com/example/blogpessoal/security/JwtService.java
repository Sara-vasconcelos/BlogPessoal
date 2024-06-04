package com.example.blogpessoal.security;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

//Objetivo da classe : Criar e confirmar o token

@Component //anotação que informa que a classe é um componente do spring
public class JwtService {

	// constante que gera uma chave para encodar as informações do token
	public static final String SECRET ="d55feb6c5e3dd706c533434cc9ca63883ea76a8085e35a007c6cdc1eff7e02cb"; //final indica que é uma constante ,  ou seja não pode ser alterado
	
	//token composto por email da pessoa/data de expiração/horário que vai expirar/assinatura
	
	//exemplo: sara@gmail.com 2024-06-04 9:40 assinatura 
	
	//mas ele não fica legivel para nós devido ao um processo de encodamento
	
	
	
	
	
	//assinatura do token
	//SECRET: chave de encodamento 
	
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);//encodamento da SECRET
		return Keys.hmacShaKeyFor(keyBytes);//retorna a chave já tratada 
	}

	
	//Claims : é uma classe que  carrega declarações e informações 
	//Declaracao usuario/declaração data/declaração assinatura
	//O claims trata  as declarações  do token
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey()).build()//extrai a declaração da assinatura do token
				.parseClaimsJws(token).getBody();//ajuda a extrair 
	}

	
	//Pega a assinatura e trata ela para ficar entendivel
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	//recupera os dados do claim onde encontramos o email(usuario)
	public String extractUsername(String token) {//extrai o usuario
		return extractClaim(token, Claims::getSubject);
	}

	//trata a data que o token expira 
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	
//valida se a data que o token expira está dentro da validade ou seja , verifica se a data atual não atingiu esta data
	private Boolean isTokenExpired(String token) {//retorna verdadeiro ou falso
		return extractExpiration(token).before(new Date());
	}

	//valida se o usuario que foi extraido do token condiz com o usuario que a UserDetails tem e se está dentro da data de validade do token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	
	//calcular o tempo de validade do token , formar o claim com as informações do token
	//forma todo o token 
	//JWT : Classe que consegue criar um token com mais segurança
	// Esse método chama uma JWT e constroi as informações , leva o Username como primeira informação, consulta a data atual e pega , e seta a informação de quando vai expirar esse token e faz um calculo
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
					.setClaims(claims)
					.setSubject(userName)//leva o username como primeira informação
					.setIssuedAt(new Date(System.currentTimeMillis()))//consulta data atual, para verificar se expirou
					.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))//conta para calcular em quanto tempo ele expira(1h)
					.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();//criando a assinatura
	}

	
	//gerar o token puxando os claims formados no método anterior 
	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}

}
