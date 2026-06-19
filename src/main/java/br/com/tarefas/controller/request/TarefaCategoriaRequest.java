package br.com.tarefas.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TarefaCategoriaRequest {
	
	private Integer id;
	
	@NotBlank(message = "{tarefa.descricao.not-blank}")
	@Size(max = 50, message = "{tarefa.descricao.size}")
	private String descricao;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	

}
