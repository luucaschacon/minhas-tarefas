package br.com.tarefas.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefas.controller.response.TarefaResponse;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.services.TarefaService;
import jakarta.validation.Valid;

@RestController
public class TarefaController {
	
	@Autowired
	private TarefaService service;
	
	@Autowired
	private ModelMapper mapper;
	
	@GetMapping("/tarefa")
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
	
	@GetMapping("/tarefa/{id}")
	public TarefaResponse umaTarefa(@PathVariable Integer id) {
		Tarefa tarefa = service.getTarefaPorId(id);
		TarefaResponse tarefaResp = mapper.map(tarefa, TarefaResponse.class);
		return tarefaResp;
	}
	
	@PostMapping("/tarefa")
	public TarefaResponse salvarTarefa(@Valid @RequestBody Tarefa tarefa) {
		return mapper.map(service.salvarTarefa(tarefa), TarefaResponse.class);
	}
	
	@DeleteMapping("/tarefa/{id}")
	public void excluirTarefa(@PathVariable Integer id) {
		service.deleteById(id);
	}

}
