/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.UserDTO;
import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import errorhandling.InvalidInputException;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import java.util.ArrayList;
import java.util.List;


public class Populator {
    public static void populate() throws InvalidInputException, AuthenticationException {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        UserFacade uf = UserFacade.getUserFacade(emf);
        User user = new User("testkunde1", "123");
        Butik butik = new Butik("Home");
        butik.addUser(user);
        uf.createUser(new UserDTO(user));

        /*
        fe.create(new RenameMeDTO(new RenameMe("First 1", "Last 1")));
        fe.create(new RenameMeDTO(new RenameMe("First 2", "Last 2")));
        fe.create(new RenameMeDTO(new RenameMe("First 3", "Last 3")));
         */

        EntityManager em = emf.createEntityManager();
       // em.getTransaction().begin();
       // User user = new User("kunde1", "kunde1kode");
       // Role role = new Role("user");
       // user.addRole(role);
       // em.persist(role);
       // em.persist(user);

       // em.getTransaction().commit();
       // em.close();
    }

    public static void main(String[] args) throws InvalidInputException, AuthenticationException {
        populate();
    }
}
