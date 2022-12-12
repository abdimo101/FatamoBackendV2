package dtos;

import entities.Pris;
import entities.Produkt;
import entities.User;

public class UserProduktDTO {
    private UserDTO userDTO;
    private ProduktDTO produktDTO;
    private PrisDTO prisDTO;

    public UserProduktDTO(User user, Produkt produkt, Pris pris) {
        this.userDTO = new UserDTO(user);
        this.produktDTO = new ProduktDTO(produkt);
        this.prisDTO = new PrisDTO(pris);
    }

    public UserProduktDTO(UserDTO userDTO, ProduktDTO produktDTO) {
        this.userDTO = userDTO;
        this.produktDTO = produktDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public ProduktDTO getProduktDTO() {
        return produktDTO;
    }

    public void setProduktDTO(ProduktDTO produktDTO) {
        this.produktDTO = produktDTO;
    }

    public PrisDTO getPrisDTO() {
        return prisDTO;
    }

    public void setPrisDTO(PrisDTO prisDTO) {
        this.prisDTO = prisDTO;
    }
}
