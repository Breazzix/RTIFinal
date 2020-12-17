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
public class BeanBDAccessSocietes extends BeanBDAccess{
    private static ResultSet rs;
    public BeanBDAccessSocietes(String User, String Pwd, String Port, String DBName, String Host, String finUrl) throws SQLException, ClassNotFoundException {
        super(User, Pwd, Port, DBName, Host, finUrl);
    }
   
    public synchronized static String selectSocietes (String id) throws SQLException
    {
        
        String nomSoc;
        rs =  instruc.executeQuery("select * from societes where idSoc = '" + id +"'");
        
        
        rs.next();
        
        try {
           nomSoc = rs.getString("NomContact");   
        } catch (Exception e) {
            nomSoc="";
        }
        
        return nomSoc;
    }
}
