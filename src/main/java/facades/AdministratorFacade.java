package facades;

import dtos.PrisDTO;
import dtos.PrisProduktDTO;
import entities.Butik;
import entities.Pris;
import entities.Produkt;
import errorhandling.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
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
        Pris pris = new Pris(prisDTO.getBeløb(), butik1, produkt1);
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

    public List<PrisDTO> getPris(String produkt, String kunde){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery query = em.createQuery("select p from Pris p join Butik b on b.id = p.butik.id join User u on u.butik.id = b.id join Produkt pp on pp.id = p.produkt.id where pp.navn = :produkt and u.userName = :kunde", Pris.class);
            query.setParameter("produkt", produkt);
            query.setParameter("kunde", kunde);
            System.out.println(query.getResultList());
            List<Pris> pe = query.getResultList();
            return PrisDTO.getDtos(pe);
        } finally {
            em.close();
        }
    }

    public List<PrisProduktDTO> getAllPris(){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery query = em.createQuery("select pp.navn, p.beløb, b.navn from Pris p join Butik b on b.id = p.butik.id  join Produkt pp on pp.id = p.produkt.id", Pris.class);
            System.out.println(query.getResultList());
            List<PrisProduktDTO> pe = query.getResultList();
            return pe;
        } finally {
            em.close();
        }
    }


}
