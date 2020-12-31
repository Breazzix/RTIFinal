/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleSUM;

import Classes.Facture;
import Classes.Mouvement;
import Classes.emplacement;
import static ProtocoleSUM.RequeteSUM.ConfigProperty;
import bdaccess.BeanBDAccessDestination;
import bdaccess.BeanBDAccessFacture;
import bdaccess.BeanBDAccessItems_Facture;
import bdaccess.BeanBDAccessMouvements;
import bdaccess.BeanBDAccessParc;
import bdaccess.BeanBDAccessPersonnel;
import bdaccess.BeanBDAccessSocietes;
import bdaccess.BeanBDAccessTransporteurs;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import requetepoolthreads.Requete;
import serveurpoolthreads.ConsoleServeur;
import utilitaire.FichierConfig;

/**
 *
 * @author Nicolas
 */
public class RequeteSUM_Cont implements Requete, Serializable{
    private Socket socketClient;
    public static FichierConfig ConfigProperty = new FichierConfig();
    
    private String sepProperty;
    private String finChaine;

  
    public RequeteSUM_Cont(Socket s) throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, NoSuchProviderException
    {
        socketClient =s;
        
        sepProperty = ConfigProperty.getConfig().getProperty("separateur");
        finChaine = ConfigProperty.getConfig().getProperty("finChaine");  
        
    }
    
    
    public Runnable createRunnable (final Socket s, final ConsoleServeur cs)
    {
        
        return new Runnable()
            {
                public void run()
                {
                    try {
                        traiteRequete(s, cs);
                    } catch (SQLException ex) {
                        Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchPaddingException ex) {
                        Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidKeyException ex) {
                        Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalBlockSizeException ex) {
                        Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (BadPaddingException ex) {
                        Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchProviderException ex) {
                        Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SignatureException ex) {
                        Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(RequeteSUM_Cont.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
    }
    
    private void traiteRequete(Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, IOException
    {
        StringTokenizer st;
        String typeMes;
        DataInputStream dis = new DataInputStream(sock.getInputStream());
        String mes;
        
        StringBuffer message = new StringBuffer(); 
        
         byte b = 0; 
        int cpt = 0; 
        
        
        while (true) 
        {
            boolean fini = false; 
           
            do 
            {
                message.setLength(0); 
                try 
                {
                   
                    while((b = dis.readByte())!= (byte)'#') 
                    {
                        if (b != 0) 
                            message.append((char) b); 
                        System.out.println("byte reçu = " + b);
                    }
                } 
                catch (IOException e) { System.out.println("Erreur de lecture = " + e.getMessage());}

                System.out.println("dernier byte reçu = "+b); 
                cpt++; 
                System.out.println (cpt + ". Message reçu = " + message.toString().trim());  
                fini = true; 
                System.out.println("fini = " + fini); 
            } while (!fini);

            st = new StringTokenizer(message.toString(), sepProperty);                
            typeMes = st.nextToken();

            if (typeMes.equals("LOGIN_CONT"))
                LoginCont (st, sock, cs);
            if (typeMes.equals("GET_XY"))
                GetXy (st, sock, cs);
            if (typeMes.equals("SEND_WEIGHT"))
                SendWeight(st,sock,cs);
            if (typeMes.equals("GET_LIST"))
                GetList(st,sock,cs);
            if (typeMes.equals("SIGNAL_DEP"))
                SignalDep(st,sock,cs);
        } 
    }
    
    private void LoginCont(StringTokenizer st, Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException
    {
        
        String adresseDistante = sock.getRemoteSocketAddress().toString();
        // Affichage des informations
        System.out.println("Début de traiteRequeteLoginCont : adresse distante = " + adresseDistante);
        cs.TraceEvenements(adresseDistante+"#"+"LOGIN_CONT"+"#"+Thread.currentThread().getName());
        
        String username = st.nextToken();
        String pwd = st.nextToken();
        
        BeanBDAccessPersonnel beanbd = new BeanBDAccessPersonnel(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameCompta"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url")); 
        beanbd.createStatement();
        
        if (beanbd.SelectPersonnel (username, pwd) != 0)
        {
            reponse ("oui", sock);
        }
        else
            reponse("non", sock);
        
    }
    
    private void GetXy(StringTokenizer st, Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException
    {
        
        String adresseDistante = sock.getRemoteSocketAddress().toString();
        // Affichage des informations
        System.out.println("Début de traiteRequeteLoginCont : adresse distante = " + adresseDistante);
        cs.TraceEvenements(adresseDistante+"#"+"GET_XY"+"#"+Thread.currentThread().getName());
        
        String numSociete = st.nextToken();
        String numImmat = st.nextToken();
        String idContainer = st.nextToken();
        String destination = st.nextToken();
        
        BeanBDAccessDestination beanbdDestination = new BeanBDAccessDestination(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameMouvements"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url")); 
        beanbdDestination.createStatement();
        
        int dest = beanbdDestination.selectDestination(destination);
        
        BeanBDAccessParc beanbd = new BeanBDAccessParc(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameMouvements"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url")); 
        beanbd.createStatement();
        
        String reponse = beanbd.getEmplacement(numSociete,numImmat,idContainer,dest);
        
       
        reponse(reponse, sock);
        
    }
    
    private void SendWeight(StringTokenizer st, Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException
    {
        
        String adresseDistante = sock.getRemoteSocketAddress().toString();
        // Affichage des informations
        System.out.println("Début de traiteRequeteLoginCont : adresse distante = " + adresseDistante);
        cs.TraceEvenements(adresseDistante+"#"+"SEND_WEIGHT"+"#"+Thread.currentThread().getName());
        
        String idContainer = st.nextToken();
        String x = st.nextToken();
        String y = st.nextToken();
        String poids = st.nextToken();
        
        BeanBDAccessParc beanbd = new BeanBDAccessParc(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameMouvements"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url")); 
        beanbd.createStatement();
        
        int ok = beanbd.updateWeight(idContainer,x,y,poids);
        
        String reponse="";
        if (ok == 1)
            reponse = "oui";
        else
            reponse = "non";
       
        reponse(reponse, sock);
        
    }
    private void GetList(StringTokenizer st, Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException
    {
        
        String adresseDistante = sock.getRemoteSocketAddress().toString();
        // Affichage des informations
        System.out.println("Début de traiteRequeteLoginCont : adresse distante = " + adresseDistante);
        cs.TraceEvenements(adresseDistante+"#"+"GET_LIST"+"#"+Thread.currentThread().getName());
        
        String idTransport = st.nextToken();
        String destination = st.nextToken();
        String nbMaxCont = st.nextToken();
        
        BeanBDAccessDestination beanbdDestination = new BeanBDAccessDestination(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameMouvements"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url")); 
        beanbdDestination.createStatement();
        
        int dest = beanbdDestination.selectDestination(destination);
        
        BeanBDAccessParc beanbd = new BeanBDAccessParc(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameMouvements"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url")); 
        beanbd.createStatement();
        
        LinkedList<emplacement> listeEmplacement = new LinkedList<emplacement>();
        
        listeEmplacement = beanbd.getListeEmplacements(idTransport,dest,nbMaxCont);
        
        String chaine = "";
        
        if (listeEmplacement!=null)
        {
            chaine = "oui"+sepProperty;
            for (int i=0;i<listeEmplacement.size();i++)
            {
                chaine = chaine + listeEmplacement.get(i) + sepProperty;
            }
            chaine = chaine.substring(0, chaine.length() - 1);
        }
        else
            chaine = "non"+sepProperty+"pas de container !";
        
       
        reponse(chaine, sock);
        
    }
    
    private void SignalDep(StringTokenizer st, Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException
    {
        String adresseDistante = sock.getRemoteSocketAddress().toString();
        // Affichage des informations
        System.out.println("Début de traiteRequeteLoginCont : adresse distante = " + adresseDistante);
        cs.TraceEvenements(adresseDistante+"#"+"SIGNAL_DEP"+"#"+Thread.currentThread().getName());
    }
    
    public void reponse(String cause, Socket sock)
    {
        // Construction d'une réponse
        DataOutputStream dos;
        try
        {
            dos = new DataOutputStream(sock.getOutputStream());
            
            dos.write(cause.getBytes());
            dos.flush();
            //dos.close();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }
}
