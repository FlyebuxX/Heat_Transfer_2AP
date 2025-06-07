package assets;

import IHM.PanneauGraphique;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Redéfinir la méthode actionListener pour le bouton qui permet l'affichage des valeurs expérimentales
 * @author Loïc BELLEC & Valentin EBERHARDT
 */

public class ListenerExp implements ActionListener {
    
    private boolean afficherValeursExperimentales = false;  // par défaut, les valeurs expérimentales sont masquées
    private final PanneauGraphique panneau;

    /**
     * Constructeur à un paramètre du listener expérimental
     * @param p PanneauGraphique : panneau sur lequel s'applique le listener expérimental
     */
    public ListenerExp(PanneauGraphique p) {
        this.panneau = p;
    }  // fin du constructeur ListenerExp

    /**
     * Redéfinir la méthode actionPerformed : mise à jour des attributs du listener et du panneau parent
     * @param e int : épaisseur dont les attributs sont à modifier
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.afficherValeursExperimentales = !this.afficherValeursExperimentales;  // effacer ou afficher les valeurs expérimentales
        this.panneau.setValeursExperimentales(this.afficherValeursExperimentales);  // mise à jour des attributs dans le panneau parent
    }  // fin actionPerformed  
}  // fin de la classe ListenerExp