package com.example.algamoney.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

// essa anotação diz q é uma aplicação com springboot, reconhecendo todas as classes inicializadas depois dela que tiverem anotações como @ configuration, @Controller, @component, etc... e por exemplo scanear ela as reconhecendo-as para N funções.
@SpringBootApplication
@EnableConfigurationProperties(AlgamoneyApiProperty.class)
public class AlgamoneyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlgamoneyApiApplication.class, args);
	}
	
	/* @PostConstruct
	    public void init(){
	        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
	    }*/

}
