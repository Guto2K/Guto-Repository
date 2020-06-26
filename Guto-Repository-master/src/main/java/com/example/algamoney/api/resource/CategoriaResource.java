package com.example.algamoney.api.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;


//Um Controller é responsável tanto por receber requisições como por enviar a resposta ao usuário, algo bem parecido com o Servlet do JSP.
// recebe requisições, pois é um controlador rest apenas e n cria ou redireciona para views, retorno ja vai ser convertido para json por exemplo. n precisando colocar @controller @RequestBody em todos métodos
@RestController
//mapeamento da requisição
@RequestMapping("/categorias")
public class CategoriaResource {
	
	//injetável, n precisando instanciar para usar com new CategoriaRepository(), afinal n é possivel instanciar;
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
		
	}
	
	
	@PostMapping
	//anotando dentro do parametro com Requestbody o spring consegue pegar o json e transformar em objeto do parametro que esta na frente
	// Esse : HttpServletResponse response no parametro serve para mandar de volta pra requisição no body dela a categoria criada.
	//@ valid significa que queremos que o spring valide a categoria q esta recebendo.
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		
		 Categoria categoriaSalva = categoriaRepository.save(categoria);
		 
		 publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
		 
		/* URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				 .buildAndExpand(categoriaSalva.getCodigo()).toUri();
		 response.setHeader("Location", uri.toASCIIString());*/ // guarda na uri a localização mapping da uri atual, é um metodo fromCurrentRequestUri() da classe : ServletUriComponentsBuilder. ele concatena path codigo da uri e adiciona o codigo com o getcodigo da categoria na URI.

		 //responseEntity é uma entidade, classe de resposta de requisição, geralmente tem um tipo dela em metodos dentro de chaves assim :<Categoria>
		 return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}
	
	@GetMapping("/{codigo}")
	//para transformar o /codigo (path da uri) da getmapping da uri em um parametro e pegar o valor dele usa o @ Pathvariable
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity buscarPeloCodigo(@PathVariable Long codigo) {
		
	Optional categoria = this.categoriaRepository.findById(codigo);
	
			return categoria.isPresent()? 
					ResponseEntity.ok(categoria.get()) : ResponseEntity.notFound().build();	//.build é preciso colocar pra ele gerar um responseEntity de resposta.	
	}
	
	

}
