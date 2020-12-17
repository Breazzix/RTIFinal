/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serveur_Mouvements;

import Serveur_Compta.*;
import serveurpoolthreads.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.net.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import utilitaire.*;
/**
 *
 * @author vange
 */
public class FenServeurMouvements extends javax.swing.JFrame implements ConsoleServeur {

    /**
     * Creates new form FenServeurMouvements
     */
    private int port;
    public static FichierConfig ConfigProperty = new FichierConfig();
    private Object[] col = { "Origine", "Requête", "Thread"};
    private DefaultTableModel modelTable = new DefaultTableModel(col, 0);
    ThreadServeur ts;
    private String sepProperty;
    private String finChaine;
    private String adresse;
    private String codeProvider = "BC";
    private String User;
    private String Password;
    private Socket cliSock;
    
    public FenServeurMouvements(){
        initComponents();
        this.setTitle("Serveur Mouvements");
        TraceEvenements("serveur#initialisation#main");
        
        BFacture.setEnabled(false);
        
        sepProperty = ConfigProperty.getConfig().getProperty("separateur");
        finChaine = ConfigProperty.getConfig().getProperty("finChaine"); 
        
        User = "romvgt99";
        Password = "123";
        
        int addProvider = Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButtonStart = new javax.swing.JButton();
        jButtonStop = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableauEvenements = new javax.swing.JTable();
        BLogin = new javax.swing.JButton();
        PanelBill = new javax.swing.JPanel();
        BFacture = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Serveur");

        jButtonStart.setText("start");
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });

        jButtonStop.setText("stop");
        jButtonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopActionPerformed(evt);
            }
        });

        TableauEvenements.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Origine", "Requête", "Thread"
            }
        ));
        jScrollPane2.setViewportView(TableauEvenements);

        BLogin.setText("Login");
        BLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BLoginActionPerformed(evt);
            }
        });

        BFacture.setText("Facture");
        BFacture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFactureActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelBillLayout = new javax.swing.GroupLayout(PanelBill);
        PanelBill.setLayout(PanelBillLayout);
        PanelBillLayout.setHorizontalGroup(
            PanelBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBillLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(BFacture)
                .addContainerGap())
        );
        PanelBillLayout.setVerticalGroup(
            PanelBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBillLayout.createSequentialGroup()
                .addComponent(BFacture)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(jButtonStart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonStop)
                .addGap(86, 86, 86)
                .addComponent(PanelBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(143, 143, 143)
                        .addComponent(BLogin)
                        .addGap(40, 40, 40))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(BLogin))
                .addGap(55, 55, 55)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonStop)
                        .addComponent(jButtonStart))
                    .addComponent(PanelBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed
        // TODO add your handling code here:
        port = Integer.parseInt(ConfigProperty.getConfig().getProperty("portIn"));
        System.out.println("port:" + port);
        TraceEvenements("serveur#acquisition du port#main");
        ts = new ThreadServeur(port,"In", new ListeTaches(), this);
        ts.start();
    }//GEN-LAST:event_jButtonStartActionPerformed

    private void jButtonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStopActionPerformed
        try {
            ts.fermer();
        } catch (IOException ex) {
            Logger.getLogger(FenServeurMouvements.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonStopActionPerformed

    private void BLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BLoginActionPerformed
        try {
            verifLogin();
        } catch (BaseException ex) {
            Logger.getLogger(FenServeurMouvements.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FenServeurMouvements.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FenServeurMouvements.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(FenServeurMouvements.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_BLoginActionPerformed

    private void BFactureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BFactureActionPerformed
        DialogFacture fen = new DialogFacture(this,true,cliSock);
        fen.setVisible(true);
    }//GEN-LAST:event_BFactureActionPerformed

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
            java.util.logging.Logger.getLogger(FenServeurMouvements.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FenServeurMouvements.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FenServeurMouvements.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FenServeurMouvements.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FenServeurMouvements().setVisible(true);
            }
        });
    }
    
    public void TraceEvenements(String commentaire)
    {
        Vector ligne = new Vector();
        StringTokenizer parser = new StringTokenizer(commentaire,"#");
        while (parser.hasMoreTokens())
        ligne.add(parser.nextToken());
       
        modelTable.insertRow(modelTable.getRowCount(),ligne);
        TableauEvenements.setModel(modelTable);
    }
    
    public void verifLogin() throws BaseException, IOException, NoSuchAlgorithmException, NoSuchProviderException
    {
        String user = User;
        String pswd = Password;
        String req = "LOGIN_TRAF"+finChaine;

        adresse = ConfigProperty.getConfig().getProperty("adresse");
        port = Integer.parseInt(ConfigProperty.getConfig().getProperty("portMouvements"));
        cliSock = new Socket(adresse, port);
        DataInputStream dis= new DataInputStream(cliSock.getInputStream());
        DataOutputStream dos= new DataOutputStream(cliSock.getOutputStream());

        try {
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

            if (reponse.equals("OK - vouss etes connecte au serveur"))
            { 
                BFacture.setEnabled(true);
                BLogin.setEnabled(false);
            }
            

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FenServeurMouvements.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(FenServeurMouvements.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BFacture;
    private javax.swing.JButton BLogin;
    private javax.swing.JPanel PanelBill;
    public static javax.swing.JTable TableauEvenements;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JButton jButtonStop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
