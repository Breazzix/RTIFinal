/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdaccess;

import java.sql.*;

/**
 *
 * @author vange
 */
public class BeanBDAccess {
    protected String user;
    protected String password;
    protected String port;
    protected String dbName;
    protected String hostName;
    protected Connection connexion;
    protected static Statement instruc;
    
    
    public BeanBDAccess (String User, String Pwd, String Port, String DBName, String Host, String finUrl) throws SQLException, ClassNotFoundException
    {
        user = User;
        password = Pwd;
        port = Port;
        dbName = DBName;
        hostName = Host;
       
        String url = "jdbc:mysql://" + hostName + ":" + port + "/" + dbName + finUrl;
        
        Class.forName("com.mysql.jdbc.Driver");   
        connexion = DriverManager.getConnection(url, user, password);
    }
    
    public void createStatement() throws SQLException
    {
        instruc = connexion.createStatement();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Connection getConnexion() {
        return connexion;
    }

    public void setConnexion(Connection connexion) {
        this.connexion = connexion;
    }

    public Statement getInstruc() {
        return instruc;
    }

    public void setInstruc(Statement instruc) {
        this.instruc = instruc;
    }
    
}
