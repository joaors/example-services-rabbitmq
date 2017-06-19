package br.com.senai.boundary;

import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import br.com.senai.service.JWTUtil;

@Provider
@PreMatching
public class RequestFilter  implements ContainerRequestFilter{
	
	private final static String OPTIONS = "OPTIONS";
		
    @Override
    public void filter( ContainerRequestContext requestCtx ) {
 
        String path = requestCtx.getUriInfo().getPath();
        
        // IMPORTANT!!! Primeiro, Verificar se é requisição OPTIONS antes de validar os headers (CORS)
        if ( requestCtx.getRequest().getMethod().equals( OPTIONS ) ) {
            requestCtx.abortWith(Response.ok().build());
            return;
        }

        if (!path.startsWith( "/autenticacao/" )) {
            String authToken = requestCtx.getHeaderString("authorization");
    		try {
    			Map<String, Object> map = JWTUtil.decode(authToken);
    			System.out.println(map.get("user"));
    		} catch (Exception e) {        
    			requestCtx.abortWith( createUnauthorized() );
    		}    		
        }

    }
    
    private Response createUnauthorized() {
    	return Response.status(Status.UNAUTHORIZED).build();
    }	

}
