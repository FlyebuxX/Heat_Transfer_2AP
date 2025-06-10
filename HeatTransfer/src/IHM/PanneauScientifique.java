package IHM;

import app.FPrincipale;
import assets.ImagePanel;
import assets.GraphActionListener;
import assets.ListenerTh;
import assets.ListenerExp;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Classe définissant le panneau scientifique contenant les graphiques
 * @author Loïc BELLEC & Valentin EBERHARDT
 */

public class PanneauScientifique extends JPanel {

    private final String essence;
    private final JPanel panneauDroite;
    public final JPanel panneauEpaisseurs;
    public final JPanel panneauValeurs;
    
    public PanneauGraphique DessinGraphique;

    public PanneauScientifique(String essence, int valeurChampEpaisseur) {
        this.essence = essence;
        this.setLayout(new BorderLayout());

        /* Section haute */
        JLabel titre = new JLabel("Outil comparatif", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 30));
        titre.setForeground(Color.WHITE);
        titre.setOpaque(true);
        titre.setBackground(new Color(Color.HSBtoRGB(0.6f, 1f, 0.3f)));
        this.add(titre, BorderLayout.NORTH);

        /* Section gauche : images */
        JPanel panneauGauche = new JPanel();
        panneauGauche.setPreferredSize(new Dimension(250, 1000));
        panneauGauche.setLayout(new BoxLayout(panneauGauche, BoxLayout.Y_AXIS));

        ImagePanel logo = new ImagePanel("img/Logo_Valo.png", 200, 200);
        panneauGauche.add(logo);

        ImagePanel imgBois = new ImagePanel("img/" + essence.replace("é", "e").replace("É", "E").replace("è", "e")
                .replace("ê", "e").replace("à", "a").replace("ç", "c").replace("ù", "u")
                .replaceAll("\\s+", "") + ".jpg", 200, 200);
        panneauGauche.add(imgBois);
        this.add(panneauGauche, BorderLayout.WEST);

        /* Section centrale : graphiques */
        this.DessinGraphique = new PanneauGraphique(this.essence, valeurChampEpaisseur);
        this.DessinGraphique.setBorder(new LineBorder(Color.black, 2));
        this.add(this.DessinGraphique, BorderLayout.CENTER);

        /* Section de droite : sélection des épaisseurs */
        this.panneauDroite = new JPanel(new BorderLayout());
        this.panneauDroite.setPreferredSize(new Dimension(200, this.getHeight()));

        this.panneauEpaisseurs = new JPanel(new GridLayout(15, 1, 0, 2));
        this.panneauEpaisseurs.add(new JLabel("Epaisseurs", SwingConstants.CENTER));

        for (int i = 2; i <= 20; i += 2) {
            JCheckBox boxEpaisseur = new JCheckBox(i + "mm");
            boxEpaisseur.setHorizontalAlignment(SwingConstants.CENTER);
            boxEpaisseur.addActionListener(new GraphActionListener(DessinGraphique, i));
            this.panneauEpaisseurs.add(boxEpaisseur);
        }
        for (int i = 30; i <= 40; i += 10) {
            JCheckBox boxEpaisseur = new JCheckBox(i + "mm");
            boxEpaisseur.setHorizontalAlignment(SwingConstants.CENTER);
            boxEpaisseur.addActionListener(new GraphActionListener(DessinGraphique, i));
            this.panneauEpaisseurs.add(boxEpaisseur);
        }

        // Bouton tout sélectionner
        JButton btnToutSelectionner = new JButton("tout sélectionner");
        btnToutSelectionner.setCursor(new Cursor(Cursor.HAND_CURSOR));  // modifier le curseur quand le curseur survole le bouton
        btnToutSelectionner.addActionListener(e -> {
            for (Component box : this.panneauEpaisseurs.getComponents()) {
                if (box instanceof JCheckBox && !((JCheckBox) box).isSelected()) {
                    ((JCheckBox) box).doClick();
                }
            }
        });
        this.panneauEpaisseurs.add(btnToutSelectionner);

        // Bouton tout déselectionner
        JButton btnToutDeselectionner = new JButton("tout déselectionner");
        btnToutDeselectionner.setCursor(new Cursor(Cursor.HAND_CURSOR));  // modifier le curseur quand le curseur survole le bouton
        btnToutDeselectionner.addActionListener(e -> {
            for (Component box : this.panneauEpaisseurs.getComponents()) {
                if (box instanceof JCheckBox && ((JCheckBox) box).isSelected()) {
                    ((JCheckBox) box).doClick();
                }
            }
        });
        this.panneauEpaisseurs.add(btnToutDeselectionner);

        this.panneauDroite.add(this.panneauEpaisseurs, BorderLayout.NORTH);

        // Boutons valeurs expérimentales et théoriques
        this.panneauValeurs = new JPanel(new GridLayout(2, 1, 0, 2));
        this.panneauValeurs.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Bouton valeurs expérimentales
        JCheckBox btnValExp = new JCheckBox("Valeurs Expérimentales");
        btnValExp.setCursor(new Cursor(Cursor.HAND_CURSOR));  // modifier le curseur quand le curseur survole le bouton
        btnValExp.setHorizontalAlignment(SwingConstants.CENTER);
        btnValExp.addActionListener(new ListenerExp(DessinGraphique));
        this.panneauValeurs.add(btnValExp);

        // Bouton valeurs théoriques
        JCheckBox btnValTh = new JCheckBox("Valeurs Théoriques");
        btnValTh.setCursor(new Cursor(Cursor.HAND_CURSOR));  // modifier le curseur quand le curseur survole le bouton
        btnValTh.setHorizontalAlignment(SwingConstants.CENTER);
        btnValTh.addActionListener(new ListenerTh(DessinGraphique));
        this.panneauValeurs.add(btnValTh);

        this.panneauDroite.add(this.panneauValeurs, BorderLayout.CENTER);

        /* Panneau boutons Retour et Exporter */
        JPanel panneauBtn = new JPanel();
        panneauBtn.setLayout(new BoxLayout(panneauBtn, BoxLayout.Y_AXIS));
        
        // Bouton retour menu de configuration
        JButton retourButton = new JButton("← Retour");
        retourButton.setCursor(new Cursor(Cursor.HAND_CURSOR));  // modifier le curseur quand le curseur survole le bouton
        retourButton.setPreferredSize(new Dimension(100, 30));
        retourButton.addActionListener(e -> {
            FPrincipale fenetre = (FPrincipale) SwingUtilities.getWindowAncestor(this);
            fenetre.afficherVue("ChaleurBois");  // interdire le redimensionnement de la fenêtre
            fenetre.setSize(1000, 700);  // rétablir la taille originale de la fenêtre
            fenetre.setLocationRelativeTo(null);
        });
        
        JPanel retourPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        retourPanel.add(retourButton);
        panneauBtn.add(retourPanel);
        panneauBtn.add(Box.createVerticalStrut(5));

        // Bouton exporter
        JButton btnExporter = new JButton("Exporter");
        btnExporter.setCursor(new Cursor(Cursor.HAND_CURSOR));  // modifier le curseur quand le curseur survole le bouton
        btnExporter.setPreferredSize(new Dimension(100, 30));
        btnExporter.addActionListener((ActionEvent actionEvent) -> {
            try {
                BufferedImage image = new BufferedImage(DessinGraphique.getWidth(), DessinGraphique.getHeight(), BufferedImage.TYPE_INT_RGB);
                File cheminImage = new File("img/Graphique_" + this.essence + ".png");
                Graphics g = image.createGraphics();
                DessinGraphique.paint(g);
                g.dispose();
                ImageIO.write(image, "png", cheminImage);

                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        desktop.open(cheminImage);
                    } else {
                        System.out.println("L'action OPEN n'est pas supportée sur cette plateforme.");
                    }
                } else {
                    System.out.println("Desktop n'est pas supporté sur cette plateforme.");
                }

            } catch (IOException ex) {
                Logger.getLogger(PanneauScientifique.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        savePanel.add(btnExporter);
        panneauBtn.add(savePanel);

        this.panneauDroite.add(panneauBtn, BorderLayout.SOUTH);
        this.add(panneauDroite, BorderLayout.EAST);
    }  // fin du constructeur PanneauScientifique
}  // fin de la classe PanneauScientifique
