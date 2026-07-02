package br.com.tarefas.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefas.controller.assembler.UsuarioModelAssembler;
import br.com.tarefas.controller.response.UsuarioResponse;
import br.com.tarefas.model.Usuario;
import br.com.tarefas.services.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private UsuarioModelAssembler assembler;
	
	@GetMapping
	public CollectionModel<EntityModel<UsuarioResponse>> todosUsuarios() {
		List<Usuario> usuarios = usuarioService.getTodosUsuarios();
		List<EntityModel<UsuarioResponse>> usuariosModel = usuarios
				.stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(usuariosModel, WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class).todosUsuarios())
				.withSelfRel()); 
	}
	
	@GetMapping("/{id}")
	public UsuarioResponse umUsuario(@PathVariable Integer id) {
		Usuario usuario = usuarioService.getUsuarioPorId(id);
		UsuarioResponse usuarioResponse = mapper.map(usuario, UsuarioResponse.class);
		return usuarioResponse;
	}

}
