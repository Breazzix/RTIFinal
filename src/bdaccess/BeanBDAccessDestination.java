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
public class BeanBDAccessDestination extends BeanBDAccess{
    private static ResultSet rs;
    public BeanBDAccessDestination(String User, String Pwd, String Port, String DBName, String Host, String finUrl) throws SQLException, ClassNotFoundException {
        super(User, Pwd, Port, DBName, Host, finUrl);
    }
    
    public synchronized static int selectDestination (String dest) throws SQLException
    {
        int trouve = 0;
        
        rs =  instruc.executeQuery("select * from destinations where Ville = '" + dest +"'");
        
        rs.next();
        
        try {
            trouve = rs.getInt("IdDestination");
        } catch (Exception e) {
            trouve = 0;
        }
        
        
        return trouve;
    }
    
    public synchronized static void insertDestination (String dest) throws SQLException
    {
        instruc.executeUpdate("INSERT INTO destinations (Ville) VALUES ('" + dest + "')");
    }
}
