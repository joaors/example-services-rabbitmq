package br.com.senai.boundary;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.senai.model.Aluno;
import br.com.senai.model.AlunoRepository;
import br.com.senai.service.RabbitMQService;

@Path("alunos")
public class AlunoResource {	
	
	@Inject
	AlunoRepository repository;
	
	@Inject
	RabbitMQService rabbit;
	
	private static final String INSERT = "INSERT";
	private static final String UPDATE = "UPDATE";
	private static final String DELETE = "DELETE";
	private static final String LIST = "LIST";
	private static final String RETRIEVE = "RETRIEVE";	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Aluno> retornaAlunos() {
		try {
			String list = rabbit.send("nada", LIST);
			Type listType = new TypeToken<ArrayList<Aluno>>(){}.getType();
			List<Aluno> alunos = new Gson().fromJson(list, listType);
			return alunos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e.getLocalizedMessage()!= null ? e.getLocalizedMessage() : "Erro ao buscar alunos", 500);
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Aluno inserirAluno(JsonObject aluno) {
		try {
			String added = rabbit.send(aluno.toString(), INSERT);
			Gson gson = new Gson();
			Aluno toAdd = gson.fromJson(added, Aluno.class);		
			return toAdd;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e.getLocalizedMessage()!= null ? e.getLocalizedMessage() : "Erro ao inserir aluno", 500);
		}

	}
	
	@DELETE
	@Path("{id}")
	public void delete(@PathParam(value = "id") String id) {
		try {
			JsonObject toDelete = Json.createObjectBuilder().add("id", id).build();
			rabbit.send(toDelete.toString(), DELETE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e.getMessage()!= null ? e.getMessage() : "Erro ao deletar aluno", 500);
		}
	}
	
	@GET
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Aluno retrieve(@PathParam(value = "id") String id) {
		try {
			JsonObject toRetrieve = Json.createObjectBuilder().add("id", id).build();
			String alunoMessage = rabbit.send(toRetrieve.toString(), RETRIEVE);
			Gson gson = new Gson();
			Aluno aluno = gson.fromJson(alunoMessage, Aluno.class);
			return aluno;			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e.getMessage()!= null ? e.getMessage() : "Erro ao deletar aluno", 500);
		}
	}	
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Aluno update(@PathParam(value = "id") String id, JsonObject aluno) {
		try {
			String updated = rabbit.send(aluno.toString(), UPDATE);
			Gson gson = new Gson();
			Aluno alunoUpdated = gson.fromJson(updated, Aluno.class);
			return alunoUpdated;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e.getLocalizedMessage()!= null ? e.getLocalizedMessage() : "Erro ao inserir aluno", 500);
		}
	}
	
}
