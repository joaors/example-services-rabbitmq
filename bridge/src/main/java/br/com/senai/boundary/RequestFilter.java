package br.com.senai.boundary;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import br.com.senai.service.JWTUtil;
import br.com.senai.service.RabbitMQService;

@Provider
@PreMatching
public class RequestFilter  implements ContainerRequestFilter{
	
	private final static String OPTIONS = "OPTIONS";
	
	@Inject
	RabbitMQService rabbitService;
		
    @Override
    public void filter( ContainerRequestContext requestCtx ) {
 
        String path = requestCtx.getUriInfo().getPath();
        
        // IMPORTANT!!! Primeiro, Verificar se é requisição OPTIONS antes de validar os headers (CORS)
        if ( requestCtx.getRequest().getMethod().equals( OPTIONS ) ) {
            requestCtx.abortWith(Response.ok().build());
            return;
        }
        try {
			rabbitService.sendAsincrono(path);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

        if (!path.startsWith( "/autenticacao/" )) {
            String authToken = requestCtx.getHeaderString("authorization");
    		try {
    			Map<String, Object> map = JWTUtil.decode(authToken);
    			rabbitService.sendAsincrono((String) map.get("user"));
    		} catch (Exception e) {        
    			requestCtx.abortWith( createUnauthorized() );
    		}    		
        }

    }
    
    private Response createUnauthorized() {
    	return Response.status(Status.UNAUTHORIZED).build();
    }	

}
