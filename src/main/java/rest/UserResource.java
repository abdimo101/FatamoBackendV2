package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ProduktDTO;
import dtos.UserDTO;
import dtos.UserProduktDTO;
import entities.User;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import errorhandling.InvalidInputException;
import errorhandling.NotFoundException;
import facades.UserFacade;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;


@Path("user")
public class UserResource {
    
    private final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private final UserFacade FACADE =  UserFacade.getUserFacade(EMF);
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("username")
    //@RolesAllowed("user")
    public String getUsername() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return thisuser ;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @Path("kunde/{kunde_navn}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPriserByKunde(@PathParam("kunde_navn")String kunde_navn){
        return GSON.toJson(FACADE.getPrisByKunde(kunde_navn));
    }

    @Path("butik/{navn}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getKunderByButik(@PathParam("navn")String navn){
        return GSON.toJson(FACADE.getKunderByButik(navn));
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public String createKunde(String user) throws AuthenticationException, InvalidInputException {
        UserDTO udto = GSON.fromJson(user, UserDTO.class);
        UserDTO newudto = FACADE.createKunde(udto);
        return GSON.toJson(newudto);
    }
    @Path("request")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public String sendRequest(String userprodukt) throws NotFoundException {
        UserProduktDTO updto = GSON.fromJson(userprodukt, UserProduktDTO.class);
        System.out.println("userprodukt: " + userprodukt);
        UserDTO newupdto = FACADE.sendRequest(updto.getUserDTO(), updto.getProduktDTO());
        return GSON.toJson(newupdto);
    }
}