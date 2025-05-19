package touhami.otmane.backend.services.interfaces;

import touhami.otmane.backend.dtos.ClientDTO;
import touhami.otmane.backend.dtos.CreditDTO;
import touhami.otmane.backend.dtos.RemboursementDTO;
import touhami.otmane.backend.enums.StatutCredit;
import touhami.otmane.backend.exceptions.ClientNotFoundException;
import touhami.otmane.backend.exceptions.CreditNotFoundException;

import java.util.List;

public interface CreditManagementService {

    // Client Operations
    ClientDTO saveClient(ClientDTO clientDTO);
    ClientDTO updateClient(Long id, ClientDTO clientDTO) throws ClientNotFoundException;
    ClientDTO getClientById(Long id) throws ClientNotFoundException;
    List<ClientDTO> getAllClients();
    void deleteClient(Long id) throws ClientNotFoundException;
    List<ClientDTO> searchClients(String keyword);

    // Credit Operations
    CreditDTO saveCredit(CreditDTO creditDTO) throws ClientNotFoundException;
    CreditDTO updateCredit(Long id, CreditDTO creditDTO) throws CreditNotFoundException, ClientNotFoundException; // Update
    CreditDTO getCreditById(Long id) throws CreditNotFoundException;
    List<CreditDTO> getAllCredits();
    List<CreditDTO> getCreditsByClientId(Long clientId) throws ClientNotFoundException;
    List<CreditDTO> getCreditsByStatus(StatutCredit status);
    void deleteCredit(Long id) throws CreditNotFoundException;
    CreditDTO acceptCredit(Long id) throws CreditNotFoundException;
    CreditDTO rejectCredit(Long id) throws CreditNotFoundException;

    // Remboursement Operations
    RemboursementDTO addRemboursement(RemboursementDTO remboursementDTO) throws CreditNotFoundException; // Create

    List<RemboursementDTO> getRemboursementsByCreditId(Long creditId) throws CreditNotFoundException;

}