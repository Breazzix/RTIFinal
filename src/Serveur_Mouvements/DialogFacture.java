/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serveur_Mouvements;

import static Application_Compta.Applic_Data_Analysis.ConfigProperty;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import utilitaire.BaseException;

/**
 *
 * @author vange
 */
public class DialogFacture extends javax.swing.JDialog {

    /**
     * Creates new form DialogFacture
     */
    
    private static DefaultListModel modelFacture = new DefaultListModel();
    private LinkedList<String> listContainers = new LinkedList<String>();
    private Socket cliSock;
    private String sepProperty;
    private String finChaine;
    private String adresse;
    private int port;
    private java.awt.Frame parent;
    
    public DialogFacture(java.awt.Frame parent, boolean modal, Socket socket) {
        super(parent, modal);
        initComponents();
        cliSock = socket;
        sepProperty = ConfigProperty.getConfig().getProperty("separateur");
        finChaine = ConfigProperty.getConfig().getProperty("finChaine"); 
        this.parent=parent;
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
        BEnvoyer = new javax.swing.JButton();
        BFermer = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ListFacture = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        TFTransport = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        TFContainers = new javax.swing.JTextField();
        BAjouter = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("FACTURE");

        BEnvoyer.setText("Envoyer");
        BEnvoyer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BEnvoyerActionPerformed(evt);
            }
        });

        BFermer.setText("Fermer");
        BFermer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFermerActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(ListFacture);

        jLabel2.setText("Id transport :");

        jLabel3.setText("Id Containers :");

        BAjouter.setText("Ajouter");
        BAjouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BAjouterActionPerformed(evt);
            }
        });

        jLabel4.setText("Liste des containers");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(TFTransport, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TFContainers, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(BAjouter)))
                        .addGap(0, 56, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(BEnvoyer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BFermer)
                        .addGap(99, 99, 99))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(jLabel4)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(TFTransport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(TFContainers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BAjouter))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BEnvoyer)
                    .addComponent(BFermer))
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BAjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BAjouterActionPerformed
        modelFacture.addElement(TFContainers.getText());
        listContainers.add(TFContainers.getText());
        ListFacture.setModel(modelFacture);
        TFContainers.setText("");
       
    }//GEN-LAST:event_BAjouterActionPerformed

    private void BEnvoyerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BEnvoyerActionPerformed
        try {
            make_bill();
        } catch (IOException ex) {
            Logger.getLogger(DialogFacture.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BaseException ex) {
            Logger.getLogger(DialogFacture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_BEnvoyerActionPerformed

    private void BFermerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BFermerActionPerformed
       this.dispose();
    }//GEN-LAST:event_BFermerActionPerformed

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
            java.util.logging.Logger.getLogger(DialogFacture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogFacture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogFacture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogFacture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogFacture dialog = new DialogFacture(new javax.swing.JFrame(), true, new Socket());
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
    
    public void make_bill() throws IOException, BaseException 
    {
        String req = "MAKE_BILL" + sepProperty + TFTransport.getText() + sepProperty;
        
        for (int i=0;i<listContainers.size();i++)
        {
            req = req + listContainers.get(i) + sepProperty;
            
        }
        req = req.substring(0, req.length() - 1);
        req = req + finChaine;
        
        System.out.println(req);

        adresse = ConfigProperty.getConfig().getProperty("adresse");
        port = Integer.parseInt(ConfigProperty.getConfig().getProperty("portMouvements"));
        DataInputStream dis= new DataInputStream(cliSock.getInputStream());
        DataOutputStream dos= new DataOutputStream(cliSock.getOutputStream());

        dos.writeUTF(req);


        String reponse = dis.readUTF(); 
        System.out.println("Réponse du serveur = " + reponse); 

        if (reponse.equals("oui"))
        { 
            this.dispose();
        }
        else
        {
            throw new BaseException(this.parent, "donnees encodees incorrectes !");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BAjouter;
    private javax.swing.JButton BEnvoyer;
    private javax.swing.JButton BFermer;
    private javax.swing.JList ListFacture;
    private javax.swing.JTextField TFContainers;
    private javax.swing.JTextField TFTransport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
