package touhami.otmane.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    @NotEmpty(message = "Le nom ne peut pas être vide")
    private String nom;
    @NotEmpty(message = "L'email ne peut pas être vide")
    @Email(message = "Format d'email invalide")
    private String email;
}