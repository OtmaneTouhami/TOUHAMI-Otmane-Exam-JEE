package touhami.otmane.backend.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import touhami.otmane.backend.dtos.ClientDTO;
import touhami.otmane.backend.dtos.CreditDTO;
import touhami.otmane.backend.dtos.RemboursementDTO;
import touhami.otmane.backend.entities.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppMapper {

    public ClientDTO fromClient(Client client) {
        if (client == null) return null;
        ClientDTO clientDTO = new ClientDTO();
        BeanUtils.copyProperties(client, clientDTO);
        return clientDTO;
    }

    public Client fromClientDTO(ClientDTO clientDTO) {
        if (clientDTO == null) return null;
        Client client = new Client();
        BeanUtils.copyProperties(clientDTO, client);
        return client;
    }

    public CreditDTO fromCredit(Credit credit) {
        if (credit == null) return null;
        CreditDTO creditDTO = new CreditDTO();
        BeanUtils.copyProperties(credit, creditDTO);

        if (credit.getClient() != null) {
            creditDTO.setClientId(credit.getClient().getId());
        }

        if (credit instanceof CreditPersonnel) {
            CreditPersonnel cp = (CreditPersonnel) credit;
            creditDTO.setType("PERSONNEL");
            creditDTO.setMotif(cp.getMotif());
            creditDTO.setTypeBienFinance(null);
            creditDTO.setRaisonSocialeEntreprise(null);
        } else if (credit instanceof CreditImmobilier) {
            CreditImmobilier ci = (CreditImmobilier) credit;
            creditDTO.setType("IMMOBILIER");
            creditDTO.setTypeBienFinance(ci.getTypeBienFinance());
            creditDTO.setMotif(null);
            creditDTO.setRaisonSocialeEntreprise(null);
        } else if (credit instanceof CreditProfessionnel) {
            CreditProfessionnel cpro = (CreditProfessionnel) credit;
            creditDTO.setType("PROFESSIONNEL");
            creditDTO.setMotif(cpro.getMotif());
            creditDTO.setRaisonSocialeEntreprise(cpro.getRaisonSocialeEnterprise());
            creditDTO.setTypeBienFinance(null);
        }

        return creditDTO;
    }

    public Credit fromCreditDTO(CreditDTO creditDTO) {
        if (creditDTO == null) return null;

        Credit credit;
        String type = creditDTO.getType();
        if ("PERSONNEL".equalsIgnoreCase(type)) {
            credit = new CreditPersonnel();
            ((CreditPersonnel) credit).setMotif(creditDTO.getMotif());
        } else if ("IMMOBILIER".equalsIgnoreCase(type)) {
            credit = new CreditImmobilier();
            ((CreditImmobilier) credit).setTypeBienFinance(creditDTO.getTypeBienFinance());
        } else if ("PROFESSIONNEL".equalsIgnoreCase(type)) {
            credit = new CreditProfessionnel();
            ((CreditProfessionnel) credit).setMotif(creditDTO.getMotif());
            ((CreditProfessionnel) credit).setRaisonSocialeEnterprise(creditDTO.getRaisonSocialeEntreprise());
        } else {
            throw new IllegalArgumentException("Unknown credit type: " + type);
        }

        BeanUtils.copyProperties(creditDTO, credit, "type", "clientId", "motif", "typeBienFinance", "raisonSocialeEntreprise");
        return credit;
    }

    public RemboursementDTO fromRemboursement(Remboursement remboursement) {
        if (remboursement == null) return null;
        RemboursementDTO remboursementDTO = new RemboursementDTO();
        BeanUtils.copyProperties(remboursement, remboursementDTO);
        if (remboursement.getCredit() != null) {
            remboursementDTO.setCreditId(remboursement.getCredit().getId());
        }
        return remboursementDTO;
    }

    public Remboursement fromRemboursementDTO(RemboursementDTO remboursementDTO) {
        if (remboursementDTO == null) return null;
        Remboursement remboursement = new Remboursement();
        BeanUtils.copyProperties(remboursementDTO, remboursement);
        return remboursement;
    }

    public List<ClientDTO> fromClientList(List<Client> clients) {
        return clients.stream().map(this::fromClient).collect(Collectors.toList());
    }

    public List<CreditDTO> fromCreditList(List<Credit> credits) {
        return credits.stream().map(this::fromCredit).collect(Collectors.toList());
    }

    public List<RemboursementDTO> fromRemboursementList(List<Remboursement> remboursements) {
        return remboursements.stream().map(this::fromRemboursement).collect(Collectors.toList());
    }
}