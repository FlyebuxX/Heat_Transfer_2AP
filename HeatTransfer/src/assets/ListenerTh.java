package assets;

import IHM.PanneauGraphique;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Redéfinir la méthode actionListener pour le bouton qui permet l'affichage des valeurs théoriques
 * @author Loïc BELLEC & Valentin EBERHARDT
 */

public class ListenerTh implements ActionListener {
    
    public boolean afficherValeursTheoriques = false;  // default
    private final PanneauGraphique panneau;

    /**
     * Constructeur à un paramètre du listener théorique
     * @param p PanneauGraphique : panneau sur lequel s'applique le listener théorique
     */
    public ListenerTh(PanneauGraphique p) {
        this.panneau = p;
    }  // fin du constructeur ListenerTh
        
    /**
     * Redéfinir la méthode actionPerformed : mise à jour des attributs du panneau parent
     * @param e int : épaisseur dont les attributs sont à modifier
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.afficherValeursTheoriques = !this.afficherValeursTheoriques;  // effacer ou afficher les valeurs théoriques
        this.panneau.setValeursTheoriques(this.afficherValeursTheoriques);  // mise à jour des attributs dans le panneau parent
    }  // fin actionPerformed
}  // fin de la classe ListenerTh