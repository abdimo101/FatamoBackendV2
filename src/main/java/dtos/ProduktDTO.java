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

    public ProduktDTO(String navn) {
        this.navn = navn;
    }

    public ProduktDTO(Produkt produkt) {
        this.navn = produkt.getNavn();
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

}
