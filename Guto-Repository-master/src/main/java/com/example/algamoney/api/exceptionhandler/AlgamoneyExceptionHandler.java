package com.example.algamoney.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.flywaydb.core.api.android.ContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//precisa ser um controlador para poder capturar as exceções da aplicação, e ele é advice pq ele observa toda a aplicação. Virando então um component do spring.
@ControllerAdvice
// extends da classe abaixo trata excessões capturando de respostas das entidades e dando response com resolução/tratando da exceção
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private  MessageSource messageSource;
	//Ele é uma forma de garantir que você está sobrescrevendo um método e não criando um novo.
	@Override
	//erro para badrequest geralmente em que não foi possivel ler a request por conta de campos excedentes ou errados.
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		
		String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
		
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		
		
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	// tratando argumentos não validos como notnull ou tamanho maximo ou minimo do campo.
	protected ResponseEntity<Object>handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
		
		List<Erro> erros = criarListaDeErros(ex.getBindingResult());
		
		
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
		
	}
	
	
	/*@ExceptionHandler({EmptyResultDataAccessException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void handleEmptyResultDataAccessException(RuntimeException ex, EmptyResultDataAccessException e, WebRequest request){
		
			
	}*/
	
	// essa trata quando não acha o valor passado para busca, delete, etc..
	//essa anotação transforma o metodo abaixo em uma handle pra tratar exceções, dentro das chaves vc coloca os erros que ela trata, separados por virgula, o nome do metodo é sempre handle seguido do erro
			@ExceptionHandler({EmptyResultDataAccessException.class})
			//@ResponseStatus(HttpStatus.NOT_FOUND)
			public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request/*,headers :(aparentemente a versao do video n aceita esse header aqui, mas a versao atual ja aceita)*/ ){
				
				String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
				String mensagemDesenvolvedor = ex.toString();/*ela n tem get.cause(), pq n tem uma causa, ela já é a exceção pronta */
				
				List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
				
				return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
				
			}
			
			
			@ExceptionHandler({DataIntegrityViolationException.class})
			public ResponseEntity<Object> handleDataIntegrityViolationException (DataIntegrityViolationException ex, WebRequest request) {
				
				String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null, LocaleContextHolder.getLocale());
				String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
				
				List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
				
				return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
				
			}
	

	
	
	
		
	
	//binding result tem a lista de todos os erros
	private List<Erro> criarListaDeErros(BindingResult bindingResult){
		List<Erro> erros = new ArrayList<>();
		//ele da no get todos os erros capturados na requisição
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
		String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()) ;
		String mensagemDesenvolvedor = fieldError.toString();
		
		erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		}
		return erros;
	}
	
	
	
	public static class Erro {
		
		private String mensagemUsuario;
		private String mensagemDesenvolvedor;
		
		
		
		
		

		public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
			this.mensagemUsuario = mensagemUsuario;
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}

		public String getMensagemUsuario() {
			return mensagemUsuario;
		}


		public String getMensagemDesenvolvedor() {
			return mensagemDesenvolvedor;
		}

		
		
	
	}

}
