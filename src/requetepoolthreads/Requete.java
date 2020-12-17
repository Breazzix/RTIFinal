/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requetepoolthreads;

import java.net.*;
import serveurpoolthreads.ConsoleServeur;

/**
 *
 * @author vange
 */
public interface Requete {
    // Ce qui va être exécuté doit connaître la socket du client distant
// ainsi que le GUI qui affiche les traces
    public Runnable createRunnable (Socket s, ConsoleServeur cs);
}
