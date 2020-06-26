package com.example.algamoney.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {
	
	@Autowired
	public PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	// é um publicador de eventos da aplicação.
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Pessoa> listar(){
		return pessoaRepository.findAll();
	}

	@PostMapping
	//@requestbody o spring pega o json que vem do request feito e trasnforma em um obj no caso abaixo do tipo pessoa
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response ){
	
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);
		
		//aciona o evento recursocriadoevent e o listener se ativa ouvindo o recurso criado event q foi lançado aqui. O this é o obj q disparou o evento, no caso essa classe PessoaResource, então é criado um obj da situ e dados atuais dela.
		//melhora o encapsulamento, escalabilidade e manutenção do código e regra de negocio fazendo isso.
		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
		
		 return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
// @getmapping anotação de mapeação para URI pelo método GET essa, passa um código também. Isso acontece quando há uma requisição nessa URI com um código.
	@GetMapping("/{codigo}")
//@Pathvariable vai pegar o valor passado na URI dessa requisição e colocar no parametro abaixo.
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public ResponseEntity buscarPeloCodigo(@PathVariable Long codigo) {
		
		Optional pessoa = this.pessoaRepository.findById(codigo);
		
		return pessoa.isPresent()?
				ResponseEntity.ok(pessoa.get()) : ResponseEntity.notFound().build();
	}
	
	
@DeleteMapping("{codigo}")
//response que seta status Http no content.
@ResponseStatus(HttpStatus.NO_CONTENT)
@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
public void remover(@PathVariable Long codigo) {
	pessoaRepository.deleteById(codigo);
}


@PutMapping("/{codigo}")
@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa){
	
	Pessoa pessoaSalva = pessoaService.atualizar(codigo, pessoa);
			
		return ResponseEntity.ok(pessoaSalva);
	
	
}

@PutMapping("/{codigo}/ativo")
@ResponseStatus(HttpStatus.NO_CONTENT)
@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @Valid @RequestBody Boolean ativo) {
	
	pessoaService.atualizarPropriedadeAtivo(codigo, ativo);
	
}


}
	
	
