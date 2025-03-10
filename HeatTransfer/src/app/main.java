/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import IHM.FenetrePrincipale;
import javax.swing.SwingUtilities;

/**** @author Loïc BELLEC & Valentin EBERHARDT  */
public class main {

    /*** @param args the command line arguments   */
    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> {
             FenetrePrincipale fenetre = new FenetrePrincipale();
             // test loic
         });
    } // Fin de la méthode principale 
    
}  // Fin du programme