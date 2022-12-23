/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;


import entities.Produkt;

import java.util.ArrayList;
import java.util.List;

public class ProduktDTO {
    private String navn;
    private List<UserDTO> users;
    public ProduktDTO(String navn) {
        this.navn = navn;
    }

    public ProduktDTO(Produkt produkt) {
        this.navn = produkt.getNavn();
        this.users = UserDTO.getDtos(produkt.getKunder());
    }

    public static List<ProduktDTO> getDtos(List<Produkt> produkt){
        List<ProduktDTO> produktdtos = new ArrayList();
        produkt.forEach(pro->produktdtos.add(new ProduktDTO(pro)));
        return produktdtos;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }


    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}
