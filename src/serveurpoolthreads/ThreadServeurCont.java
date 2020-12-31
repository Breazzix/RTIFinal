/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveurpoolthreads;

import ProtocoleSUM.RequeteSUM;
import ProtocoleSUM.RequeteSUM_Cont;
import java.net.*;
import java.io.*;
import requetepoolthreads.Requete;
import serveurpoolthreads.ConsoleServeur;
import static Serveur_Compta.FenServeurCompta.ConfigProperty;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nicolas
 */
public class ThreadServeurCont extends Thread{
    private int port;
    private SourceTaches tachesAExecuter;
    private ConsoleServeur guiApplication;
    private ServerSocket SSocket = null;
    String type;
    
    public ThreadServeurCont(int p,String Type, SourceTaches st, ConsoleServeur fs)
    {
         port = p; tachesAExecuter = st; guiApplication = fs; type=Type;
    }
   
    public void run()
    {
        try
        {
            SSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
             System.err.println("Erreur de port d'écoute ! ? [" + e + "]"); System.exit(1);
        }
        // Démarrage du pool de threads
        for (int i=0; i<Integer.parseInt(ConfigProperty.getConfig().getProperty("clients")); i++) // 3 devrait être constante ou une propriété du fichier de config
        {
            ThreadClient thr = new ThreadClient (tachesAExecuter, "Thread du pool du port" + type + " n°" +
            String.valueOf(i));
            thr.start();
        }
        // Mise en attente du serveur
        Socket CSocket = null;
        while (!isInterrupted())
        {
            try
            {
                System.out.println("************ Serveur en attente *************");
                CSocket = SSocket.accept();
                guiApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+
                "#accept#thread serveur");
            }
            catch (IOException e)
            {
                System.err.println("Erreur d'accept ! ? [" + e.getMessage() + "]"); System.exit(1);
            }
            
            
            RequeteSUM_Cont req = null;
            try {
                req = new RequeteSUM_Cont(CSocket);
                
                Runnable travail = req.createRunnable(CSocket, guiApplication);
            
                if (travail != null)
                {
                    tachesAExecuter.recordTache(travail);
                    System.out.println("Travail mis dans la file");
                }
                
            else System.out.println("Pas de mise en file");
            } catch (IOException ex) {
                Logger.getLogger(ThreadServeur.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ThreadServeur.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CertificateException ex) {
                Logger.getLogger(ThreadServeur.class.getName()).log(Level.SEVERE, null, ex);
            } catch (KeyStoreException ex) {
                Logger.getLogger(ThreadServeur.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnrecoverableKeyException ex) {
                Logger.getLogger(ThreadServeur.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchProviderException ex) {
                Logger.getLogger(ThreadServeur.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void fermer () throws IOException
    {
        SSocket.close();
    }
}
