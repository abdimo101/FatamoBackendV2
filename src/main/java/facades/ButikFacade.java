package facades;

import dtos.ButikDTO;
import entities.Butik;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class ButikFacade {

    private static ButikFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private ButikFacade() {}
    

    public static ButikFacade getButikFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ButikFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public Butik getButikByName(String name)
    {
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery query = em.createQuery("select b from Butik b where b.navn = :name", Butik.class);
            query.setParameter("name", name);
            System.out.println(query.getSingleResult());
            Butik butik = (Butik) query.getSingleResult();
            return butik;
        }
        catch (NoResultException nre){
            return null;
        } finally {
            em.close();
        }
    }

    public Butik getOrCreateButik(ButikDTO butikdto){
        EntityManager em = emf.createEntityManager();
        try {
            Butik butik = getButikByName(butikdto.getNavn());
            if (butik == null){
                Butik nyButik = new Butik(butikdto.getNavn());
                em.getTransaction().begin();
                em.persist(nyButik);
                butik = nyButik;
                em.getTransaction().commit();
            }
            return butik;
        } finally {
            em.close();
        }
    }


}
