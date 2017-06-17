package br.com.senai.alunos.persist;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

/**
 * Entity implementation class for Entity: Aluno
 *
 */
@Entity
public class Aluno implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private String nome;
	
	private String sobrenome;
	
	private String curso;
	
	private Long semestre;
	
	public Aluno(){}

	public Aluno(String id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

        public void beforeInsert() {
            this.id = UUID.randomUUID().toString();
        }
        
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aluno other = (Aluno) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getSemestre() {
		return semestre;
	}

	public void setSemestre(Long semestre) {
		this.semestre = semestre;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	@Override
	public String toString() {
		return "Aluno [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ", curso=" + curso + ", semestre="
				+ semestre + "]";
	}

   
}
