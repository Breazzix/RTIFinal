/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application_Compta;

import Classes.Facture;
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import utilitaire.BaseException;
import utilitaire.FichierConfig;

/**
 *
 * @author vange
 */
public class FenListeFactures extends javax.swing.JDialog {
    
    private Object[] col = { "Id", "Societe", "Date", "Montant Total", "MontantAPayer", "Moyen d'envoi" };
    private DefaultTableModel modelFactures = new DefaultTableModel(col, 0);
    
    public static Socket cliSock;
    public static FichierConfig ConfigProperty = new FichierConfig();
    private String sepProperty;
    private String finChaine;
    private static String codeProvider = "BC"; //CryptixCrypto";
    private static PrivateKey cleClient;
    
    public static LinkedList<Facture> listeFacturesAPasEnvoyer = new LinkedList<Facture>();
    public static LinkedList<Facture> listeFacturesAEnvoyer = new LinkedList<Facture>();
    
    private java.awt.Frame parent;
    /**
     * Creates new form FenListeFactures
     */
    public FenListeFactures(java.awt.Frame parent, boolean modal,Socket sock, PrivateKey keyClient) throws IOException, ClassNotFoundException {
        super(parent, modal);
        initComponents();
        
        cliSock = sock;
        sepProperty = ConfigProperty.getConfig().getProperty("separateur");
        finChaine = ConfigProperty.getConfig().getProperty("finChaine");
        cleClient=keyClient;
        
        jTableFactures.setModel(modelFactures);
        
        for(int i = modelFactures.getRowCount(); i > 1; --i)
        {
            modelFactures.removeRow(i-1);
        }
        
        recupFactures();
        
        FileInputStream fis = new FileInputStream("data.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        DataInputStream dis = new DataInputStream( new BufferedInputStream(cliSock.getInputStream()));
        
        int taille = dis.readInt();
        
        Facture fact;
         Object[] data ;

         for (int i=0;i<taille;i++)
         {
             fact = (Facture) ois.readObject();
             data = new Object[]{ fact.getIdFacture(), fact.getSociete(),fact.getDate(), fact.getMontantTotal(),fact.getMontantAPayer(),fact.getMoyenEnvoi() };
             modelFactures.insertRow(i, data);
             listeFacturesAEnvoyer.add(fact);
         }
         
        jTableFactures.setModel(modelFactures);
        
        ois.close();
        fis.close();
    }
    
    private void recupFactures()
    {
        String message = "Liste_Facture" + finChaine;
        
        Envoyer(message);
    }
    
    public String Envoyer(String message)
    {
        String reponse = null;
        try 
        {
            DataOutputStream dos= new DataOutputStream(cliSock.getOutputStream());

            dos.writeUTF(message);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Applic_Compta.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return reponse;
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
        Bdelete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableFactures = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Liste factures à valider");

        BEnvoyer.setText("Envoyer");
        BEnvoyer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BEnvoyerActionPerformed(evt);
            }
        });

        Bdelete.setText("Delete");
        Bdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BdeleteActionPerformed(evt);
            }
        });

        jTableFactures.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTableFactures.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Societe", "Date", "Montant Total", "Montant Restant", "Moyen d'envoi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTableFactures.setToolTipText("");
        jScrollPane1.setViewportView(jTableFactures);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 20, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 675, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(212, 212, 212)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(BEnvoyer)
                        .addGap(134, 134, 134)
                        .addComponent(Bdelete)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BEnvoyer)
                    .addComponent(Bdelete))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BEnvoyerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BEnvoyerActionPerformed
        
        try {
            DataInputStream dis= new DataInputStream(cliSock.getInputStream());
            DataOutputStream dos= new DataOutputStream(cliSock.getOutputStream());
            
            String message = "SEND_BILLS" + finChaine;

            Envoyer(message);
            
            String donnees = "no";
            
            if (listeFacturesAPasEnvoyer.size()>0)
            {
                for (int i=0;i<listeFacturesAPasEnvoyer.size();i++)
                {
                    donnees = donnees + listeFacturesAPasEnvoyer.get(i).getIdFacture() + sepProperty;
                }
                donnees = donnees.substring(0, donnees.length() - 1);
            }
            

            byte[] Message = donnees.getBytes();
            
            System.out.println("Instanciation de la signature");
            Signature s = Signature. getInstance("SHA1withRSA",codeProvider);
            System.out.println("Initialisation de la signature");
            s.initSign(cleClient);
            System.out.println("Hachage du message");
            s.update(Message);
            System.out.println("Generation des bytes");
            byte[] signature = s.sign();
            System.out.println("Termine : signature construite");
            System.out.println("Signature = " + new String(signature));
            System.out.println("Longueur de la signature = " + signature.length);
            System.out.println("Envoi du message et de la signature");
            dos.writeUTF(donnees);
            dos.writeInt(signature.length);
            dos.write(signature);
            
            String reponse = dis.readUTF(); 
            if (reponse.equals("Signature testee avec succes"))
            {
                this.dispose();
            }
            else
            {
                throw new BaseException(this.parent, "signature non vérifiée");
            }
             
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FenListeFactures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(FenListeFactures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FenListeFactures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(FenListeFactures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            Logger.getLogger(FenListeFactures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FenListeFactures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BaseException ex) {
            Logger.getLogger(FenListeFactures.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_BEnvoyerActionPerformed

    private void BdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BdeleteActionPerformed
        if (jTableFactures.getSelectedRow() != -1) {
            // remove selected row from the model
            listeFacturesAPasEnvoyer.add(listeFacturesAEnvoyer.get(jTableFactures.getSelectedRow()));
            listeFacturesAEnvoyer.remove(jTableFactures.getSelectedRow());
            modelFactures.removeRow(jTableFactures.getSelectedRow());

            jTableFactures.setModel(modelFactures);
        }
    }//GEN-LAST:event_BdeleteActionPerformed

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
            java.util.logging.Logger.getLogger(FenListeFactures.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FenListeFactures.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FenListeFactures.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FenListeFactures.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FenListeFactures dialog;
                try {
                    dialog = new FenListeFactures(new javax.swing.JFrame(), true,new Socket(),cleClient);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(FenListeFactures.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(FenListeFactures.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BEnvoyer;
    private javax.swing.JButton Bdelete;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableFactures;
    // End of variables declaration//GEN-END:variables
}
