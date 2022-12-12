package facades;

import dtos.ProduktDTO;
import dtos.UserDTO;
import entities.Produkt;
import entities.User;
import errorhandling.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class ProduktFacade {

    private static ProduktFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private ProduktFacade() {}
    

    public static ProduktFacade getProduktFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ProduktFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public Produkt getProduktByName(String name)
    {
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery query = em.createQuery("select p from Produkt p where p.navn = :name", Produkt.class);
            query.setParameter("name", name);
            System.out.println(query.getSingleResult());
            Produkt produkt = (Produkt) query.getSingleResult();
            return produkt;
        }
        catch (NoResultException nre){
            return null;
        } finally {
            em.close();
        }
    }

    public Produkt getProdukt(ProduktDTO produktdto) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            Produkt produkt = getProduktByName(produktdto.getNavn());
            if (produkt == null){
                throw new NotFoundException("Den ønskede produkt findes ikke");
            }
            return produkt;
        } finally {
            em.close();
        }
    }




}
