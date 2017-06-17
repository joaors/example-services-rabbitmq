package br.com.senai.alunos.cdi;

import javax.inject.Inject;

import com.google.gson.Gson;

import br.com.senai.alunos.persist.Aluno;
import br.com.senai.alunos.persist.AlunoRepository;
import java.util.List;

@Message(Message.Type.LIST)
public class ListMessage implements MessageType{

	@Inject
	private AlunoRepository repository;
	
	@Override
	public String execute(String toExecute) {
		List<Aluno> alunos = repository.getAlunos();
		return new Gson().toJson(alunos);
	}

}
