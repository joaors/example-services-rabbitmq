package br.com.senai.alunos.cdi;

import br.com.senai.alunos.persist.Aluno;
import javax.inject.Inject;

import br.com.senai.alunos.persist.AlunoRepository;
import com.google.gson.Gson;

@Message(Message.Type.UPDATE)
public class UpdateMessage implements MessageType{

	@Inject
	private AlunoRepository repository;	
	
	@Override
	public String execute(String toExecute) {
		Gson gson = new Gson();
		Aluno toAdd = gson.fromJson(toExecute, Aluno.class);
		repository.update(toAdd.getId(), toAdd);
		return gson.toJson(toAdd);
	}

}
