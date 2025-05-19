package touhami.otmane.backend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("PERSONNEL")
@Getter
@Setter
@NoArgsConstructor
public class CreditPersonnel extends Credit {
    private String motif;
}