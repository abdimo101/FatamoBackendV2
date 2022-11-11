package dtos;

import entities.Butik;
import entities.Pris;
import entities.Produkt;

public class PrisProduktDTO {
    private PrisDTO pris;
    private ProduktDTO produkt;
    private ButikDTO butik;
    public PrisProduktDTO(Produkt produkt, Pris pris, Butik butik) {
        this.pris = new PrisDTO(pris);
        this.produkt = new ProduktDTO(produkt);
        this.butik = new ButikDTO(butik);
    }

    public PrisDTO getPris() {
        return pris;
    }

    public void setPris(PrisDTO pris) {
        this.pris = pris;
    }

    public ProduktDTO getProdukt() {
        return produkt;
    }

    public void setProdukt(ProduktDTO produkt) {
        this.produkt = produkt;
    }

    public ButikDTO getButik() {
        return butik;
    }

    public void setButik(ButikDTO butik) {
        this.butik = butik;
    }
}
