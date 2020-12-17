/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitaire;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

/**
 *
 * @author vange
 */
public class FichierConfig {
    
    private static Properties config;
    public static String test;
    
    public FichierConfig () {
        loadConfig();
    }

    /**
     * @return the config
     */
    public static Properties getConfig() {
        return config;
    }

    /**
     * @param aConfig the config to set
     */
    public static void setConfig(Properties aConfig) {
        config = aConfig;
    }
    

    public static String getNomsFichs(String str)
    {
        String fileName = new String();

        switch (str) {
            case "config":
                fileName = System.getProperty("user.dir") + System.getProperty("file.separator")+ "Config.properties";
                break;
            case "erreur":
                fileName = System.getProperty("user.dir") + System.getProperty("file.separator") + "images" + System.getProperty("file.separator") + "Err.jpg";
                break;
            case "login":
                fileName = System.getProperty("user.dir") + System.getProperty("file.separator") + "login.properties";
                break;
        }
        
        return fileName;
    }
    
    public static void creeConfig () throws FileNotFoundException, IOException {
        String portMouvements = "60001";
        String portCompta = "60002";
        String portIn = "60003";
        String maxClients = "3";
        String Adresse = "192.168.1.1";
        String sep = "|";
        String fc = "#";
        String MaxLig = "5";
        String MaxCol = "5";
        String user= "root";
        String pwd="";
        String portbd= "3306";
        String dbname1= "BD_Compta";
        String dbname2="bd_mouvements";
        String host= "127.0.0.1";
        String url= "?zeroDateTimeBehavior=convertToNull";
        String driver ="com.mysql.jdbc.Driver";
        
       
        config.setProperty("portMouvements", portMouvements);
        config.setProperty("portCompta", portCompta);
        config.setProperty("portIn", portIn);
        config.setProperty("clients", maxClients);
        config.setProperty("adresse", Adresse);
        config.setProperty("separateur", sep);
        config.setProperty("finChaine", fc);
        config.setProperty("maxLig", MaxLig);
        config.setProperty("maxCol", MaxCol);
        config.setProperty("user", user);
        config.setProperty("pwd", pwd);
        config.setProperty("portbd", portbd);
        config.setProperty("dbnameCompta", dbname1);
        config.setProperty("dbnameMouvements", dbname2);
        config.setProperty("host", host);
        config.setProperty("url", url);
        config.setProperty("driver", driver);
        
        
        config.store(new FileOutputStream(getNomsFichs("config")), null);
    }
    
    
     public static void loadConfig() {
        config = new Properties();
        
        try  {
            config.load(new FileInputStream(getNomsFichs("config")));
        } 
        catch(FileNotFoundException e) { 
            System.out.println("Fichier Config.properties non trouvé !");
            try {
                creeConfig ();
            } 
            catch (FileNotFoundException ex) { System.out.println("Fichier Config.properties non trouvé !"); } 
            catch (IOException ex) { System.out.println("Error: "+ ex.getMessage()); }
        }
        catch(IOException e) { System.out.println("Error: "+ e.getMessage()); }
    }
  
}
