package touhami.otmane.backend.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import touhami.otmane.backend.dtos.CreditDTO;
import touhami.otmane.backend.dtos.RemboursementDTO;
import touhami.otmane.backend.enums.StatutCredit;
import touhami.otmane.backend.exceptions.ClientNotFoundException;
import touhami.otmane.backend.exceptions.CreditNotFoundException;
import touhami.otmane.backend.services.interfaces.CreditManagementService;

import java.util.List;

@RestController
@RequestMapping("/api/credits")
@AllArgsConstructor
@Slf4j
public class CreditRestController {

    private CreditManagementService creditService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYE')")
    public List<CreditDTO> getAllCredits() {
        log.info("Received request to get all credits");
        return creditService.getAllCredits();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYE') or hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<CreditDTO> getCreditById(@PathVariable Long id) {
        log.info("Received request to get credit by ID: {}", id);
        try {
            CreditDTO credit = creditService.getCreditById(id);
            return ResponseEntity.ok(credit);
        } catch (CreditNotFoundException e) {
            log.error("Credit not found: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CLIENT') or hasAuthority('ROLE_EMPLOYE')")
    public ResponseEntity<CreditDTO> createCredit(@Valid @RequestBody CreditDTO creditDTO) {
        log.info("Received request to create credit for client ID: {}", creditDTO.getClientId());
        if (creditDTO.getId() != null) {
            log.warn("CreditDTO ID is not null during creation: {}", creditDTO.getId());
            creditDTO.setId(null);
        }
        try {
            CreditDTO savedCredit = creditService.saveCredit(creditDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCredit);
        } catch (ClientNotFoundException e) {
            log.error("Client not found for credit creation: {}", creditDTO.getClientId(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IllegalArgumentException e) {
            log.error("Invalid credit data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("Error creating credit:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYE')")
    public ResponseEntity<CreditDTO> updateCredit(@PathVariable Long id, @Valid @RequestBody CreditDTO creditDTO) {
        log.info("Received request to update credit ID: {}", id);
        if (creditDTO.getId() == null || !creditDTO.getId().equals(id)) {
            log.warn("Credit ID in path ({}) does not match ID in body ({}), setting ID in body to path ID.", id, creditDTO.getId());
            creditDTO.setId(id);
        }
        try {
            CreditDTO updatedCredit = creditService.updateCredit(id, creditDTO);
            return ResponseEntity.ok(updatedCredit);
        } catch (CreditNotFoundException e) {
            log.error("Credit not found for update: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ClientNotFoundException e) {
            log.error("Client not found for credit update: {}", creditDTO.getClientId(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IllegalArgumentException e) {
            log.error("Invalid credit update data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        catch (Exception e) {
            log.error("Error updating credit: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCredit(@PathVariable Long id) {
        log.info("Received request to delete credit by ID: {}", id);
        try {
            creditService.deleteCredit(id);
            return ResponseEntity.noContent().build();
        } catch (CreditNotFoundException e) {
            log.error("Credit not found for deletion: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error deleting credit: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/accept")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYE')")
    public ResponseEntity<CreditDTO> acceptCredit(@PathVariable Long id) {
        log.info("Received request to accept credit ID: {}", id);
        try {
            CreditDTO acceptedCredit = creditService.acceptCredit(id);
            return ResponseEntity.ok(acceptedCredit);
        } catch (CreditNotFoundException e) {
            log.error("Credit not found for acceptance: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error accepting credit: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYE')")
    public ResponseEntity<CreditDTO> rejectCredit(@PathVariable Long id) {
        log.info("Received request to reject credit ID: {}", id);
        try {
            CreditDTO rejectedCredit = creditService.rejectCredit(id);
            return ResponseEntity.ok(rejectedCredit);
        } catch (CreditNotFoundException e) {
            log.error("Credit not found for rejection: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error rejecting credit: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYE')")
    public List<CreditDTO> getCreditsByStatus(@PathVariable StatutCredit status) {
        log.info("Received request to get credits by status: {}", status);
        return creditService.getCreditsByStatus(status);
    }


    @GetMapping("/{creditId}/remboursements")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYE') or hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<List<RemboursementDTO>> getRemboursementsByCredit(@PathVariable Long creditId) {
        log.info("Received request to get remboursements for credit ID: {}", creditId);
        try {
            List<RemboursementDTO> remboursements = creditService.getRemboursementsByCreditId(creditId);
            return ResponseEntity.ok(remboursements); // 200 OK
        } catch (CreditNotFoundException e) {
            log.error("Credit not found while fetching remboursements: {}", creditId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404
        } catch (Exception e) {
            log.error("Error fetching remboursements for credit: {}", creditId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
