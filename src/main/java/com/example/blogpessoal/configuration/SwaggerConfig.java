package com.example.blogpessoal.configuration;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration
public class SwaggerConfig {
	
	@Bean
	OpenAPI springBlogPessoalOpenAPI () {
		
		return new OpenAPI()
				.info(new Info()
						.title("Projeto Blog Pesoal da Sara Vasconcelos")
						.description("Projeto Blog Pessoal desenvolvido por Sara Vasconcelos Freitas Souza durante o Bootcamp FullStack da Generation Brasil") //descrição depois posso colocar mais coisa
						.version("v0.0.1")
						.license(new License()
								.name("PDF da Documentação")
								.url(""))// colocar o link do pdf depois 
						.contact(new Contact()
								.name("Sara Vasconcelos Freitas Souza - Meu Linkedin")//nome de quem desenvolveu ou a empresa
								.url("linkedin.com/in/sara-vasconcelos-freitas-souza")//link de alguma coisa
								.email("vasconcelossara11@gmail.com")))//email principal do projeto
								.externalDocs(new ExternalDocumentation()
										.description("Github")//link externos
										.url("https://github.com/Sara-vasconcelos"));//link do projeto - posso puxar pelo git remote -v
	}
	

	@Bean
	OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {
		
		return openApi ->{
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations()
					.forEach(operation -> {
						ApiResponses apiResponses = operation.getResponses();
						
						//mensagens para cada erro
						 apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
						 apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!")); //quando um objeto é criado
						 apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
						 apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
						 apiResponses.addApiResponse("401", createApiResponse("Acesso não Autorizado!"));
						 apiResponses.addApiResponse("403", createApiResponse("Acesso Proibido!"));
						 apiResponses.addApiResponse("404", createApiResponse("Objeto não Encontrado!"));
						 apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação "));
					}));
		};
		
	}
	
	//método para conseguir retornar a mensagem
	private ApiResponse createApiResponse(String message) {
		return new ApiResponse().description(message);
	}
}


