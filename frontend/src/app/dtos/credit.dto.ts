export enum StatutCredit {
  EN_COURS = 'EN_COURS',
  ACCEPTE = 'ACCEPTE',
  REJETE = 'REJETE'
}

export enum TypeBienFinance {
  APPARTEMENT = 'APPARTEMENT',
  MAISON = 'MAISON',
  LOCAL_COMMERCIAL = 'LOCAL_COMMERCIAL'
}

export interface CreditDTO {
  id?: number;
  dateDemande: string; // Consider using Date type and handling conversion
  statut: StatutCredit;
  dateAcceptation?: string; // Consider using Date type
  montant: number;
  duree: number;
  tauxInteret: number;
  clientId: number;
  type: string; // This refers to the type of credit (Personnel, Immobilier, Professionnel)

  // For Crédit Personnel
  motif?: string;

  // For Crédit Immobilier
  typeBienFinance?: TypeBienFinance;

  // For Crédit Professionnel
  // motif is already there
  raisonSocialeEntreprise?: string;
} 