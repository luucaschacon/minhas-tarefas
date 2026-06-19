package br.com.tarefas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefas.controller.request.TarefaRequest;
import br.com.tarefas.controller.response.TarefaResponse;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.services.TarefaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {
	
	@Autowired
	private TarefaService service;
	
	@Autowired
	private ModelMapper mapper;
	
	@GetMapping
	public List<TarefaResponse> todasTarefas(@RequestParam Map<String, String> parametros) {
		List<Tarefa> tarefas = new ArrayList<>();
		
		if (parametros.isEmpty()) {
			tarefas = service.getTodasTarefas();
		} else {
			String descricao = parametros.get("descricao");
			tarefas = service.getTarefasPorDescricao(descricao);
		}
		
		List<TarefaResponse> tarefasResp = tarefas
				.stream()
				.map(tarefa -> mapper.map(tarefa, TarefaResponse.class))
				.collect(Collectors.toList());
		
		return tarefasResp; 
	}
	
	@GetMapping("/{id}")
	public EntityModel<TarefaResponse> umaTarefa(@PathVariable Integer id) {
		Tarefa tarefa = service.getTarefaPorId(id);
		TarefaResponse tarefaResp = mapper.map(tarefa, TarefaResponse.class);
		
		EntityModel<TarefaResponse> tarefaModel = EntityModel.of(tarefaResp,
				linkTo(methodOn(TarefaController.class).umaTarefa(id)).withSelfRel(),
				linkTo(methodOn(TarefaController.class).todasTarefas(new HashMap<>())).withRel("tarefas"),
				linkTo(methodOn(TarefaCategoriaController.class).umaCategoria(tarefaResp.getCategoriaId())).withRel("categoria"),
				linkTo(methodOn(UsuarioController.class).umUsuario(tarefaResp.getUsuarioId())).withRel("usuario"));
		
		return tarefaModel;
	}
	
	@PostMapping
	public TarefaResponse salvarTarefa(@Valid @RequestBody TarefaRequest tarefaReq) {
		
		Tarefa tarefa = mapper.map(tarefaReq, Tarefa.class);
		return mapper.map(service.salvarTarefa(tarefa), TarefaResponse.class);
	}
	
	@DeleteMapping("/{id}")
	public void excluirTarefa(@PathVariable Integer id) {
		service.deleteById(id);
	}

}
