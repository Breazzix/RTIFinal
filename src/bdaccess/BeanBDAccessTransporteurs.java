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
public class BeanBDAccessTransporteurs extends BeanBDAccess{
    private static ResultSet rs;
    
    public BeanBDAccessTransporteurs(String User, String Pwd, String Port, String DBName, String Host, String finUrl) throws SQLException, ClassNotFoundException {
        super(User, Pwd, Port, DBName, Host, finUrl);
    }
    
    public synchronized String selectSocProp(String idTrans) throws SQLException
    {
        String idSoc;
        rs =  instruc.executeQuery("select idSocProp from transporteurs where idTrans =  '" + idTrans + "'");
        
        rs.next();
        
        idSoc = rs.getString("idSocProp");
        
       
        return idSoc;
    }
}
