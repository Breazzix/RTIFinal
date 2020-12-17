/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application_Compta;

import ProtocoleSUM.*;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.RootPaneContainer;
import utilitaire.BaseException;
import utilitaire.FichierConfig;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
/**
 *
 * @author vange
 */
public class Login extends javax.swing.JDialog {

    private java.awt.Frame parent;

    private String codeProvider = "BC";
    public static FichierConfig ConfigProperty = new FichierConfig();
    
    
    private String sepProperty;
    private String finChaine;
    private String adresse;
    private int port;
    
    private PrivateKey PrivateKeyClient;
    private PublicKey PublicKeyServer;
    private SecretKey cleSession;
    private SecretKey cleHmac;

    /**
     * Creates new form Login
     */
    public Login(/*java.awt.Frame parent, boolean modal, boolean inscription*/) throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException {
        //super(parent, modal);
        initComponents();
        
        jTextFieldUserName.setText("romvgt99");
        jPasswordField.setText("123");
        //setLocation(parent);
        this.setTitle("Login");
        
        sepProperty = ConfigProperty.getConfig().getProperty("separateur");
        finChaine = ConfigProperty.getConfig().getProperty("finChaine");  
        
        int addProvider = Security.addProvider(new BouncyCastleProvider());
        
        initKeys();
        
         if (Security.getProvider("BC") == null){
            System.out.println("Bouncy Castle provider is NOT available");
        }
        else{
            System.out.println("Bouncy Castle provider is available");
        }
    }

    private void setLocation(java.awt.Frame parent) {
        int posX = parent.getLocation().x + ((RootPaneContainer) parent).getContentPane().getSize().height / 2;
        this.setLocation(posX,  parent.getLocation().y + 100);
    }
    
    private void initKeys() throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException
    {
        KeyStore ks = null;
        ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream("D:\\keystores\\keystore_client.pfx"),
        "password".toCharArray());
        System.out.println("Recuperation de la cle privee");
        PrivateKeyClient = (PrivateKey) ks.getKey("vangeitclient", "password".toCharArray());
        System.out.println(" *** Cle privee recuperee = " + PrivateKeyClient.toString());
        
        X509Certificate certif = (X509Certificate)ks.getCertificate("vangeitserveur");
        System.out.println("Recuperation de la cle publique");
        PublicKeyServer = certif.getPublicKey();
        System.out.println("*** Cle publique recuperee = "+PublicKeyServer.toString());
    }
    
    public void verifLogin() throws BaseException, IOException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        String user = jTextFieldUserName.getText();
        String pswd = new String(jPasswordField.getPassword());
        String req = "LOGIN"+finChaine;
        
        if (!user.equals("") || !pswd.equals(""))
        {
            Socket cliSock;
           
            adresse = ConfigProperty.getConfig().getProperty("adresse");
            port = Integer.parseInt(ConfigProperty.getConfig().getProperty("portCompta"));
            cliSock = new Socket(adresse, port);
            DataInputStream dis= new DataInputStream(cliSock.getInputStream());
            DataOutputStream dos= new DataOutputStream(cliSock.getOutputStream());
            
            try {
                //int c; 
                //byte b; 
                //StringBuffer msgClient = new StringBuffer();
                //System.out.println("Message à envoyer au serveur : "); 
 
                System.out.println("Instanciation du message digest"); 
                MessageDigest md = MessageDigest.getInstance("SHA-1", codeProvider); 
                md.update(user.getBytes()); 
                md.update(pswd.getBytes());
                
                long temps = (new Date()).getTime(); 
                double alea = Math.random(); 
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream bdos = new DataOutputStream(baos); 
                
                dos.writeUTF(req);
                bdos.writeLong(temps); 
                bdos.writeDouble(alea);
                
                md.update(baos.toByteArray()); 
                byte[] msgD = md.digest();
                System.out.println("Envoi du message digest"); 
               
                dos.writeUTF(user); 
                dos.writeLong(temps); 
                dos.writeDouble(alea); 
                dos.writeInt(msgD.length); 
                dos.write(msgD);
                
                String reponse = dis.readUTF(); 
                System.out.println("Réponse du serveur = " + reponse); 
                
                if (reponse.equals("oui"))
                { 
                    int size;
                    byte[] msgCleSession;
                    size = dis.readInt();
                    msgCleSession = new byte[size];
                    dis.readFully(msgCleSession);
                    
                    byte[] msgCleHmac;
                    size = dis.readInt();
                    msgCleHmac = new byte[size];
                    dis.readFully(msgCleHmac);
                    Cipher chiffrementd = Cipher.getInstance("RSA");
                    chiffrementd.init(Cipher.DECRYPT_MODE, PrivateKeyClient);
                    byte[] msgClairSession = chiffrementd.doFinal(msgCleSession);
                    cleSession = new SecretKeySpec(msgClairSession, 0,msgClairSession.length,"DES");
                    
                    byte[] msgClairHmac = chiffrementd.doFinal(msgCleHmac);
                    cleHmac = new SecretKeySpec(msgClairHmac, 0, msgClairHmac.length,"DES");
                    
                    Applic_Data_Analysis fen = new Applic_Data_Analysis(cliSock, PrivateKeyClient, PublicKeyServer, cleSession, cleHmac);
                    fen.setVisible(true);
                    this.dispose();
                }
                else
                {
                    throw new BaseException(this.parent, "donnees encodees incorrectes !");
                }
                
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchProviderException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldUserName = new javax.swing.JTextField();
        jButtonValider = new javax.swing.JButton();
        jButtonAnnuler = new javax.swing.JButton();
        jPasswordField = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("LOGIN");

        jLabel2.setText("Nom d'utilisateur:");

        jLabel3.setText("Mot de passe:");

        jTextFieldUserName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldUserNameActionPerformed(evt);
            }
        });

        jButtonValider.setText("Valider");
        jButtonValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonValiderActionPerformed(evt);
            }
        });

        jButtonAnnuler.setText("Annuler");
        jButtonAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnnulerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(77, 77, 77)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                            .addComponent(jPasswordField)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(jButtonValider)
                        .addGap(72, 72, 72)
                        .addComponent(jButtonAnnuler))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(jLabel1)))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel1)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonValider)
                    .addComponent(jButtonAnnuler))
                .addGap(38, 38, 38))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldUserNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldUserNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldUserNameActionPerformed

    private void jButtonValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonValiderActionPerformed
        
        try 
        {
             verifLogin();
        }
        catch (BaseException e)
        {
            System.out.println(e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonValiderActionPerformed

    private void jButtonAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnnulerActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButtonAnnulerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Login dialog;
                try {
                    dialog = new Login();
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CertificateException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                } catch (KeyStoreException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnrecoverableKeyException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAnnuler;
    private javax.swing.JButton jButtonValider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JTextField jTextFieldUserName;
    // End of variables declaration//GEN-END:variables

 
    
}
