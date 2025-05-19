package touhami.otmane.backend.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import touhami.otmane.backend.dtos.ClientDTO;
import touhami.otmane.backend.dtos.CreditDTO;
import touhami.otmane.backend.exceptions.ClientNotFoundException;
import touhami.otmane.backend.services.interfaces.CreditManagementService;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@AllArgsConstructor
@Slf4j
public class ClientRestController {

    private CreditManagementService creditService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYE')")
    public List<ClientDTO> getAllClients() {
        log.info("Received request to get all clients");
        return creditService.getAllClients();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYE')")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        log.info("Received request to get client by ID: {}", id);
        try {
            ClientDTO client = creditService.getClientById(id);
            return ResponseEntity.ok(client);
        } catch (ClientNotFoundException e) {
            log.error("Client not found: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYE')")
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        log.info("Received request to create client: {}", clientDTO.getEmail());
        if (clientDTO.getId() != null) {
            log.warn("ClientDTO ID is not null during creation: {}", clientDTO.getId());
            clientDTO.setId(null);
        }
        ClientDTO savedClient = creditService.saveClient(clientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYE')")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDTO clientDTO) {
        log.info("Received request to update client ID: {}", id);
        if (clientDTO.getId() == null || !clientDTO.getId().equals(id)) {
            log.warn("Client ID in path ({}) does not match ID in body ({}), setting ID in body to path ID.", id, clientDTO.getId());
            clientDTO.setId(id);
        }

        try {
            ClientDTO updatedClient = creditService.updateClient(id, clientDTO);
            return ResponseEntity.ok(updatedClient);
        } catch (ClientNotFoundException e) {
            log.error("Client not found for update: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error updating client: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.info("Received request to delete client by ID: {}", id);
        try {
            creditService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (ClientNotFoundException e) {
            log.error("Client not found for deletion: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error deleting client: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYE')")
    public List<ClientDTO> searchClients(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
        log.info("Received request to search clients with keyword: {}", keyword);
        return creditService.searchClients(keyword);
    }

    @GetMapping("/{clientId}/credits")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYE')")
    public ResponseEntity<List<CreditDTO>> getCreditsByClient(@PathVariable Long clientId) {
        log.info("Received request to get credits for client ID: {}", clientId);
        try {
            List<CreditDTO> credits = creditService.getCreditsByClientId(clientId);
            return ResponseEntity.ok(credits);
        } catch (ClientNotFoundException e) {
            log.error("Client not found while fetching credits: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error fetching credits for client: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}