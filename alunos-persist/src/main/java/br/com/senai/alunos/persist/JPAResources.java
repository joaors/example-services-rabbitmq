package br.com.senai.alunos.persist;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAResources {
	
	private static final String DB_HOST = System.getenv("DB_HOST");
	
    @Produces @ApplicationScoped
    public EntityManagerFactory criaFactory() {     	
    	
    	Map<String, String> persistenceMap = new HashMap<String, String>();
    	persistenceMap.put("javax.persistence.jdbc.url", "jdbc:postgresql://"+DB_HOST+":5432/postgres");
    	persistenceMap.put("javax.persistence.jdbc.user", "postgres");
    	persistenceMap.put("javax.persistence.jdbc.password", "postgres");
    	persistenceMap.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
    	persistenceMap.put("hibernate.hbm2ddl.auto", "update");   
    	persistenceMap.put("hibernate.show_sql", "true");
    	persistenceMap.put("hibernate.format_sql", "true");
    	
        return Persistence.createEntityManagerFactory("alunos-persist", persistenceMap);
    }

    @Produces
    public EntityManager criaEm(EntityManagerFactory factory) {
        return factory.createEntityManager();
    }
    
    public void close(@Disposes EntityManager em) {
            em.close();
    }

}
