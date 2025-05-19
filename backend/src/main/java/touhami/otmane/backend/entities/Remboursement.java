package touhami.otmane.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import touhami.otmane.backend.enums.TypeRemboursement;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "remboursements")
@Getter
@Setter
@NoArgsConstructor
public class Remboursement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    private TypeRemboursement type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id")
    private Credit credit;
}

