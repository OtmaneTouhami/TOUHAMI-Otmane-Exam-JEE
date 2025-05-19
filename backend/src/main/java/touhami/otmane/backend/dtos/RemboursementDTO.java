package touhami.otmane.backend.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import touhami.otmane.backend.enums.TypeRemboursement;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemboursementDTO {
    private Long id;

    @NotNull(message = "La date est requise")
    private LocalDate date;

    @NotNull(message = "Le montant est requis")
    @DecimalMin(value = "0.01", message = "Le montant doit être positif")
    private BigDecimal montant;

    @NotNull(message = "Le type de remboursement est requis")
    private TypeRemboursement type;

    @NotNull(message = "Le crédit associé est requis")
    private Long creditId;
}