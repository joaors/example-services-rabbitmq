package br.com.senai.alunos.principal;

import br.com.senai.alunos.persist.Application;
import java.io.IOException;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class Main {
	
	public static void main(String[] args) throws IOException {
	    Weld weld = new Weld();
	    WeldContainer container = weld.initialize();
	    Application application = container.instance().select(Application.class).get();
	    application.run();
	    weld.shutdown();
	}	

}
