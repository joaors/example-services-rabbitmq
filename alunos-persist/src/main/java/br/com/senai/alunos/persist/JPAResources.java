package br.com.senai.alunos.persist;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAResources {
	
        @Produces @ApplicationScoped
        public EntityManagerFactory criaFactory() {
                return Persistence.createEntityManagerFactory("alunos-persist");
        }

        @Produces
        public EntityManager criaEm(EntityManagerFactory factory) {
            return factory.createEntityManager();
        }
        
        public void close(@Disposes EntityManager em) {
                em.close();
        }

}
