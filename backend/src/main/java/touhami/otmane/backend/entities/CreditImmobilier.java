package touhami.otmane.backend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import touhami.otmane.backend.enums.TypeBienFinance;

@Entity
@DiscriminatorValue("IMMOBILIER")
@Getter
@Setter
@NoArgsConstructor
public class CreditImmobilier extends Credit {
    @Enumerated(EnumType.STRING)
    private TypeBienFinance typeBienFinance;
}
