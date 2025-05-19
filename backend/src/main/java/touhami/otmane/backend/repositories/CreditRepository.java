package touhami.otmane.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import touhami.otmane.backend.entities.Client;
import touhami.otmane.backend.entities.Credit;
import touhami.otmane.backend.enums.StatutCredit;

import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {

    List<Credit> findByClientId(Long clientId);

    List<Credit> findByClient(Client client);

    List<Credit> findByStatut(StatutCredit statut);
}