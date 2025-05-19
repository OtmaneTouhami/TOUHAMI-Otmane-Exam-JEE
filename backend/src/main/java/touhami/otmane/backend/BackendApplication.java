package touhami.otmane.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import touhami.otmane.backend.entities.*;
import touhami.otmane.backend.enums.StatutCredit;
import touhami.otmane.backend.enums.TypeBienFinance;
import touhami.otmane.backend.enums.TypeRemboursement;
import touhami.otmane.backend.repositories.ClientRepository;
import touhami.otmane.backend.repositories.CreditRepository;
import touhami.otmane.backend.repositories.RemboursementRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(
            ClientRepository clientRepository,
            CreditRepository creditRepository,
            RemboursementRepository remboursementRepository
    ) {
        return args -> {
            System.out.println("--- Populating database with test data ---");

            // 1. Create Clients
            Stream.of("Hassan Sami", "Khalid Manus", "Fatima Lkhalfi")
                    .forEach(name -> {
                        Client client = new Client();
                        client.setNom(name);
                        client.setEmail(name.toLowerCase().replace(" ", ".") + "@gmail.com");
                        clientRepository.save(client);
                        System.out.println("Saved client: " + client);
                    });

            // Retrieve clients to link credits
            List<Client> clients = clientRepository.findAll();

            if (!clients.isEmpty()) {
                Client client1 = clients.get(0);
                Client client2 = clients.get(1);

                CreditPersonnel cp1 = new CreditPersonnel();
                cp1.setMotif("Achat de voiture");
                cp1.setDateDemande(LocalDate.now().minusMonths(6));
                cp1.setStatut(StatutCredit.ACCEPTE);
                cp1.setDateAcceptation(LocalDate.now().minusMonths(5));
                cp1.setMontant(new BigDecimal("20000.00"));
                cp1.setDuree(60);
                cp1.setTauxInteret(new BigDecimal("0.04"));
                cp1.setClient(client1);
                creditRepository.save(cp1);
                System.out.println("Saved credit personnel: " + cp1.getId());

                // Credit Immobilier
                CreditImmobilier ci1 = new CreditImmobilier();
                ci1.setTypeBienFinance(TypeBienFinance.MAISON);
                ci1.setDateDemande(LocalDate.now().minusYears(1));
                ci1.setStatut(StatutCredit.ACCEPTE);
                ci1.setDateAcceptation(LocalDate.now().minusYears(1).plusDays(15));
                ci1.setMontant(new BigDecimal("250000.00"));
                ci1.setDuree(300);
                ci1.setTauxInteret(new BigDecimal("0.025"));
                ci1.setClient(client1); // Set the relationship
                creditRepository.save(ci1);
                System.out.println("Saved credit immobilier: " + ci1.getId());

                // Credit Professionnel
                CreditProfessionnel cpro1 = new CreditProfessionnel();
                cpro1.setMotif("Acquisition de matériel");
                cpro1.setRaisonSocialeEnterprise("Bob's Plumbing Ltd.");
                cpro1.setDateDemande(LocalDate.now().minusMonths(3));
                cpro1.setStatut(StatutCredit.EN_COURS);
                cpro1.setDateAcceptation(null);
                cpro1.setMontant(new BigDecimal("50000.00"));
                cpro1.setDuree(84);
                cpro1.setTauxInteret(new BigDecimal("0.03"));
                cpro1.setClient(client2);
                creditRepository.save(cpro1);
                System.out.println("Saved credit professionnel: " + cpro1.getId());

                // Another Credit for Client 2 - rejected
                CreditPersonnel cp2 = new CreditPersonnel();
                cp2.setMotif("Vacances");
                cp2.setDateDemande(LocalDate.now().minusMonths(1));
                cp2.setStatut(StatutCredit.REJETE);
                cp2.setDateAcceptation(null);
                cp2.setMontant(new BigDecimal("5000.00"));
                cp2.setDuree(24);
                cp2.setTauxInteret(new BigDecimal("0.05"));
                cp2.setClient(client2);
                creditRepository.save(cp2);
                System.out.println("Saved rejected credit personnel: " + cp2.getId());


                // 4. Create Remboursements for the accepted credits
                BigDecimal monthlyPaymentCp1 = new BigDecimal("350.00");
                for (int i = 1; i <= 6; i++) {
                    Remboursement r = new Remboursement();
                    r.setDate(cp1.getDateAcceptation().plusMonths(i));
                    r.setMontant(monthlyPaymentCp1);
                    r.setType(TypeRemboursement.MENSUALITE);
                    r.setCredit(cp1);
                    remboursementRepository.save(r);
                    System.out.println("Saved remboursement for cp1: " + r.getId());
                }

                // Add an early reimbursement for cp1
                Remboursement r_early_cp1 = new Remboursement();
                r_early_cp1.setDate(cp1.getDateAcceptation().plusMonths(7));
                r_early_cp1.setMontant(new BigDecimal("2000.00"));
                r_early_cp1.setType(TypeRemboursement.REMBOURSEMENT_ANTICIPE);
                r_early_cp1.setCredit(cp1);
                remboursementRepository.save(r_early_cp1);
                System.out.println("Saved early remboursement for cp1: " + r_early_cp1.getId());


                // Remboursements for Credit Immobilier (ci1)
                BigDecimal monthlyPaymentCi1 = new BigDecimal("1100.00");
                for (int i = 1; i <= 12; i++) {
                    Remboursement r = new Remboursement();
                    r.setDate(ci1.getDateAcceptation().plusMonths(i));
                    r.setMontant(monthlyPaymentCi1);
                    r.setType(TypeRemboursement.MENSUALITE);
                    r.setCredit(ci1);
                    remboursementRepository.save(r);
                    System.out.println("Saved remboursement for ci1: " + r.getId());
                }
            } else {
                System.out.println("No clients found, skipping credit and remboursement creation.");
            }


            System.out.println("--- Database population finished ---");
        };
    }
}
