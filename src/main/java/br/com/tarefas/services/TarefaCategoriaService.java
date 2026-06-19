package br.com.tarefas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tarefas.model.TarefaCategoria;
import br.com.tarefas.repository.TarefaCategoriaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TarefaCategoriaService {
	
	@Autowired
	private TarefaCategoriaRepository repositorio;
	
	public List<TarefaCategoria> getTodasCategorias() {
		return repositorio.findAll();
	}
	
	public List<TarefaCategoria> getTarefasPorDescricao(String descricao) {
		return repositorio.findByNomeLike("%" + descricao + "%");
	}
	
	public TarefaCategoria getTarefaCategoriaPorId(Integer id) {
		return repositorio.findById(id).orElseThrow(() -> new EntityNotFoundException());
	}
	
	public TarefaCategoria salvar(TarefaCategoria tarefaCategoria) {
		return repositorio.save(tarefaCategoria);
	}
	
	public void deleteById(Integer id) {
		repositorio.deleteById(id);
	}

	// CRIAR O METODO DE CANCELAR

}
