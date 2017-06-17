package br.com.senai.alunos.cdi;

import javax.inject.Inject;

import com.google.gson.Gson;

import br.com.senai.alunos.persist.Aluno;
import br.com.senai.alunos.persist.AlunoRepository;

@Message(Message.Type.INSERT)
public class InsertMessage implements MessageType{

	@Inject
	private AlunoRepository repository;
	
	@Override
	public String execute(String toExecute) {
		Gson gson = new Gson();
		Aluno toAdd = gson.fromJson(toExecute, Aluno.class);
		repository.insertAluno(toAdd);
		return gson.toJson(toAdd);
	}

}
