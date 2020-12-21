/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdaccess;

import Classes.Facture;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

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
    
    public synchronized static Facture getLastFacture (int maxfact) throws SQLException
    {
        Facture fact;
        
        
        rs =  instruc.executeQuery("select * from factures where flagValidated = '0' ORDER BY idFacture DESC LIMIT " + maxfact + ",1");
        rs.next();
        
        try {
            fact = new Facture(rs.getInt("idFacture"),rs.getString("idSociete"),rs.getString("moisAnnee"),rs.getString("montantTotal"),rs.getString("montantAPayer"),rs.getInt("flagValidated"),rs.getInt("idComptableValidateur"),rs.getInt("flagSent"),rs.getString("moyenEnvoi"),rs.getInt("flagPaid"));
        } catch (Exception e) {
            return null;
        }
        
        
        return fact;
    }
    
    public synchronized static void validateFacture (String idFacture, int idComptable) throws SQLException
    {
        instruc.executeUpdate("UPDATE factures SET FlagValidated = 1, idComptableValidateur = " + idComptable + " WHERE idFacture = '"+ idFacture +"'");
    }
    
    public synchronized static LinkedList<Facture> getListeFactures (int idComptable) throws SQLException
    {
        LinkedList<Facture> listeFactures = new LinkedList<Facture>();
        Facture fact;
        rs =  instruc.executeQuery("select * from factures where idComptableValidateur = " + idComptable + " and flagSent = 0 ");
     
        
        while(rs.next()){
                fact = new Facture(rs.getInt("idFacture"),rs.getString("idSociete"),rs.getString("moisAnnee"),rs.getString("montantTotal"),rs.getString("montantAPayer"),rs.getInt("flagValidated"),rs.getInt("idComptableValidateur"),rs.getInt("flagSent"),rs.getString("moyenEnvoi"),rs.getInt("flagPaid"));
                listeFactures.add(fact);
        }
        
        return listeFactures;
    }
   
    
    public synchronized void updateSendBill(LinkedList<Integer> listeId,int idComptable) throws SQLException
    {
        instruc.executeUpdate("UPDATE factures SET FlagSent = 1 WHERE idComptableValidateur = '"+ idComptable +"'");
        
        for (int i=0;i<listeId.size();i++)
        {
            instruc.executeUpdate("UPDATE factures SET FlagSent = 0 WHERE idFacture = '"+ listeId.get(i) +"'");
        }
    }

    public synchronized LinkedList<Facture> getListBill(String idSociete, String dateDebut, String dateFin) throws SQLException 
    {
       LinkedList<Facture> listeFactures = new LinkedList<Facture>();
        Facture fact;
        rs =  instruc.executeQuery("select * from factures where idSociete = " + idSociete);
     
        try {
             while(rs.next()){
                fact = new Facture(rs.getInt("idFacture"),rs.getString("idSociete"),rs.getString("moisAnnee"),rs.getString("montantTotal"),rs.getString("montantAPayer"),rs.getInt("flagValidated"),rs.getInt("idComptableValidateur"),rs.getInt("flagSent"),rs.getString("moyenEnvoi"),rs.getInt("flagPaid"));
                listeFactures.add(fact);
            }
        } catch (Exception e) {
            return null;
        }
       
        
        return listeFactures;
    }
    
    public synchronized int getMontantAPayer(int numFact) throws SQLException
    {
        int montant = -1;
        
        rs =  instruc.executeQuery("select * from factures where flagPaid = 0 AND idFacture = " + numFact);
        
        if (rs.next())
        {
            montant = rs.getInt("montantAPayer");
        }
 
        return montant;       
    }
    
    public synchronized String updatePaiement (int numFact, int montant) throws SQLException
    {
        String reponse = "";
        int montantTotal = getMontantAPayer(numFact);
        
        if (montant <= montantTotal)
        {
            if (montant == montantTotal)
            {
                instruc.executeUpdate("UPDATE factures SET flagPaid = 1, montantAPayer = 0 WHERE idFacture = " + numFact);
                reponse = "La totalité a été payée";
            }
            else
            {
                int reste = montantTotal - montant;
                instruc.executeUpdate("UPDATE factures SET montantAPayer = " + reste + "WHERE idFacture = " + numFact);
                reponse = "Une partie du paiement été effectuée";
            }
        }
        else
        {
            reponse = "non + Le montant est supérieur au montant à payer";
        }
        return reponse;
    }
    
    public synchronized ArrayList<Facture> getAllWaitingPayements () throws SQLException
    {
        ArrayList<Facture> list = new ArrayList<Facture>();
        Facture f;
        
        rs =  instruc.executeQuery("select * from factures where flagPaid = 0");
        
        while(rs.next())
        {
            f = new Facture(rs.getInt("idFacture"), rs.getString("idSociete"), rs.getString("moisAnnee"), rs.getString("montantTotal"), 
                    rs.getString("montantAPayer"), rs.getInt("flagValidated"), rs.getInt("idComptableValidateur"), rs.getInt("flagSent"), rs.getString("moyenEnvoi"), 
                    rs.getInt("flagPaid"));
            
            list.add(f);
        }
        return list;
    }
    
    public synchronized ArrayList<Facture> getWaitingPaymentsBySoc (int id) throws SQLException
    {
        ArrayList<Facture> list = new ArrayList<Facture> ();
        Facture f;
        
        rs =  instruc.executeQuery("select * from factures where flagPaid = 0 AND idSociete = " + id);
        
        while (rs.next())
        {
            f = new Facture(rs.getInt("idFacture"), rs.getString("idSociete"), rs.getString("moisAnnee"), rs.getString("montantTotal"), 
                    rs.getString("montantAPayer"), rs.getInt("flagValidated"), rs.getInt("idComptableValidateur"), rs.getInt("flagSent"), rs.getString("moyenEnvoi"), 
                    rs.getInt("flagPaid"));
            list.add(f);
        }
        return list;
    }
    
    public synchronized ArrayList<Facture> getWaitingPaymentsByDate() throws SQLException, ParseException
    {
        ArrayList<Facture> list = new ArrayList<Facture> ();
        Facture f;
        
        Date currentDate = java.util.Calendar.getInstance().getTime();
        Date rowDate;
        
        SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
        
        String cur = formatter.format(currentDate);
        currentDate = formatter.parse(cur);
        
        rs =  instruc.executeQuery("select * from factures where flagPaid = 0");
        
        while (rs.next())
        {
            rowDate = formatter.parse(rs.getString("moisAnnee"));
            
            if (currentDate.after(rowDate))
            {
                f = new Facture(rs.getInt("idFacture"), rs.getString("idSociete"), rs.getString("moisAnnee"), rs.getString("montantTotal"), 
                    rs.getString("montantAPayer"), rs.getInt("flagValidated"), rs.getInt("idComptableValidateur"), rs.getInt("flagSent"), rs.getString("moyenEnvoi"), 
                    rs.getInt("flagPaid"));
                list.add(f);
            }
        }
        return list;
    }
    
}
