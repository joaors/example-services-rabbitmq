package br.com.senai.boundary;

import java.util.Objects;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

import br.com.senai.model.Aluno;
import br.com.senai.service.JWTUtil;
import br.com.senai.service.RabbitMQService;

@Path("/autenticacao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {
	
	private static final String GET_BY_LOGIN = "GET_BY_LOGIN";	
	
	@Inject
	RabbitMQService rabbit;
	
	@POST	
	@Path("login")
	public Response autenticar(JsonObject login) {
		String user = login.getString("usuario");
		
		/* se for admin tudo certo */
		if (user.equals("admin")) {
			String token = JWTUtil.createToken(login.getString("usuario"));
			JsonObject json = Json.createObjectBuilder().add("token", token).build();
			return Response.ok(json.toString()).build();			
		}
		
		
		JsonObject toAuthorize = Json.createObjectBuilder().add("nome", user).build();
		try {
			String authorized = rabbit.send(toAuthorize.toString(), GET_BY_LOGIN, RabbitMQService.AUTHORIZATION);
			if (Objects.nonNull(authorized)) {
				Gson gson = new Gson();
				Aluno aluno = gson.fromJson(authorized, Aluno.class);
				if (Objects.nonNull(aluno.getId())) {
					String token = JWTUtil.createToken(login.getString("usuario"));
					JsonObject json = Json.createObjectBuilder().add("token", token).build();
					return Response.ok(json.toString()).build();
				}
			}
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.UNAUTHORIZED).build();
		}
		

	}	

}
