package touhami.otmane.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import touhami.otmane.backend.entities.Credit;
import touhami.otmane.backend.entities.Remboursement;
import touhami.otmane.backend.enums.TypeRemboursement;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RemboursementRepository extends JpaRepository<Remboursement, Long> {

    List<Remboursement> findByCreditId(Long creditId);

    List<Remboursement> findByCredit(Credit credit);

    List<Remboursement> findByType(TypeRemboursement type);

    List<Remboursement> findByDateBetween(LocalDate startDate, LocalDate endDate);
}