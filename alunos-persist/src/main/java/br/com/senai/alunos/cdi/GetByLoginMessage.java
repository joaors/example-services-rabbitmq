/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senai.alunos.cdi;

import br.com.senai.alunos.persist.Aluno;
import br.com.senai.alunos.persist.AlunoRepository;
import com.google.gson.Gson;
import java.util.Optional;
import javax.inject.Inject;

/**
 *
 * @author joaorodrigo
 */

@Message(Message.Type.GET_BY_LOGIN)
public class GetByLoginMessage implements MessageType{

    @Inject
    private AlunoRepository repository;
    
    @Override
    public String execute(String toExecute) {
        
        Gson gson = new Gson();
        Aluno toGet = gson.fromJson(toExecute, Aluno.class);
        Optional<Aluno> found = repository.getByNome(toGet.getNome());
        return gson.toJson(found.orElse(new Aluno()));        
    }
    
}
