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
public class Mouvement {
    String idMouv;
    String destination;
    public Mouvement() {
        this.idMouv = "";
        this.destination = "";
    }
    
    public String getIdMouv() {
        return idMouv;
    }

    public void setIdMouv(String idMouv) {
        this.idMouv = idMouv;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Mouvement(String idMouv, String destination) {
        this.idMouv = idMouv;
        this.destination = destination;
    }
}
