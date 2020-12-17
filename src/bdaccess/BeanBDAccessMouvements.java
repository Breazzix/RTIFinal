/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdaccess;


import Classes.Mouvement;
import bdaccess.BeanBDAccess;
import static ProtocoleSUM.RequeteSUM.ConfigProperty;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author vange
 */
public class BeanBDAccessMouvements extends BeanBDAccess{
    private static ResultSet rs;
    
    public BeanBDAccessMouvements(String User, String Pwd, String Port, String DBName, String Host, String finUrl) throws SQLException, ClassNotFoundException {
        super(User, Pwd, Port, DBName, Host, finUrl);
    }
    
    public synchronized static void insertMouvements (String idTransEnt, String dateArrivee, String dest) throws SQLException
    {
        instruc.executeUpdate("INSERT INTO mouvements (IdTransEntrant,DateArrivee,destination) VALUES ('" +  idTransEnt + "','" + dateArrivee  + "','" + dest + "' )");
    }
    
    public synchronized static void insertMouvementsParc (String idContainer, String date) throws SQLException
    {
        instruc.executeUpdate("INSERT INTO mouvements (IdContainer,DateArrivee) VALUES ('" +  idContainer + "','" + date + "' )");
    }
    
    public synchronized static double [] selectMouvements(String nbEchantillon) throws SQLException
    {
        int nb = Integer.parseInt(nbEchantillon);
        double [] listeJours = new double[nb];
        String tmp;
        rs =  instruc.executeQuery("select DATEDIFF(DateDepart, DateArrivee) as jours from mouvements LIMIT " + nbEchantillon + "");
        int i=0;
        
        while(rs.next()){
                tmp = rs.getObject(1).toString();
                listeJours[i]= Integer.parseInt(tmp);
                i++;
        }
        
        for (int j=0;j<i;j++)
            System.out.println(listeJours[j]);
        return listeJours;
    }
    
    public synchronized Mouvement selectMouvement(String idCont, String idTrans) throws SQLException
    {
        Mouvement mouv;
        
        rs =  instruc.executeQuery("select IdMouv, destination from mouvements where IdContainer = '" + idCont + "' and IdTransSortant = '" + idTrans + "'");
        rs.next();
        
        
        mouv = new Mouvement(rs.getString("IdMouv"),rs.getString("destination"));
        
        
        return mouv;
    }
    
    public synchronized static String [][] selectMouvements(String nbEchantillon1,String ville1,String nbEchantillon2,String ville2) throws SQLException
    {
        int nb1 = Integer.parseInt(nbEchantillon1);
        int nb2 = Integer.parseInt(nbEchantillon2);
        int nb = nb1+nb2;
        
        String [][] listeDonnees = new String[nb][2];
        
        rs =  instruc.executeQuery("select destination,DATEDIFF(DateDepart, DateArrivee) as jours from mouvements where destination = '" + ville1 + "' LIMIT " + nbEchantillon1 + "");
        int i=0;
        
        while(rs.next()){
                listeDonnees[i][1] = rs.getObject(1).toString();
                listeDonnees[i][0] = rs.getObject(2).toString();
                i++;
        }
        
        rs =  instruc.executeQuery("select destination,DATEDIFF(DateDepart, DateArrivee) as jours from mouvements where destination = '" + ville2 + "' LIMIT " + nbEchantillon2 + "");
        
        while(rs.next()){
                listeDonnees[i][1] = rs.getObject(1).toString();
                listeDonnees[i][0] = rs.getObject(2).toString();
                i++;
        }
        
        return listeDonnees;
    }
    
    public synchronized static String [][] selectMouvements(String nbEchantillon,boolean var) throws SQLException
    {
        int nb = Integer.parseInt(nbEchantillon);
      
        
        String [][] listeDonnees = new String[nb][2];
        
        rs =  instruc.executeQuery("select destination,DATEDIFF(DateDepart, DateArrivee) as jours from mouvements  LIMIT " + nbEchantillon + "");
        int i=0;
        
        while(rs.next()){
                listeDonnees[i][1] = rs.getObject(1).toString();
                listeDonnees[i][0] = rs.getObject(2).toString();
                i++;
        }
        
        return listeDonnees;
    }
    
    
    public synchronized static String selectMouvements (String dateDebut, String dateFin, String filtre) throws SQLException, ParseException
    {
        String chaine = "";
        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy/MM/dd");
        Date date1=formatter1.parse(dateDebut);
        Date date2=formatter1.parse(dateFin);
        Date dateComp;
        rs =  instruc.executeQuery("select * from mouvements where destination = '" + filtre +"'");
        while (rs.next())
        {
            dateComp = formatter1.parse(rs.getString("DateArrivee"));
            if ((dateComp.equals(date1) || dateComp.after(date1)) && (dateComp.equals(date2) || dateComp.before(date2)))
            {
                chaine = chaine + rs.getString("IdMouv") + ConfigProperty.getConfig().getProperty("separateur");
            }
        }
        if (!"".equals(chaine))
        {
            chaine = chaine.substring(0, chaine.length() - 1);
            chaine = chaine + ConfigProperty.getConfig().getProperty("finChaine");
            chaine = "oui" + ConfigProperty.getConfig().getProperty("separateur") + chaine;
        }  
        else
            chaine = "non + aucun mouvement trouve";
   
        return chaine;
    }
}
