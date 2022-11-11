/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Butik;
import entities.User;

import java.util.ArrayList;
import java.util.List;

public class ButikDTO {
    private String navn;
    private List<UserDTO> usersDTOS;
    public ButikDTO(String navn) {
        this.navn = navn;
    }

    public ButikDTO(Butik butik) {
        this.navn = butik.getNavn();
    }

    public static List<ButikDTO> getDtos(List<Butik> butik){
        List<ButikDTO> butikdtos = new ArrayList();
        butik.forEach(bu->butikdtos.add(new ButikDTO(bu)));
        return butikdtos;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public List<UserDTO> getUsersDTOS() {
        return usersDTOS;
    }

    public void setUsersDTOS(List<UserDTO> usersDTOS) {
        this.usersDTOS = usersDTOS;
    }
}
