package br.com.senai.boundary;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.com.senai.service.JWTUtil;

@Path("/autenticacao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {
	
	@POST	
	@Path("login")
	public Response autenticar(JsonObject login) {
		if(login.getString("usuario").equals("admin")) {
			String token = JWTUtil.createToken(login.getString("usuario"));
			JsonObject json = Json.createObjectBuilder().add("token", token).build();
			return Response.ok(json.toString()).build();	
		}
		return Response.status(Status.UNAUTHORIZED).build();

	}	

}
