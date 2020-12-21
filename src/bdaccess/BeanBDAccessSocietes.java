/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdaccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

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
    
    public synchronized static String selectSociete (String nomSociete) throws SQLException
    {
        
        String idSoc;
        rs =  instruc.executeQuery("select * from societes where NomContact = '" + nomSociete +"'");
        
        
        rs.next();
        
        try {
           idSoc = rs.getString("idSoc");   
        } catch (Exception e) {
            idSoc="";
        }
        
        return idSoc;
    }
    
    
    
    public synchronized static LinkedList<String> getListeSocietes () throws SQLException
    {
        
        LinkedList<String> listeSocietes = new LinkedList<String>();
        String tmp;
        
        rs =  instruc.executeQuery("select * from societes");
        
        while(rs.next()){
            tmp = rs.getString("NomContact");
            listeSocietes.add(tmp);
        }
        
        return listeSocietes;
    }
    
    public synchronized int SelectSocieteByName (String name) throws SQLException
    {
        int id = -1;
        
        rs = instruc.executeQuery("select * from societes where Upper (NomContact) = Upper('" + name + "')");
        
        if (rs.next())
        {
            id = rs.getInt("idSoc");
        }
        return id;
    }
}

