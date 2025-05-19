package touhami.otmane.backend.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import touhami.otmane.backend.dtos.RemboursementDTO;
import touhami.otmane.backend.exceptions.CreditNotFoundException;
import touhami.otmane.backend.services.interfaces.CreditManagementService;

@RestController
@RequestMapping("/api/remboursements")
@AllArgsConstructor
@Slf4j
public class RemboursementRestController {

    private CreditManagementService creditService;

    @PostMapping
    public ResponseEntity<RemboursementDTO> addRemboursement(@Valid @RequestBody RemboursementDTO remboursementDTO) {
        log.info("Received request to add remboursement for credit ID: {}", remboursementDTO.getCreditId());
        if (remboursementDTO.getId() != null) {
            log.warn("RemboursementDTO ID is not null during creation: {}", remboursementDTO.getId());
            remboursementDTO.setId(null);
        }
        try {
            RemboursementDTO savedRemboursement = creditService.addRemboursement(remboursementDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRemboursement);
        } catch (CreditNotFoundException e) {
            log.error("Credit not found for adding reimbursement: {}", remboursementDTO.getCreditId(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IllegalStateException e) {
            log.error("Business rule violation when adding reimbursement: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("Error adding reimbursement:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}