package br.com.senai.alunos.persist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class AlunoRepository {

	@Inject
	private EntityManager em;
	
	public  void remove(String id) {
		Optional<Aluno> found = getById(id);
		found.ifPresent(t -> em.remove(t));
	}
	
	public void update(String id, Aluno aluno) {
            em.getTransaction().begin();
            Optional<Aluno> found = Optional.of(em.find(Aluno.class, id));
            found.ifPresent(t -> {
                    t.setCurso(aluno.getCurso());
                    t.setNome(aluno.getNome());
                    t.setSemestre(aluno.getSemestre());
                    t.setSobrenome(aluno.getSobrenome());
            });
            em.getTransaction().commit();
	}
	
	public Aluno insertAluno(Aluno aluno) {
            em.getTransaction().begin();
            aluno.setId(UUID.randomUUID().toString());
            em.persist(aluno);
            em.getTransaction().commit();
            return aluno;
	}
	
	public List<Aluno> getAlunos() {
		return em.createQuery("SELECT a FROM Aluno a", Aluno.class)
				.getResultList();
	}
	
	public Optional<Aluno> getById(String id) {
            Aluno aluno = em.find(Aluno.class, id);
            return Optional.ofNullable(aluno);
	}
        
	public Optional<Aluno> getByNome(String nome) {
            Query q = em.createQuery("select a from Aluno a where a.nome = :nome");
            q.setParameter("nome", nome);  
            try {
                return Optional.ofNullable((Aluno) q.getSingleResult());   
            } catch (Exception e) {
                return Optional.empty();
            }
	}    
}
