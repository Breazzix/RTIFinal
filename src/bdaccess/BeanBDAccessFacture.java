/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdaccess;

import Classes.Facture;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author vange
 */
public class BeanBDAccessFacture extends BeanBDAccess{
    
    private static ResultSet rs;
    public BeanBDAccessFacture(String User, String Pwd, String Port, String DBName, String Host, String finUrl) throws SQLException, ClassNotFoundException {
        super(User, Pwd, Port, DBName, Host, finUrl);
    }
   
    public synchronized static String insertFacture (String idSoc, String date) throws SQLException
    {
        String idFacture;
        instruc.executeUpdate("INSERT INTO factures (idSociete,moisAnnee,montantTotal,montantAPayer,flagValidated,flagSent,moyenEnvoi,flagPaid) VALUES ('" +  idSoc + "','" + date  + "','1000', '1000', '0', '0', 'Bateau', '0')");
        
        rs =  instruc.executeQuery("select Max(idFacture) as Max_id from factures");
        rs.next();
        
        idFacture = rs.getString("Max_id");
        return idFacture;
    }
    
    public synchronized static Facture getLastFacture () throws SQLException
    {
        Facture fact;
        
        rs =  instruc.executeQuery("select Max(idFacture) as Max_id, idSociete, moisAnnee, montantTotal, montantAPayer, flagValidated, idComptableValidateur, flagSent, moyenEnvoi, flagPaid from factures where flagValidated = '0'");
        rs.next();
        
        try {
            fact = new Facture(rs.getInt("Max_id"),rs.getString("idSociete"),rs.getString("moisAnnee"),rs.getString("montantTotal"),rs.getString("montantAPayer"),rs.getInt("flagValidated"),rs.getInt("idComptableValidateur"),rs.getInt("flagSent"),rs.getString("moyenEnvoi"),rs.getInt("flagPaid"));
        } catch (Exception e) {
            return null;
        }
        
        
        return fact;
    }
    
}
