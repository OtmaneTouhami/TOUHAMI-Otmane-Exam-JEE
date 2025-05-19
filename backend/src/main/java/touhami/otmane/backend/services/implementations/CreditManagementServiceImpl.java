package touhami.otmane.backend.services.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touhami.otmane.backend.dtos.ClientDTO;
import touhami.otmane.backend.dtos.CreditDTO;
import touhami.otmane.backend.dtos.RemboursementDTO;
import touhami.otmane.backend.entities.*;
import touhami.otmane.backend.enums.StatutCredit;
import touhami.otmane.backend.exceptions.ClientNotFoundException;
import touhami.otmane.backend.exceptions.CreditNotFoundException;
import touhami.otmane.backend.mappers.AppMapper;
import touhami.otmane.backend.repositories.ClientRepository;
import touhami.otmane.backend.repositories.CreditRepository;
import touhami.otmane.backend.repositories.RemboursementRepository;
import touhami.otmane.backend.services.interfaces.CreditManagementService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CreditManagementServiceImpl implements CreditManagementService {

    private ClientRepository clientRepository;
    private CreditRepository creditRepository;
    private RemboursementRepository remboursementRepository;
    private AppMapper mapper;

    // --- Client Operations ---

    @Override
    public ClientDTO saveClient(ClientDTO clientDTO) {
        log.info("Saving new Client");
        Client client = mapper.fromClientDTO(clientDTO);
        Client savedClient = clientRepository.save(client);
        log.info("Client saved with ID: {}", savedClient.getId());
        return mapper.fromClient(savedClient);
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) throws ClientNotFoundException {
        log.info("Updating Client with ID: {}", id);
        Optional<Client> existingClientOpt = clientRepository.findById(id);
        if (existingClientOpt.isEmpty()) {
            throw new ClientNotFoundException("Client not found with ID: " + id);
        }
        Client existingClient = existingClientOpt.get();

        existingClient.setNom(clientDTO.getNom());
        existingClient.setEmail(clientDTO.getEmail());

        Client updatedClient = clientRepository.save(existingClient);
        log.info("Client updated with ID: {}", updatedClient.getId());
        return mapper.fromClient(updatedClient);
    }


    @Override
    public ClientDTO getClientById(Long id) throws ClientNotFoundException {
        log.info("Fetching Client with ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with ID: " + id));
        return mapper.fromClient(client);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        log.info("Fetching all Clients");
        List<Client> clients = clientRepository.findAll();
        return mapper.fromClientList(clients);
    }

    @Override
    public void deleteClient(Long id) throws ClientNotFoundException {
        log.info("Deleting Client with ID: {}", id);
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client not found with ID: " + id);
        }
        clientRepository.deleteById(id);
        log.info("Client deleted with ID: {}", id);
    }

    @Override
    public List<ClientDTO> searchClients(String keyword) {
        log.info("Searching Clients with keyword: {}", keyword);
        log.warn("SearchClients implementation is basic or requires repository method. Using findByNomContainingIgnoreCase for now.");
        List<Client> clients = clientRepository.findByNomContainingIgnoreCase(keyword);
        return mapper.fromClientList(clients);
    }

    // --- Credit Operations ---

    @Override
    public CreditDTO saveCredit(CreditDTO creditDTO) throws ClientNotFoundException {
        log.info("Saving new Credit type: {}", creditDTO.getType());
        Client client = clientRepository.findById(creditDTO.getClientId())
                .orElseThrow(() -> new ClientNotFoundException("Client not found with ID: " + creditDTO.getClientId()));

        Credit credit = mapper.fromCreditDTO(creditDTO);
        credit.setClient(client);

        if (credit.getStatut() == StatutCredit.ACCEPTE && credit.getDateAcceptation() == null) {
            credit.setDateAcceptation(LocalDate.now());
        } else if (credit.getStatut() != StatutCredit.ACCEPTE) {
            credit.setDateAcceptation(null);
        }


        Credit savedCredit = creditRepository.save(credit);
        log.info("Credit saved with ID: {}", savedCredit.getId());
        return mapper.fromCredit(savedCredit);
    }

    @Override
    public CreditDTO updateCredit(Long id, CreditDTO creditDTO) throws CreditNotFoundException, ClientNotFoundException {
        log.info("Updating Credit with ID: {}", id);
        Optional<Credit> existingCreditOpt = creditRepository.findById(id);
        if (existingCreditOpt.isEmpty()) {
            throw new CreditNotFoundException("Credit not found with ID: " + id);
        }
        Credit existingCredit = existingCreditOpt.get();

        if (!existingCredit.getClass().getSimpleName().toUpperCase().contains(creditDTO.getType().toUpperCase())) {
            throw new IllegalArgumentException("Cannot change credit type during update.");
        }

        Client client = clientRepository.findById(creditDTO.getClientId())
                .orElseThrow(() -> new ClientNotFoundException("Client not found with ID: " + creditDTO.getClientId()));

        existingCredit.setDateDemande(creditDTO.getDateDemande());
        existingCredit.setStatut(creditDTO.getStatut());
        existingCredit.setMontant(creditDTO.getMontant());
        existingCredit.setDuree(creditDTO.getDuree());
        existingCredit.setTauxInteret(creditDTO.getTauxInteret());
        existingCredit.setClient(client);

        if (creditDTO.getStatut() == StatutCredit.ACCEPTE && existingCredit.getDateAcceptation() == null) {
            existingCredit.setDateAcceptation(LocalDate.now());
        } else if (creditDTO.getStatut() != StatutCredit.ACCEPTE) {
            existingCredit.setDateAcceptation(null);
        } else {
            existingCredit.setDateAcceptation(creditDTO.getDateAcceptation());
        }


        if (existingCredit instanceof CreditPersonnel) {
            ((CreditPersonnel) existingCredit).setMotif(creditDTO.getMotif());
        } else if (existingCredit instanceof CreditImmobilier) {
            ((CreditImmobilier) existingCredit).setTypeBienFinance(creditDTO.getTypeBienFinance());
        } else if (existingCredit instanceof CreditProfessionnel) {
            ((CreditProfessionnel) existingCredit).setMotif(creditDTO.getMotif());
            ((CreditProfessionnel) existingCredit).setRaisonSocialeEnterprise(creditDTO.getRaisonSocialeEntreprise());
        } else {
            log.warn("Unknown credit subclass during update: {}", existingCredit.getClass().getName());
        }


        Credit updatedCredit = creditRepository.save(existingCredit);
        log.info("Credit updated with ID: {}", updatedCredit.getId());
        return mapper.fromCredit(updatedCredit);
    }


    @Override
    public CreditDTO getCreditById(Long id) throws CreditNotFoundException {
        log.info("Fetching Credit with ID: {}", id);
        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new CreditNotFoundException("Credit not found with ID: " + id));
        return mapper.fromCredit(credit);
    }

    @Override
    public List<CreditDTO> getAllCredits() {
        log.info("Fetching all Credits");
        List<Credit> credits = creditRepository.findAll();
        return mapper.fromCreditList(credits);
    }

    @Override
    public List<CreditDTO> getCreditsByClientId(Long clientId) throws ClientNotFoundException {
        log.info("Fetching Credits for Client ID: {}", clientId);
        if (!clientRepository.existsById(clientId)) {
            throw new ClientNotFoundException("Client not found with ID: " + clientId);
        }
        List<Credit> credits = creditRepository.findByClientId(clientId);
        return mapper.fromCreditList(credits);
    }

    @Override
    public List<CreditDTO> getCreditsByStatus(StatutCredit status) {
        log.info("Fetching Credits with status: {}", status);
        List<Credit> credits = creditRepository.findByStatut(status);
        return mapper.fromCreditList(credits);
    }

    @Override
    public void deleteCredit(Long id) throws CreditNotFoundException {
        log.info("Deleting Credit with ID: {}", id);
        if (!creditRepository.existsById(id)) {
            throw new CreditNotFoundException("Credit not found with ID: " + id);
        }
        creditRepository.deleteById(id);
        log.info("Credit deleted with ID: {}", id);
    }

    @Override
    public CreditDTO acceptCredit(Long id) throws CreditNotFoundException {
        log.info("Accepting Credit with ID: {}", id);
        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new CreditNotFoundException("Credit not found with ID: " + id));

        if (credit.getStatut() == StatutCredit.ACCEPTE) {
            log.warn("Credit {} is already accepted.", id);
            return mapper.fromCredit(credit);
        }

        credit.setStatut(StatutCredit.ACCEPTE);
        credit.setDateAcceptation(LocalDate.now());
        Credit updatedCredit = creditRepository.save(credit);
        log.info("Credit {} accepted.", id);
        return mapper.fromCredit(updatedCredit);
    }

    @Override
    public CreditDTO rejectCredit(Long id) throws CreditNotFoundException {
        log.info("Rejecting Credit with ID: {}", id);
        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new CreditNotFoundException("Credit not found with ID: " + id));

        if (credit.getStatut() == StatutCredit.REJETE) {
            log.warn("Credit {} is already rejected.", id);
            return mapper.fromCredit(credit);
        }

        credit.setStatut(StatutCredit.REJETE);
        credit.setDateAcceptation(null);
        Credit updatedCredit = creditRepository.save(credit);
        log.info("Credit {} rejected.", id);
        return mapper.fromCredit(updatedCredit);
    }


    // --- Remboursement Operations ---

    @Override
    public RemboursementDTO addRemboursement(RemboursementDTO remboursementDTO) throws CreditNotFoundException {
        log.info("Adding new Remboursement for Credit ID: {}", remboursementDTO.getCreditId());
        Credit credit = creditRepository.findById(remboursementDTO.getCreditId())
                .orElseThrow(() -> new CreditNotFoundException("Credit not found with ID: " + remboursementDTO.getCreditId()));

        if (credit.getStatut() != StatutCredit.ACCEPTE) {
            throw new IllegalStateException("Cannot add a reimbursement to a credit that is not accepted.");
        }

        Remboursement remboursement = mapper.fromRemboursementDTO(remboursementDTO);
        remboursement.setCredit(credit);

        Remboursement savedRemboursement = remboursementRepository.save(remboursement);
        log.info("Remboursement saved with ID: {}", savedRemboursement.getId());
        return mapper.fromRemboursement(savedRemboursement);
    }

    @Override
    public List<RemboursementDTO> getRemboursementsByCreditId(Long creditId) throws CreditNotFoundException {
        log.info("Fetching Remboursements for Credit ID: {}", creditId);
        if (!creditRepository.existsById(creditId)) {
            throw new CreditNotFoundException("Credit not found with ID: " + creditId);
        }
        List<Remboursement> remboursements = remboursementRepository.findByCreditId(creditId);
        return mapper.fromRemboursementList(remboursements);
    }
}