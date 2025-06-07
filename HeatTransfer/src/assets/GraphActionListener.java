package assets;

import IHM.PanneauGraphique;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Redéfinir la méthode actionListener  pour la sélection des épaisseurs en lui passant un paramètre
 * @author Loïc BELLEC & Valentin EBERHARDT
 */
public class GraphActionListener implements ActionListener {
    private final int epaisseur;
    private final PanneauGraphique panneau;
    public boolean estAffiche = false;

    /**
     * Constructeur à un paramètre du listener
     * @param p Panneau graphique : panneau sur lequel l'élément auquel on ajoute un listener se situe
     * @param e int : bouton épaisseur correspondant au listener
     */
    public GraphActionListener(PanneauGraphique p, int e) {
        this.epaisseur = e;
        this.panneau = p;
    }  // fin du constructeur GraphActionListener
    
    /**
     * Redéfinir la méthode actionPerformed : mise à jour des attributs du panneau parent
     * @param e int : épaisseur dont les attributs sont à modifier
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.estAffiche = !estAffiche;  // effacer ou afficher le graphique correspond à l'épaisseur e
        this.panneau.setEpaisseur(this.epaisseur, this.estAffiche);  // mise à jour des attributs dans la classe PanneauGraphique
    }  // fin actionPerformed  
} // fin de la classe GraphActionListener