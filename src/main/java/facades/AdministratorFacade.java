package facades;

import dtos.*;
import entities.Butik;
import entities.Pris;
import entities.Produkt;
import entities.User;
import errorhandling.NotFoundException;

import javax.enterprise.inject.Typed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class AdministratorFacade {

    private static AdministratorFacade instance;
    private static ButikFacade butikFacade;
    private static ProduktFacade produktFacade;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private AdministratorFacade() {}
    

    public static AdministratorFacade getAdministratorFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AdministratorFacade();
            butikFacade = ButikFacade.getButikFacade(emf);
            produktFacade = ProduktFacade.getProduktFacade(emf);
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    

    public PrisDTO createPris(PrisDTO prisDTO) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        Butik butik1 = butikFacade.getOrCreateButik(prisDTO.getButikDTO());
        Produkt produkt1 =produktFacade.getProdukt(prisDTO.getProduktDTO());
        Pris pris = new Pris(prisDTO.getId(),prisDTO.getBeløb(), butik1, produkt1);
        try{
        em.getTransaction().begin();
        em.persist(pris);
        butik1.addPriser(pris);
        produkt1.addPriser(pris);
        em.merge(pris);
        em.getTransaction().commit();
        return new PrisDTO(pris);
        }
        finally {
            em.close();
        }
    }



    public List<PrisProduktDTO> getAllPris(){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery query = em.createQuery("select p.id, pp.navn, p.beløb, b.navn from Pris p join Butik b on b.id = p.butik.id  join Produkt pp on pp.id = p.produkt.id", Pris.class);
            System.out.println(query.getResultList());
            List<PrisProduktDTO> pe = query.getResultList();
            return pe;
        } finally {
            em.close();
        }
    }


    public PrisDTO editPris(PrisDTO prisDTO) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        Pris pris = em.find(Pris.class, prisDTO.getId());
        Butik butik1 = butikFacade.getOrCreateButik(prisDTO.getButikDTO());
        Produkt produkt1 =produktFacade.getProdukt(prisDTO.getProduktDTO());
        try{
            if(pris == null){
                throw new NotFoundException("Kunne ikke finde prisen med det indtastede id");
            }
            em.getTransaction().begin();
            pris.setBeløb(prisDTO.getBeløb());
            pris.setButik(butik1);
            pris.setProdukt(produkt1);
            em.getTransaction().commit();
            return new PrisDTO(pris);
        }
        finally {
            em.close();
        }
    }

    public String deleteKunde(String kunde) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            TypedQuery query = em.createQuery("delete from User u  where u.userName = :kunde", User.class);
            query.setParameter("kunde", kunde);
            query.executeUpdate();
            em.getTransaction().commit();
            String res = "kundeprofil "+kunde +" slettet";
            return res;
        } finally {
            em.close();
        }
        }

    public List<UserProduktDTO> getAllRequests(){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery query = em.createQuery("select u.userName, u.butik.navn, p.navn from User u join u.produkter p join Pris pp on pp.produkt.id = p.id", User.class);
            List<UserProduktDTO> upp = query.getResultList();
            return upp;
        } finally {
            em.close();
        }
    }
}
