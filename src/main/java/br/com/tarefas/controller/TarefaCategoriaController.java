package br.com.tarefas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefas.controller.request.TarefaCategoriaRequest;
import br.com.tarefas.controller.response.TarefaCategoriaResponse;
import br.com.tarefas.controller.response.TarefaResponse;
import br.com.tarefas.model.TarefaCategoria;
import br.com.tarefas.services.TarefaCategoriaService;

@RestController
@RequestMapping("/categoria")
public class TarefaCategoriaController {
	
	@Autowired
	private TarefaCategoriaService service;
	
	@Autowired
	private ModelMapper mapper;
	
	@GetMapping
	public List<TarefaCategoriaResponse> todasCategorias() {
		List<TarefaCategoria> categorias = service.getTodasCategorias();
		return categorias
				.stream()
				.map(categoria -> mapper.map(categoria, TarefaCategoriaResponse.class))
				.collect(Collectors.toList());
	}
	
	@GetMapping("/{id}")
	public EntityModel<TarefaResponse> umaCategoria(@PathVariable Integer id) {
		TarefaCategoria tarefa = service.getTarefaCategoriaPorId(id);
		TarefaResponse tarefaResp = mapper.map(tarefa, TarefaResponse.class);
		
		EntityModel<TarefaResponse> tarefaModel = EntityModel.of(tarefaResp,
				linkTo(methodOn(TarefaController.class).umaTarefa(id)).withSelfRel(),
				linkTo(methodOn(TarefaController.class).todasTarefas(new HashMap<>())).withRel("tarefas"));
		
		return tarefaModel;
	}
	
	@PostMapping
	public TarefaCategoriaResponse salvarCategoria(@RequestBody TarefaCategoriaRequest categoriaReq) {
		TarefaCategoria categoria = mapper.map(categoriaReq, TarefaCategoria.class);
		return mapper.map(service.salvar(categoria), TarefaCategoriaResponse.class);
	}
	
	@DeleteMapping("/{id}")
	public void excluirTarefa(@PathVariable Integer id) {
		service.deleteById(id);
	}

}
