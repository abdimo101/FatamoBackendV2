package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PrisDTO;
import dtos.UserDTO;
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


    @Path("produkt/all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPriser(){
        return GSON.toJson(FACADE.getAllPris());
    }

    @Path("pris/edit/{id}")
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public String editPris(@PathParam("id")int id, String pris) throws NotFoundException {
        PrisDTO prisDTO =  GSON.fromJson(pris, PrisDTO.class);
        prisDTO.setId(id);
        return GSON.toJson(FACADE.editPris(prisDTO));
    }

    @Path("kunde/delete/{kunde}")
    @DELETE
    @Consumes("application/json")
    @Produces("application/json")
    public String deleteKunde(@PathParam("kunde") String kunde) throws NotFoundException {
        return GSON.toJson(FACADE.deleteKunde(kunde));
    }

    @Path("request/all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllRequests(){
        return GSON.toJson(FACADE.getAllRequests());
    }


}
