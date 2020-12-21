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
public class BeanBDAccessPersonnel extends BeanBDAccess{
    private static ResultSet rs;
    public BeanBDAccessPersonnel(String User, String Pwd, String Port, String DBName, String Host, String finUrl) throws SQLException, ClassNotFoundException {
        super(User, Pwd, Port, DBName, Host, finUrl);
    }
   
    public synchronized static int SelectPersonnel (String user, String mdp) throws SQLException
    {
        int trouve = 0;
        
        rs =  instruc.executeQuery("select * from personnel where Login = '" + user +"' and Password = '" + mdp +"'");
        
        
        rs.next();
        
        try {
          trouve = rs.getInt("Matricule");   
        } catch (Exception e) {
            trouve = 0;
        }
        
        return trouve;
    }
    
    public synchronized String SelectPassword (String user) throws SQLException
    {
        String pwd=null;
        
        rs =  instruc.executeQuery("select * from personnel where Login = '" + user +"'");
        
        rs.next();
        
        try {
            pwd = rs.getString("Password");
        } catch (Exception e) {
            pwd = "non";
        }
        
        return pwd;
    }
    
    public synchronized int selectId (String user) throws SQLException
    {
        int id=1;
        
        rs =  instruc.executeQuery("select * from personnel where Login = '" + user +"'");
        
        rs.next();
        
        try {
            id = rs.getInt("Matricule");
        } catch (Exception e) {
            id = 1;
        }
        
        return id;
    }
}
