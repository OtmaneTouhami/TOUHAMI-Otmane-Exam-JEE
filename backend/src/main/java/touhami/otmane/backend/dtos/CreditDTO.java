package touhami.otmane.backend.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import touhami.otmane.backend.enums.StatutCredit;
import touhami.otmane.backend.enums.TypeBienFinance;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDTO {
    private Long id;

    @NotNull(message = "La date de demande est requise")
    private LocalDate dateDemande;

    @NotNull(message = "Le statut est requis")
    private StatutCredit statut;

    private LocalDate dateAcceptation;

    @NotNull(message = "Le montant est requis")
    @DecimalMin(value = "0.01", message = "Le montant doit être positif")
    private BigDecimal montant;

    @NotNull(message = "La durée est requise")
    @Min(value = 1, message = "La durée doit être au moins 1")
    private Integer duree;

    @NotNull(message = "Le taux d'intérêt est requis")
    @DecimalMin(value = "0.0", message = "Le taux d'intérêt ne peut pas être négatif")
    private BigDecimal tauxInteret;

    @NotNull(message = "Le client est requis")
    private Long clientId;

    @NotEmpty(message = "Le type de crédit est requis")
    private String type;

    private String motif;
    private TypeBienFinance typeBienFinance;
    private String raisonSocialeEntreprise;
}