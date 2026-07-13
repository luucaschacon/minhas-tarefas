package br.com.tarefas.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.tarefas.model.Role;
import br.com.tarefas.model.Usuario;
import br.com.tarefas.repository.RoleRepository;
import br.com.tarefas.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepositorio;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	public List<Usuario> getTodosUsuarios() {
		return usuarioRepositorio.findAll();
	}
	
	public Usuario getUsuarioPorId(Integer id) {
		return usuarioRepositorio.findById(id).orElseThrow(() -> new EntityNotFoundException());
	}

	public Usuario salvar(Usuario usuario) {
		Set<Role> roles = getRoles(usuario);
		usuario.setRoles(roles);
		usuario.setSenha(encoder.encode(usuario.getSenha()));
		return usuarioRepositorio.save(usuario);
	}
	
	public Usuario atualizar(Integer id, Usuario usuario) {
		if (!usuarioRepositorio.existsById(id))
			throw new EntityNotFoundException();
		
		usuario.setId(id);
		
		return salvar(usuario);
	}
	
	public void deleteById(Integer id) {
		usuarioRepositorio.deleteById(id);
	}
	
	private Set<Role> getRoles(Usuario usuario) {
		Set<Role> rolesBanco = usuario.getRoles()
				.stream()
				.map(role -> roleRepository.findByName(role.getName()))
				.collect(Collectors.toSet());
		
		return rolesBanco;
	}

}
