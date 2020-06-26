package com.example.algamoney.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoney.api.event.RecursoCriadoEvent;
//esse é o cara que ouve o evento, por isso a anotação @component
@Component
//o evento q ele vai ouvir : recursocriadoevent, ou seja quando lançar o recursocriadoevento esse cara aqui vai ouvir e se ativar digamos
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent> {

	@Override
	// aqui oque ele faz, trazendo coisas do recurso criado event como response e codigo pra adicionar o header location que antes ficava na recource.
	public void onApplicationEvent(RecursoCriadoEvent recursoCriadoEvent) {
		
		HttpServletResponse response = recursoCriadoEvent.getResponse();
		Long codigo = recursoCriadoEvent.getCodigo();
		
		
		 adicionarHeaderLocation(response,codigo);
		
	}

	private void adicionarHeaderLocation( HttpServletResponse response, Long codigo) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				 .buildAndExpand(codigo).toUri();
		
		 response.setHeader("Location", uri.toASCIIString());
	}
	
	

}
