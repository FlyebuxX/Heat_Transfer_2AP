/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IHM;



import javax.swing.JPanel;
import java.awt.*;

/**** @author Loïc Bellec & Valentin EBERHARDT */
public class PanneauPrincipal extends JPanel {

    private textPanel welcomePanel;
    private String welcomeMessage = "Bienvenue ! Cet outil vous aidera à évaluer la diffusion d'un flux de chaleur à " +
            "travers du bois sec, aussi bien pour des feuillus que pour des résineux !";

    private commandPanel welcomeCommand;
    
     /****************************************************
     *  CONSTRUCTEUR DU PANNEAU PRINCIPAL
     ****************************************************/
    
    public PanneauPrincipal() {
        super();
        this.setLayout(new BorderLayout());

        this.welcomePanel = new textPanel(1000, 600, welcomeMessage);
        this.welcomeCommand = new commandPanel("Quitter", "Aller à l'application");

        this.add(this.welcomePanel, BorderLayout.NORTH);
        this.add(welcomeCommand, BorderLayout.SOUTH);

    } // Fin du constructeur sans paramètre  

    
} // Fin de la classe PanneauPrincipal
