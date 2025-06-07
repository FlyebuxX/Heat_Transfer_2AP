package IHM;

import assets.ImagePanel;
import javax.swing.*;
import java.awt.*;

/**
 * Classe défininissant la page d'explication de l'application
 * @author Loïc BELLEC & Valentin EBERHARDT
 */
public class PExplain extends JPanel {

    /**
     * Constructeur du panneau d'explication de l'application
     * @param cardLayout CardLayout : paquet de panneaux à afficher
     * @param cardPanel  JPanel : panneau affiché sur l'écran de l'utilisateur
     */
    public PExplain(CardLayout cardLayout, JPanel cardPanel) {

        setBackground(Color.WHITE);  // définir un fond blanc
        setLayout(new BorderLayout());  // Panel principal

        /* Panneau principal */
        JPanel panPrincipal = new JPanel(new BorderLayout());
        panPrincipal.setBackground(Color.WHITE);

        // Titre
        JPanel panTitre = new JPanel();
        panTitre.setBackground(new Color(Color.HSBtoRGB(0.6f, 1f, 0.3f)));
        JLabel labTitre = new JLabel("Explication du problème");
        labTitre.setForeground(Color.WHITE);
        labTitre.setFont(new Font("Arial", Font.BOLD, 30));
        panTitre.add(labTitre);
        panPrincipal.add(panTitre, BorderLayout.NORTH);

        /* Contenu au centre*/
        JPanel panCentre = new JPanel();
        panCentre.setLayout(new BoxLayout(panCentre, BoxLayout.Y_AXIS));
        panCentre.setBackground(Color.WHITE);


        JLabel textLabel = new JLabel("<html>"
                + "<div style='font-family:SansSerif;font-size:13px;max-width:950px;'>"
                + "<h2 style='color:#001f4d;'>Objectif de la modélisation</h2>"
                + "Le problème à résoudre consiste à modéliser les transferts de chaleur dans différentes essences de bois sec,<br>"
                + "soumises à un faible flux thermique de 4 kW/m². La température reste inférieure à 200 °C, ce qui évite toute <br>"
                + "pyrolyse, combustion ou réaction chimique. Le transfert de chaleur est donc le seul phénomène considéré.<br><br>"
                + "<h2 style='color:#001f4d;'>Méthodologie numérique</h2>"
                + "L’objectif est de reproduire numériquement l’évolution de la température dans le bois au cours du temps,<br>"
                + "en se basant sur les propriétés thermiques du matériau :<br>"
                + "<ul>"
                + "<li>Conductivité thermique</li>"
                + "<li>Capacité thermique massique</li>"
                + "<li>Masse volumique</li>"
                + "</ul>"
                + "Le modèle est unidimensionnel, basé sur l’équation de la chaleur, et résolu par volumes finis avec un schéma <br>"
                +" explicite.<br><br>"
                + "<h2 style='color:#001f4d;'>Conditions aux limites</h2>"
                + "Les échanges de chaleur se font :<br>"
                + "- Par conduction dans le bois<br>"
                + "- Par convection et rayonnement à la surface<br>"
                + "- Par apport de chaleur dû au cône chauffant en face avant<br><br>"
                + "<h2 style='color:#001f4d;'>Validation expérimentale</h2>"
                + "Les données expérimentales fournissent les températures mesurées pendant 7200 secondes pour cinq essences :<br>"
                + "Balsa, Peuplier, Épicéa, Mélèze et Chêne. Ces mesures permettent de comparer les résultats simulés et <br>"
                +"valider le modèle."
                + "</div></html>", SwingConstants.CENTER);

        textLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        textLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // JScrollPane pour défiler le texte dans le panneau
        JScrollPane scrollPane = new JScrollPane(textLabel);
        scrollPane.setPreferredSize(new Dimension(0, 500));
        panCentre.add(scrollPane);

        panPrincipal.add(panCentre, BorderLayout.CENTER);

        /* Contenu au bas du panneau */
        JPanel panBas = new JPanel();
        panBas.setLayout(new BoxLayout(panBas, BoxLayout.Y_AXIS));
        panBas.setBackground(Color.WHITE);

        // Logo
        ImagePanel logoValo = new ImagePanel("img/Logo_Valo.png", 100, 100);
        logoValo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panBas.add(logoValo);

        // Noms
        JLabel labelNoms = new JLabel("© Loïc.B et Valentin.E", SwingConstants.CENTER);
        labelNoms.setFont(new Font("Arial", Font.BOLD, 16));
        labelNoms.setAlignmentX(Component.CENTER_ALIGNMENT);
        panBas.add(labelNoms);

        // Bouton de navigation
        JButton btnRetour = new JButton("← Retour à l'accueil");
        btnRetour.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRetour.addActionListener(e -> cardLayout.show(cardPanel, "Accueil"));
        panBas.add(Box.createRigidArea(new Dimension(0, 10)));
        panBas.add(btnRetour);

        panPrincipal.add(panBas, BorderLayout.SOUTH);

        this.add(panPrincipal, BorderLayout.CENTER);
    }  // fin du constructeur PExplain
}  // fin de la classe PExplain