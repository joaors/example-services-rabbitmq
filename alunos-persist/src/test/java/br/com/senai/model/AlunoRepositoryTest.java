package br.com.senai.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.jboss.weld.environment.se.Weld;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.senai.alunos.persist.Aluno;
import br.com.senai.alunos.persist.AlunoRepository;

public class AlunoRepositoryTest {
	
	private static Weld WELD;
	private AlunoRepository repository;
	
	@BeforeClass
	public static void setupBeforeClass() throws Exception{
		WELD = new Weld();
	}
	
	@AfterClass
	public static void setupAfterClass() throws Exception {
		WELD.shutdown();
	}
	
	@Before
	public void setup() throws Exception{
		repository = WELD.initialize().instance().select(AlunoRepository.class).get();
	}

	@Test
	public void testInsertAluno() {
		Aluno aluno = new Aluno();
		aluno.setCurso("curso1");
		aluno.setNome("Teste nome");
		aluno.setSemestre(3L);
		aluno.setSobrenome("Sobrenome Teste");
		repository.insertAluno(aluno);
		assertNotNull(aluno.getId());
	}
	
	@Test
	public void testJson() {
		String json = "{\"firstName\":\"John\",\"lastName\":\"Smith\",\"age\":25,"+
						     "\"address\":{"+
						         "\"streetAddress\":\"21 2nd Street\","+
						         "\"city\":\"New York\","+
						         "\"state\":\"NY\","+
						         "\"postalCode\":\"10021\""+
						     "},"+
						     "\"phoneNumber\":["+
						         "{\"type\":\"home\",\"number\":\"212 555-1234\"},"+
						         "{\"type\":\"fax\",\"number\":\"646 555-4567\"}"+
						     "]"+
						 "}";
		try(JsonReader jsonReader = Json.createReader(new StringReader(json))) {
			JsonObject object = jsonReader.readObject();
			System.out.println(object.toString());
			assertEquals(object.toString(), json);			
		}

	}

}
