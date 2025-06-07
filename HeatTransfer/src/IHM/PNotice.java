package IHM;

import assets.ImagePanel;
import javax.swing.*;
import java.awt.*;

/**
 * Classe définissant la notice d'utilisation de l'application
 * @@author Loïc BELLEC & Valentin EBERHARDT
 */
public class PNotice extends JPanel {

    /**
     * Constructeur du panneau comportant la notice d'utilisation
     * @param cardLayout CardLayout : paquet de panneaux à afficher
     * @param cardPanel  JPanel : panneau affiché sur l'écran de l'utilisateur
     */
    public PNotice(CardLayout cardLayout, JPanel cardPanel) {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        /* Panneau principal */
        JPanel panPrincipal = new JPanel(new BorderLayout());
        panPrincipal.setBackground(Color.WHITE);

        // Titre
        JPanel panTitre = new JPanel();
        panTitre.setBackground(new Color(Color.HSBtoRGB(0.6f, 1f, 0.3f)));
        JLabel labTitre = new JLabel("Notice d'utilisation");
        labTitre.setForeground(Color.WHITE);
        labTitre.setFont(new Font("Arial", Font.BOLD, 30));
        panTitre.add(labTitre);
        panPrincipal.add(panTitre, BorderLayout.NORTH);

        /* Panneau comportant le contenu de la notice */
        JPanel panCentre = new JPanel();
        panCentre.setLayout(new BoxLayout(panCentre, BoxLayout.Y_AXIS));
        panCentre.setBackground(Color.WHITE);

        JLabel textLabel = new JLabel("<html>"
        + "<div style='font-family:SansSerif;font-size:13px;max-width:950px;'>"

        + "<h2 style='color:#001f4d;'>Introduction</h2>"
        + "Ce programme permet de simuler et de modéliser les transferts de chaleur dans le bois sec.<br>"
        + "Il a été conçu pour faciliter l’analyse de la conductivité thermique de différentes essences de bois, en fonction <br> "
        + "de l'épaisseur étudiée.<br><br>"

        + "<h2 style='color:#001f4d;'>Sélection de l’essence de bois :</h2>"
        + "<ul>"
        + "<li><b style='color:#f0dcc5;'>Balsa</b> : Bois très léger, excellent isolant thermique, utilisé en modélisme ou <br>"
        + "pour les structures isolantes dans les drones et avions miniatures.</li><br>"
        + "<li><b style='color:#b4ad94;'>Mélèze</b> : Bois résineux naturellement résistant à l'humidité, utilisé pour les <br>"
        + "bardages extérieurs en zones froides, avec une conductivité thermique modérée.</li><br>"
        + "<li><b style='color:#c1b07e;'>Épicéa</b> : Bois tendre souvent utilisé dans la construction de chalets et d’instruments <br>"
        +" de musique (tables d’harmonie), caractérisé par une faible inertie thermique.</li><br>"
        + "<li><b style='color:#a39370;'>Chêne</b> : Bois dense à forte inertie thermique, utilisé pour les parquets chauffants <br>"
        +" ou la fabrication de meubles massifs exposés à des variations de température.</li><br>"
        + "<li><b style='color:#f5e4ac;'>Peuplier</b> : Bois clair et léger, souvent employé pour les contreplaqués ou les <br>"
        +" palettes, avec une conductivité thermique faible, utile dans les structures isolées à faible charge.</li>"
        + "</ul><br>"

        + "<h2 style='color:#001f4d;'>Epaisseur analysée :</h2>"
        + "L’utilisateur peut définir l'épaisseur d’analyse en millimètres (entre 2 et 40 mm), soit en saisissant une valeur, <br>"
        + "soit en utilisant la barre de progression située sous les images des essences de bois.<br><br>"
        + "<b>Couleur de la barre :</b><br>"
        + "-<b style='color:#ffcc00;'> Jaune </b> = faible épaisseur <br>"
        + "-<b style='color:#ff0000;'> Rouge </b> = épaisseur maximale (proche de 40 mm)<br><br>"
        + "L'épaisseur sélectionnée servira au calcul des transferts de chaleur à partir de la surface du bois, accessible <br>"
        +" ensuite via le bouton <i>« Outil comparatif »</i>.<br><br>"

        + "<h2 style='color:#001f4d;'>Affichage des propriétés physiques du bois sélectionné :</h2>"
        + "<ul>"
        + "<li>Masse volumique (en kg/m³)</li>"
        + "<li>Conductivité thermique (en W/m·K)</li>"
        + "<li>Émissivité : coefficient sans unité compris entre 0 et 1 exprimant la capacité du bois à émettre de la chaleur</li>"
        + "</ul><br>"

        + "<h2 style='color:#001f4d;'>Page « Outil comparatif » :</h2>"
        + "Une fois vos paramètres définis, cette page vous permet d’afficher une courbe correspondant aux valeurs <br>"
        + " expérimentales du bois et de l'épaisseur sélectionnés.<br>"
        + "Vous pouvez aussi :<br>"
        + "- Sélectionner d’autres épaisseurs via la colonne de boutons (entre 2 et 40 mm)<br>"
        + "- Choisir d’afficher les courbes expérimentales, théoriques, ou les deux<br>"
        + "- Enregistrer le graphique affiché au format PNG grâce au dernier bouton<br>"

        + "</div></html>", SwingConstants.CENTER);

        textLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        // Ajouter une marge intérieure pour le JLabel
        textLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Marge de 20px autour du texte

        // Ajouter un JScrollPane pour permettre le défilement
        JScrollPane scrollPane = new JScrollPane(textLabel);
        scrollPane.setPreferredSize(new Dimension(0, 500)); // Taille de la zone de défilement

        // Ajouter le JScrollPane dans le panneau central
        panCentre.add(scrollPane);
        panPrincipal.add(panCentre, BorderLayout.CENTER);

        /* Panneau de commandes en bas */
        JPanel panneauBas = new JPanel();
        panneauBas.setLayout(new BoxLayout(panneauBas, BoxLayout.Y_AXIS));
        panneauBas.setBackground(Color.WHITE);

        // Logo
        ImagePanel logoValo = new ImagePanel("img/Logo_Valo.png", 100, 100);
        logoValo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauBas.add(logoValo);

        // Noms des développeurs
        JLabel namesLabel = new JLabel("© Loïc.B et Valentin.E", SwingConstants.CENTER);
        namesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        namesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauBas.add(namesLabel);

        // Bouton de retour
        JButton btn = new JButton("← Retour à la modélisation");
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> cardLayout.show(cardPanel, "ChaleurBois"));
        panneauBas.add(Box.createRigidArea(new Dimension(0, 10)));
        panneauBas.add(btn);

        panPrincipal.add(panneauBas, BorderLayout.SOUTH);

        
        this.add(panPrincipal, BorderLayout.CENTER);  // ajouter panPrincipal à l'objet PNotice
    }  // fin du constructeur PNotice
}  // fin de la classe PNotice
