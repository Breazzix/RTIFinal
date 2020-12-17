/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author vange
 */
public class Facture {
    
    private int idFacture;
    private String societe;
    private String date;
    private String montantTotal;
    private String montantAPayer;
    private int flagValidated;
    private int idComptableValidateur;
    private int flagSent;
    private String moyenEnvoi;
    private int flagPaid;

    public Facture(int idFacture, String societe, String date, String montantTotal, String montantAPayer, int flagValidated, int idComptableValidateur, int flagSent, String moyenEnvoi, int flagPaid) {
        this.idFacture = idFacture;
        this.societe = societe;
        this.date = date;
        this.montantTotal = montantTotal;
        this.montantAPayer = montantAPayer;
        this.flagValidated = flagValidated;
        this.idComptableValidateur = idComptableValidateur;
        this.flagSent = flagSent;
        this.moyenEnvoi = moyenEnvoi;
        this.flagPaid = flagPaid;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getMontantAPayer() {
        return montantAPayer;
    }

    public void setMontantAPayer(String montantAPayer) {
        this.montantAPayer = montantAPayer;
    }

    public int getFlagValidated() {
        return flagValidated;
    }

    public void setFlagValidated(int flagValidated) {
        this.flagValidated = flagValidated;
    }

    public int getIdComptableValidateur() {
        return idComptableValidateur;
    }

    public void setIdComptableValidateur(int idComptableValidateur) {
        this.idComptableValidateur = idComptableValidateur;
    }

    public int getFlagSent() {
        return flagSent;
    }

    public void setFlagSent(int flagSent) {
        this.flagSent = flagSent;
    }

    public String getMoyenEnvoi() {
        return moyenEnvoi;
    }

    public void setMoyenEnvoi(String moyenEnvoi) {
        this.moyenEnvoi = moyenEnvoi;
    }

    public int getFlagPaid() {
        return flagPaid;
    }

    public void setFlagPaid(int flagPaid) {
        this.flagPaid = flagPaid;
    }
    
    @Override
    public String toString() {
        return idFacture + "|" + societe + "|" + date + "|" + montantTotal + "|" + montantAPayer + "|" + flagValidated + "|" + idComptableValidateur + "|" + flagSent + 
                "|" + moyenEnvoi + "|" + flagPaid;
    }
}
