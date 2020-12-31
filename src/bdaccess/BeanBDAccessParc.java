/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdaccess;

import Classes.emplacement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 *
 * @author vange
 */
public class BeanBDAccessParc extends BeanBDAccess{
    private static ResultSet rs;
    public BeanBDAccessParc(String User, String Pwd, String Port, String DBName, String Host, String finUrl) throws SQLException, ClassNotFoundException {
        super(User, Pwd, Port, DBName, Host, finUrl);
    }
    
    public void insertParc(int x, int y, int flag, String immat, int idContainer, int destination, String DateArrivee,float poids, String type) throws SQLException
    {
        instruc.executeUpdate("INSERT INTO parc (X,Y,FlagOccupe,NumImmat,IdContainer,Destination,DateArrivee,Poids,TypeTransport) VALUES ('" + x + "','" + y +"','" + flag + "','" + immat +"','" + idContainer +"',"
                + "'" + destination +"','" + DateArrivee +"','" + poids +"','" + type +"',)");
    }
    
    public synchronized int selectParc (String numRes) throws SQLException
    {
        int trouve = 0;
        
        rs =  instruc.executeQuery("select * from parc where Reservation = '" + numRes +"'");
        
        
        rs.next();
        
        try {
          trouve = rs.getInt("FlagOccupe");   
        } catch (Exception e) {
            trouve = 0;
        }
        
        return trouve;
    }
    public synchronized int selectParcEmplLibre () throws SQLException
    {
        int trouve = 0;
        
        rs =  instruc.executeQuery("select * from parc where FlagOccupe = '0'");
        rs.next();
        
        try {
          trouve = rs.getInt("idParc");   
        } catch (Exception e) {
            trouve = 0;
        }
        
        return trouve;
    }
    
    public synchronized int selectParcId (String immat,String dest, String date) throws SQLException
    {
        int trouve = 0;
        
        rs =  instruc.executeQuery("select * from parc where NumImmat = '" + immat +"'");
        rs.next();
        
        try {
          trouve = rs.getInt("idParc");   
        } catch (Exception e) {
            trouve = 0;
        }
        
        return trouve;
    }
    
    public synchronized int getX (int id) throws SQLException
    {
        int trouve = 0;
        
        rs =  instruc.executeQuery("select * from parc where idParc = '" + id +"'");
        
        rs.next();
        
        try {
          trouve = rs.getInt("X");  
        } catch (Exception e) {
           trouve = 0;
        }
        System.out.println("X:" + trouve);
        
        return trouve;
    }
    
    public synchronized int getY (int id) throws SQLException
    {
        int trouve = 0;
        
        
        rs =  instruc.executeQuery("select * from parc where idParc = '" + id +"'");
        
        rs.next();
        
        try {
          trouve = rs.getInt("Y");
        } catch (Exception e) {
            trouve = 0;
        }
       
        System.out.println("Y:" + trouve);
        return trouve;
    }
    
     public synchronized String getEmplacement(String numSociete,String numImmat,String idContainer,int destination) throws SQLException
     {
         String reponse = "";
         String x,y,numRes;
        
        
        rs =  instruc.executeQuery("select * from parc where idContainer = '" + idContainer +"' and destination = '" + destination + "' and NumImmat = '" + numImmat + "'");
        
        rs.next();
        
        try {
          x = rs.getString("X");
          y = rs.getString("Y");
          numRes = rs.getString("Reservation");
          reponse = "oui|"+x+"|"+y+"|"+numRes;
        } catch (Exception e) {
            reponse = "non|un element ne correspond pas";
        }
       
        System.out.println("reponse:" + reponse);
        return reponse;
     }
     
    public synchronized int updateWeight(String numContainer, String x,String y, String poids) throws SQLException
    {
        int ok = 0;
        try {
            instruc.executeUpdate("UPDATE parc SET Poids = '" + poids + "'  WHERE idContainer = '" + numContainer + "' and x = '" + x + "' and y = '" + y + "'");
            ok = 1;
        } catch (Exception e) {
            return ok;
        }
        
        return ok;
    }
    
    public synchronized LinkedList<emplacement> getListeEmplacements(String idTransport,int dest,String nbMaxCont) throws SQLException
    {
        LinkedList<emplacement> listeEmplacements = new LinkedList<emplacement>();
        emplacement emp;
        rs =  instruc.executeQuery("select * from parc where destination = '" + dest + "'");
     
        
        while(rs.next()){
                emp = new emplacement(rs.getString("X"),rs.getString("Y"));
                listeEmplacements.add(emp);
        }
        
        return listeEmplacements;
    }
}
