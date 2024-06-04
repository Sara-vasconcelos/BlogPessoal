package com.example.blogpessoal.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.blogpessoal.model.usuario;

public class UserDetailsImpl  implements UserDetails{//se comunica com a security

	
	//configuração de segurança
	//informar para o security os dados de acesso da api
	
	private static final long serialVersionUID = 1L;//Trás a versão da classe UserDetailsImpl , e assim ela libera os metodos abaixo 
	
	private String userName; //atributo para tratar o usuario que está tentando fazer o login 
	private String password; //atributo da senha , para informar para a security a senha 
	private List<GrantedAuthority> authorities; //GrantedAuthority: trata todas as informações de permissão da api 
	//authorities : trás uma lista de permissões
	
	
	public UserDetailsImpl() {//metodo usado quando o usuario já está logado , então ele não precisará passar novamente a senha
			
	}
	
	public UserDetailsImpl(usuario user) {
		this.userName = user.getUsuario(); //informando qual metodo cada um irá receber 
		this.password = user.getSenha();
		
	}
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {//trás quais são as autorização de acesso
		// TODO Auto-generated method stub
		return authorities;
	}

	
	

	@Override
	public String getPassword() {//retorna a senha 
		// TODO Auto-generated method stub
		return password; //onde guarda a senha 
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;//recebe o nome
	}

	// os metodos estão como true para que libere o acesso , caso fosse false , o mesmo não teria acesso
	@Override
	public boolean isAccountNonExpired() {//se a conta não estiver expirada , ele autoriza a passagem do usuario- true 
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		//se a conta não estiver bloqueada ele acessa - true
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		//se a credencial(token) não estiver expirada ele acessa - true
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		//se o usuario estiver habilitado ele consegue acessar  - true
		// TODO Auto-generated method stub
		return true;
	}

}
