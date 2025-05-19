package touhami.otmane.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("PROFESSIONNEL")
@Getter
@Setter
@NoArgsConstructor
public class CreditProfessionnel extends Credit {

    private String motif;

    @Column(name = "raison_sociale")
    private String raisonSocialeEnterprise;
}
