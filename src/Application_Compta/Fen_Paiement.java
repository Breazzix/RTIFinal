/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application_Compta;

import static Application_Compta.Applic_Compta.ConfigProperty;
import static Application_Compta.Applic_Compta.cliSock;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

/**
 *
 * @author Nicolas
 */
public class Fen_Paiement extends javax.swing.JDialog {

    /**
     * Creates new form Fen_Paiement
     * @param parent
     */
    private String codeProvider = "BC";
    public static Socket cliSock;
    
    private String sepProperty;
    private String finChaine;
    
    private static PrivateKey pkClient;
    private static PublicKey pkServer;
    private static SecretKey keySession;
    private static SecretKey keyHmac;
    
    public Fen_Paiement(java.awt.Frame parent, boolean modal, Socket sock, PrivateKey privateKeyClient, PublicKey publicKeyServer, SecretKey cleSession, SecretKey cleHmac) {
        super(parent, modal);
        initComponents();
        
        cliSock = sock;
        sepProperty = ConfigProperty.getConfig().getProperty("separateur");
        finChaine = ConfigProperty.getConfig().getProperty("finChaine");  
        
        pkClient = privateKeyClient;
        pkServer = publicKeyServer;
        keySession = cleSession;
        keyHmac = cleHmac;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton_OK = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField_NF = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField_Montant = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField_CB = new javax.swing.JTextField();
        jButton_Fermer = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField_réponse = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton_OK.setText("Ok");
        jButton_OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_OKActionPerformed(evt);
            }
        });

        jLabel1.setText("Numéro facture :");

        jLabel2.setText("Montant :");

        jLabel3.setText("Numéro de compte :");

        jButton_Fermer.setText("Fermer");
        jButton_Fermer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_FermerActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel4.setText("Enregistrement du payement ");

        jLabel5.setText("Réponse :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(64, 64, 64))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jTextField_réponse, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jButton_Fermer)
                .addGap(52, 52, 52))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField_NF, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(87, 87, 87)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField_Montant, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField_CB, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addComponent(jButton_OK)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField_NF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField_Montant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField_CB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jButton_OK)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Fermer)
                    .addComponent(jLabel5)
                    .addComponent(jTextField_réponse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_OKActionPerformed
       String Message = null;
         
         DataOutputStream dos=null; DataInputStream dis=null;
         
         String nf = jTextField_NF.getText();
         String montant = jTextField_Montant.getText();
         String cb = jTextField_CB.getText();
         
         if (!nf.equals("") && !montant.equals("") && !cb.equals(""))
         {
            Message = "REC_PAY"+ sepProperty + nf + sepProperty + montant + sepProperty + cb + finChaine;
                     
            try 
            {
                System.out.println(cliSock.getInetAddress().toString()); 
                dos = new DataOutputStream(cliSock.getOutputStream()); 
                dis = new DataInputStream(cliSock.getInputStream());
            } catch (UnknownHostException e) { System.err.println("Erreur ! Host non trouvÃ© [" + e + "]"); 
            } catch (IOException e) { System.err.println("Erreur ! Pas de connexion ? [" + e + "]");}
            
            if (cliSock == null || dis==null || dos==null) 
                System.exit(1);
            try 
            {
                System.out.println("Message a envoyer au serveur : " + Message); 
                byte[] message = Message.getBytes();
                 
                System.out.println(" *** Cle secrÃ¨te rÃ©cupÃ©rÃ©e = " + keyHmac.toString());
                System.out.println("Instanciation du HMAC"); 
                Mac hmac = Mac.getInstance("HMAC-MD5", codeProvider); 
                hmac.init(keyHmac); 
                
                System.out.println("Hachage du message"); 
                hmac.update(message); 
                
                System.out.println("Generation des bytes");   
                byte[] hb = hmac.doFinal(); 
                System.out.println("Termine : HMAC construit"); 
                System.out.println("HMAC = " + new String(hb)); 
                System.out.println("Longueur du HMAC = " + hb.length);
                
                System.out.println("Envoi du message et de son HMAC"); 
                dos.writeUTF(Message); 
                dos.writeInt(hb.length); 
                
                dos.write(hb);
                dos.flush();
                
                String réponse = dis.readUTF();
                System.out.println("Reponse du serveur = " + réponse); 
            } 
            catch (UnknownHostException e) { System.err.println("Erreur ! Host non trouvÃ© [" + e + "]"); }  
            catch (IOException e) { System.err.println("Erreur ! Pas de connexion ? [" + e + "]"); } 
            catch (Exception e) { System.out.println("Aie aie imprÃ©vut ou " + e.getMessage()+ " -- " + e.getClass());}
         }
    }//GEN-LAST:event_jButton_OKActionPerformed

    private void jButton_FermerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_FermerActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton_FermerActionPerformed

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
            java.util.logging.Logger.getLogger(Fen_Paiement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Fen_Paiement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Fen_Paiement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Fen_Paiement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Fen_Paiement dialog = new Fen_Paiement(new javax.swing.JFrame(), true, null, null, null, null, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Fermer;
    private javax.swing.JButton jButton_OK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextField_CB;
    private javax.swing.JTextField jTextField_Montant;
    private javax.swing.JTextField jTextField_NF;
    private javax.swing.JTextField jTextField_réponse;
    // End of variables declaration//GEN-END:variables
}