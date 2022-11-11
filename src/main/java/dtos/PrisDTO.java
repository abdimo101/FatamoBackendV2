/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Pris;

import java.util.ArrayList;
import java.util.List;

public class PrisDTO {
    private Integer beløb;
    private ButikDTO butikDTO;
    private ProduktDTO produktDTO;

    public PrisDTO(Integer beløb, ButikDTO butikDTO, ProduktDTO produktDTO) {
       this.beløb = beløb;
       this.butikDTO = butikDTO;
       this.produktDTO = produktDTO;
    }

    public PrisDTO(Pris pris) {
        this.beløb = pris.getBeløb();
    }

    public static List<PrisDTO> getDtos(List<Pris> pris){
        List<PrisDTO> prisdtos = new ArrayList();
        pris.forEach(pr->prisdtos.add(new PrisDTO(pr)));
        return prisdtos;
    }

    public Integer getBeløb() {
        return beløb;
    }

    public void setBeløb(Integer beløb) {
        this.beløb = beløb;
    }

    public ButikDTO getButikDTO() {
        return butikDTO;
    }

    public void setButikDTO(ButikDTO butikDTO) {
        this.butikDTO = butikDTO;
    }

    public ProduktDTO getProduktDTO() {
        return produktDTO;
    }

    public void setProduktDTO(ProduktDTO produktDTO) {
        this.produktDTO = produktDTO;
    }
}
