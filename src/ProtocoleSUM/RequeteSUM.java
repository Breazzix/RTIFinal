/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleSUM;

import Classes.Facture;
import Classes.Mouvement;
import bdaccess.BeanBDAccessFacture;
import bdaccess.BeanBDAccessItems_Facture;
import bdaccess.BeanBDAccessMouvements;
import bdaccess.BeanBDAccessPersonnel;
import bdaccess.BeanBDAccessSocietes;
import bdaccess.BeanBDAccessTransporteurs;
import java.io.*;
import java.util.*;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import requetepoolthreads.Requete;
import utilitaire.FichierConfig;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import serveurpoolthreads.ConsoleServeur;
/**
 *
 * @author vange
 */
public class RequeteSUM implements Requete, Serializable{
    private Socket socketClient;
    public static FichierConfig ConfigProperty = new FichierConfig();
    private static String codeProvider = "BC"; //CryptixCrypto";
   private PrivateKey PrivateKeyServer;
    private PublicKey PublicKeyClient;
    
    private String sepProperty;
    private String finChaine;
    
    SecretKey keySession;
    SecretKey keyHmac;
  
    public RequeteSUM(Socket s) throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, NoSuchProviderException
    {
        socketClient =s;
        int addProvider = Security.addProvider(new BouncyCastleProvider());
        
        initKeys();
        
        sepProperty = ConfigProperty.getConfig().getProperty("separateur");
        finChaine = ConfigProperty.getConfig().getProperty("finChaine");  
        
    }
    
    private void initKeys() throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, NoSuchProviderException
    {
        KeyStore ks = null;
        ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream("D:\\keystores\\keystore_serveur.pfx"),
        "password".toCharArray());
        System.out.println("Recuperation de la cle privee");
        PrivateKeyServer = (PrivateKey) ks.getKey("romain", "password".toCharArray());
        System.out.println(" *** Cle privee recuperee = " + PrivateKeyServer.toString());
        
        X509Certificate certif = (X509Certificate)ks.getCertificate("vangeitclient");
        System.out.println("Recuperation de la cle publique");
        PublicKeyClient = certif.getPublicKey();
        System.out.println("*** Cle publique recuperee = "+PublicKeyClient.toString());
        
        KeyGenerator keyGen = null;
        keyGen = KeyGenerator.getInstance("DES",codeProvider);
        keyGen.init(new SecureRandom());
        
        keySession = keyGen.generateKey();
        keyHmac = keyGen.generateKey();
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
                    }
                }
            };
    }
    
    private void traiteRequete(Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchProviderException
    {
        StringTokenizer st;
        String typeMes;
        DataInputStream dis;
        String mes;
        while (true)
        {
            try
            {
                dis = new DataInputStream(sock.getInputStream());
                mes = dis.readUTF();
                System.out.println(mes);
                
                st = new StringTokenizer(mes, FichierConfig.getConfig().getProperty("finChaine"));
                
                try {
                    st = new StringTokenizer(st.nextToken(), FichierConfig.getConfig().getProperty("separateur"));
                } catch (Exception e) {
                    st = new StringTokenizer(mes, FichierConfig.getConfig().getProperty("finChaine"));
                }
                
                
                typeMes = st.nextToken();
                
                if (typeMes.equals("LOGIN_TRAF"))
                    LoginTraf(sock, cs);
                else if (typeMes.equals("MAKE_BILL"))
                    MakeBill(st, sock, cs);
                else if (typeMes.equals("LOGIN"))
                    Login(sock,cs);
                else if (typeMes.equals("GET_NEXT_BILL"))
                    getNextBill(sock,cs);
                
            } catch (IOException ex) {
                Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
    }
    
    
    private void LoginTraf(Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException
    {
        
        String adresseDistante = sock.getRemoteSocketAddress().toString();
        // Affichage des informations
        System.out.println("Début de traiteRequeteLogin : adresse distante = " + adresseDistante);
        cs.TraceEvenements(adresseDistante+"#"+"LOGIN_TRAF"+"#"+Thread.currentThread().getName());
        
        byte b;
        String cause;
        DataInputStream dis=null; 
        DataOutputStream dos=null;
        
        try {
            dis = new DataInputStream( new BufferedInputStream(socketClient.getInputStream()));
            dos = new DataOutputStream( new BufferedOutputStream(socketClient.getOutputStream()));
            
            if (dis==null || dos==null) 
                System.exit(1);
            
            String user = dis.readUTF();
            System.out.println("Utilisateur = " + user); 
            long temps = dis.readLong(); 
            System.out.println("temps = " + temps); 
            double alea = dis.readDouble(); 
            System.out.println("Nombre aléatoire = " + alea);
            int longueur = dis.readInt(); 
            System.out.println("Longueur = " + longueur); 
            byte[] msgD = new byte[longueur]; 
            dis.readFully(msgD);
            
            BeanBDAccessPersonnel beanbd = new BeanBDAccessPersonnel(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameCompta"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url")); 
            beanbd.createStatement();
            String password = beanbd.SelectPassword(user);
            
            System.out.println("password = " + password); 
            // confection d'un digest local 
            MessageDigest md = MessageDigest.getInstance("SHA-1", codeProvider); 
            md.update(user.getBytes()); md.update(password.getBytes()); 
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
            DataOutputStream bdos = new DataOutputStream(baos); 
            bdos.writeLong(temps); bdos.writeDouble(alea); 
            md.update(baos.toByteArray()); 
            byte[] msgDLocal = md.digest();
            System.out.println("alzzz"); 
            // comparaison String réponse; 
            if (MessageDigest.isEqual(msgD, msgDLocal) ) {
                cause = "OK - vouss etes connecte au serveur";
                System.out.println("Le client " + user + " est connecté au serveur");
            } 
            else {
                cause = "Sorry - votre demande de connexion est refusee"; 
                System.out.println("Le client " + user + " est refusé");
            }
            reponse(cause,sock); 
        } 
        catch (IOException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void MakeBill(StringTokenizer st,Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException
    {
         String adresseDistante = sock.getRemoteSocketAddress().toString();
        // Affichage des informations
        System.out.println("Début de traiteRequeteLogin : adresse distante = " + adresseDistante);
        cs.TraceEvenements(adresseDistante+"#"+"MAKE_BILL"+"#"+Thread.currentThread().getName());
        
        String idTransport = st.nextToken();
        LinkedList<String> listeContainers = new LinkedList<String>();
        for (int i = 0;st.hasMoreElements();i++) {
            listeContainers.add((String) st.nextElement());
        }
        
        BeanBDAccessMouvements beanbdMouv = new BeanBDAccessMouvements(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameMouvements"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url")); 
        BeanBDAccessTransporteurs beanbdTrans = new BeanBDAccessTransporteurs(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameMouvements"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url"));
        BeanBDAccessFacture beanbdFactures = new BeanBDAccessFacture(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameCompta"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url"));
        BeanBDAccessItems_Facture beanbdItemFacture = new BeanBDAccessItems_Facture(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameCompta"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url"));
        
        Mouvement mouv ;
        String idSoc, idFacture;
        Date actuelle = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        String date = dateFormat.format(actuelle);
        
        for (int i=0;i<listeContainers.size();i++)
        {
            beanbdMouv.createStatement();
            mouv = beanbdMouv.selectMouvement(listeContainers.get(i),idTransport);
            beanbdTrans.createStatement();
            idSoc = beanbdTrans.selectSocProp(idTransport);
            beanbdFactures.createStatement();
            idFacture = beanbdFactures.insertFacture(idSoc, date);
            beanbdItemFacture.createStatement();
            System.out.println("idFacture = " + idFacture);
            System.out.println("idMouv = " + mouv.getIdMouv());
            System.out.println("idContainer = " + listeContainers.get(i));
            System.out.println("destination = " + mouv.getDestination());
            beanbdItemFacture.insertItemFacture(idFacture, mouv.getIdMouv(), listeContainers.get(i), mouv.getDestination());
        }
        reponse("oui", sock);
        
    }
    
    private void Login(Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        
        String adresseDistante = sock.getRemoteSocketAddress().toString();
        // Affichage des informations
        System.out.println("Début de traiteRequeteLogin : adresse distante = " + adresseDistante);
        cs.TraceEvenements(adresseDistante+"#"+"LOGIN"+"#"+Thread.currentThread().getName());
        
        byte b;
        String cause;
        DataInputStream dis=null; 
        DataOutputStream dos=null;
        
        try {
            dis = new DataInputStream( new BufferedInputStream(socketClient.getInputStream()));
            dos = new DataOutputStream( new BufferedOutputStream(socketClient.getOutputStream()));
            
            if (dis==null || dos==null) 
                System.exit(1);
            
            String user = dis.readUTF();
            System.out.println("Utilisateur = " + user); 
            long temps = dis.readLong(); 
            System.out.println("temps = " + temps); 
            double alea = dis.readDouble(); 
            System.out.println("Nombre aléatoire = " + alea);
            int longueur = dis.readInt(); 
            System.out.println("Longueur = " + longueur); 
            byte[] msgD = new byte[longueur]; 
            dis.readFully(msgD);
            
            BeanBDAccessPersonnel beanbd = new BeanBDAccessPersonnel(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameCompta"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url")); 
            beanbd.createStatement();
            String password = beanbd.SelectPassword(user);
            
            // confection d'un digest local 
            MessageDigest md = MessageDigest.getInstance("SHA-1", codeProvider); 
            md.update(user.getBytes()); md.update(password.getBytes()); 
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
            DataOutputStream bdos = new DataOutputStream(baos); 
            bdos.writeLong(temps); bdos.writeDouble(alea); 
            md.update(baos.toByteArray()); 
            byte[] msgDLocal = md.digest();
            
            // comparaison String réponse; 
            if (MessageDigest.isEqual(msgD, msgDLocal) ) {
                cause = "oui";
                
                Cipher chiffrement = Cipher.getInstance("RSA");
                
                chiffrement.init(Cipher.ENCRYPT_MODE, PublicKeyClient);
                byte[] cleSessionCryptee = chiffrement.doFinal(keySession.getEncoded());
                byte[] cleHmacCryptee = chiffrement.doFinal(keyHmac.getEncoded());
                
                dos.writeUTF(cause);
                
                dos.writeInt(cleSessionCryptee.length);
                dos.write(cleSessionCryptee);
                dos.writeInt(cleHmacCryptee.length);
                dos.write(cleHmacCryptee);
                
                dos.flush();
            } 
            else {
                cause = "non"; 
                System.out.println("Le client " + user + " est refusé");
                
                reponse(cause, sock);
            }
        } 
        catch (IOException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getNextBill(Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException
    {
        String adresseDistante = sock.getRemoteSocketAddress().toString();
        // Affichage des informations
        System.out.println("Début de traiteRequeteLogin : adresse distante = " + adresseDistante);
        cs.TraceEvenements(adresseDistante+"#"+"NEXTBILL"+"#"+Thread.currentThread().getName());
        DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
     
        
        BeanBDAccessFacture beanbd = new BeanBDAccessFacture(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameCompta"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url")); 
        beanbd.createStatement();
        Facture lastFact = beanbd.getLastFacture();
        
        if (!lastFact.equals(null))
        {
             BeanBDAccessSocietes beanbdSoc = new BeanBDAccessSocietes(ConfigProperty.getConfig().getProperty("user"), ConfigProperty.getConfig().getProperty("pwd"), ConfigProperty.getConfig().getProperty("portbd"), ConfigProperty.getConfig().getProperty("dbnameMouvements"), ConfigProperty.getConfig().getProperty("adresse"), ConfigProperty.getConfig().getProperty("url")); 
            beanbdSoc.createStatement();
            String nomSociete = beanbdSoc.selectSocietes(lastFact.getSociete());

            lastFact.setSociete(nomSociete);
            
            Cipher chiffrement = Cipher.getInstance("DES/ECB/PKCS5Padding",codeProvider);
            chiffrement.init(Cipher.ENCRYPT_MODE, keySession);
            byte[] texteClair = lastFact.toString().getBytes();
            byte[] texteCrypté = chiffrement.doFinal(texteClair);
            String texteCryptéAff = new String (texteCrypté);
            System.out.println(new String(texteClair) + " ---> " + texteCryptéAff);
            
            dos.writeUTF("oui");
                
            dos.writeInt(texteCrypté.length);
            dos.write(texteCrypté);
            
        }
        else
        {
            dos.writeUTF("non");
        }
        dos.flush();
    }
    
    public void reponse(String cause,Socket sock)
    {
        // Construction d'une réponse
        DataOutputStream dos;
        try
        {
            dos = new DataOutputStream(sock.getOutputStream());
            
            dos.writeUTF(cause);
            dos.flush();
            //dos.close();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }
}
