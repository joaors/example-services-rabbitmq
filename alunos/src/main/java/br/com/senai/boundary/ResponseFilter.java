package br.com.senai.boundary;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class ResponseFilter implements ContainerResponseFilter{
    
    private final static Logger LOG = Logger.getLogger(ResponseFilter.class.getName());

    @Override
    public void filter( ContainerRequestContext requestCtx, ContainerResponseContext responseCtx ) throws IOException {
        LOG.info( "Filtering REST Response" );
        responseCtx.getHeaders().add( "Access-Control-Allow-Origin", "*" );    // Limitar aqui os IPs com acesso
        responseCtx.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
        responseCtx.getHeaders().add( "Access-Control-Allow-Headers", "Content-Type" );
        responseCtx.getHeaders().add( "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT" );
    }
    
}
