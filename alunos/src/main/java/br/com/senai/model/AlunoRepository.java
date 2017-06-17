package br.com.senai.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AlunoRepository {

	@PersistenceContext
	private EntityManager em;
	
	public  void remove(String id) {
		Optional<Aluno> found = getById(id);
		found.ifPresent(t -> em.remove(t));
	}
	
	public void update(String id, Aluno aluno) {
		Optional<Aluno> found = Optional.of(em.find(Aluno.class, id));
		found.ifPresent(t -> {
			t.setCurso(aluno.getCurso());
			t.setNome(aluno.getNome());
			t.setSemestre(aluno.getSemestre());
			t.setSobrenome(aluno.getSobrenome());
		});
	}
	
	public Aluno insertAluno(Aluno aluno) {
		aluno.setId(UUID.randomUUID().toString());
		em.persist(aluno);
		return aluno;
	}
	
	public List<Aluno> getAlunos() {
		return em.createQuery("SELECT a FROM Aluno a", Aluno.class)
				.getResultList();
	}
	
	public Optional<Aluno> getById(String id) {
		return Optional.of(em.find(Aluno.class, id));
	}
}
