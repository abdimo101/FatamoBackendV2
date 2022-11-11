package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PrisDTO;
import errorhandling.NotFoundException;
import facades.AdministratorFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/admin")
public class AdministratorResource {

    private final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private final AdministratorFacade FACADE =  AdministratorFacade.getAdministratorFacade(EMF);
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces("text/plain")
    public String hello(){
        return "hello world!";
    }


    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public String createPris(String pris) throws NotFoundException {
        PrisDTO prisDTO = GSON.fromJson(pris, PrisDTO.class);
        PrisDTO nyprisdto = FACADE.createPris(prisDTO);
        return GSON.toJson(nyprisdto);
    }



    @Path("produkt/{produkt_navn}/{kunde_navn}")
    @RolesAllowed("admin")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPriserByProdukt(@PathParam("produkt_navn")String produkt_navn, @PathParam("kunde_navn")String kunde_navn){
        return GSON.toJson(FACADE.getPris(produkt_navn, kunde_navn));
    }

    @Path("produkt/all")
    @RolesAllowed("admin")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPriser(){
        return GSON.toJson(FACADE.getAllPris());
    }



}
