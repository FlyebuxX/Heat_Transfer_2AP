package app;

import IHM.PAccueil;
import IHM.PExplain;
import IHM.PModeleChaleur;
import IHM.PNotice;
import IHM.PanneauScientifique;
import javax.swing.*;
import java.awt.*;

/**
 * Applicaiton VALO - lancement du script
 * @author Loïc BELLEC & Valentin EBERHARDT
 */
public class FPrincipale extends JFrame {
    
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    /**
     * Constructeur de l'application
     */
    public FPrincipale() {
        setTitle("VALO - Modélisation des transferts de chaleur dans le bois sec");
        setSize(1000, 700);
        setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false); // Par défaut la fenêtre n’est pas redimensionnable

        // CardLayout et panneau contenant les vues
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Création des panneaux (vues)
        PanneauScientifique panneauScientifique = new PanneauScientifique("Balsa", 2);  // défaut
        PAccueil pAccueil = new PAccueil(cardLayout, cardPanel);
        PNotice pNotice = new PNotice(cardLayout, cardPanel);
        PExplain pExplain = new PExplain(cardLayout, cardPanel);
        PModeleChaleur pModeleChaleur = new PModeleChaleur(cardLayout, cardPanel);

        // Ajout des panneaux dans le cardPanel avec un nom pour chacun
        cardPanel.add(panneauScientifique, "Graphique");
        cardPanel.add(pAccueil, "Accueil");
        cardPanel.add(pNotice, "Notice");
        cardPanel.add(pExplain, "Explain");
        cardPanel.add(pModeleChaleur, "ChaleurBois");

        // Afficher la page d'accueil au lancement
        cardLayout.show(cardPanel, "Accueil");

        // Ajout du cardPanel à la fenêtre principale
        this.add(cardPanel);

        setVisible(true);
    }  // fin du constructeur FPrincipale
    
    /**
     * Affiche une vue spécifique et ajuste la possibilité de redimensionner la fenêtre.
     * @param nomVue Nom de la vue à afficher
     */
    public void afficherVue(String nomVue) {
        cardLayout.show(cardPanel, nomVue);
        // Redimensionnable uniquement pour la vue graphique
        setResizable("GraphiqueTemp".equals(nomVue));
    }  // fin afficherVue

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FPrincipale::new);
    }  // fin du main
}  // fin de la classe FPrincipale
