package br.com.tarefas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tarefas.model.Usuario;
import br.com.tarefas.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepositorio;
	
	public List<Usuario> getTodosUsuarios() {
		return usuarioRepositorio.findAll();
	}
	
	public Usuario getUsuarioPorId(Integer id) {
		return usuarioRepositorio.findById(id).orElseThrow(() -> new EntityNotFoundException());
	}

}
