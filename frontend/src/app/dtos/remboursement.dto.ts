export enum TypeRemboursement {
  MENSUALITE = 'MENSUALITE',
  REMBOURSEMENT_ANTICIPE = 'REMBOURSEMENT_ANTICIPE'
}

export interface RemboursementDTO {
  id?: number;
  date: string; // Consider using Date type
  montant: number;
  type: TypeRemboursement;
  creditId: number;
} 