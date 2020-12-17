/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdaccess;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author vange
 */
public class BeanBDAccessItems_Facture extends BeanBDAccess{
    
    private static ResultSet rs;
    public BeanBDAccessItems_Facture(String User, String Pwd, String Port, String DBName, String Host, String finUrl) throws SQLException, ClassNotFoundException {
        super(User, Pwd, Port, DBName, Host, finUrl);
    }
   
    public synchronized static void insertItemFacture (String idFacture, String idMouvement, String idContainer, String destination ) throws SQLException
    {
        instruc.executeUpdate("INSERT INTO items_facture (idFacture,idMouvement,idContainer,destination,prix) VALUES ('" +  idFacture + "','" + idMouvement  + "','" + idContainer + "', '" + destination + "', '1000')");
    }
    
}
