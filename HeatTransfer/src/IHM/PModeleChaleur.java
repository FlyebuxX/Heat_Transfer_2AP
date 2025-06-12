package IHM;

import app.FPrincipale;
import assets.ImagePanel;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Menu de sélection des essences et des épaisseurs
 * @author Loïc BELLEC & Valentin EBERHARDT
 */
public class PModeleChaleur extends JPanel {

    private final Image backgroundImage;
    private ImagePanel imageBois;
    private JLabel citationLabel;
    private String essenceChoisie = "Balsa";  // défaut

    /**
     * Constructeur du menu de configuration
     * @param cardLayout CardLayout : paquet de panneaux à afficher
     * @param cardPanel  JPanel : panneau affiché sur l'écran de l'utilisateur
     */
    public PModeleChaleur(CardLayout cardLayout, JPanel cardPanel){

        /* Changer l'image de fond en fonction de l'OS utilisé pour éviter des décalages */
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            backgroundImage = new ImageIcon("img/PModeleChaleurMAC.png").getImage();
        }
        else {
            if (os.contains("win")) {
                backgroundImage = new ImageIcon("img/PModeleChaleurPC.png").getImage();
            }
            else {  // défaut si l'OS n'est pas reconnu
                backgroundImage = new ImageIcon("img/PModeleChaleurPC.png").getImage();
            }
        }

        setLayout(new BorderLayout());

        // Titre du projet en haut
        JLabel titre = new JLabel("Modélisation des transferts de chaleur dans le bois sec", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 30));
        titre.setForeground(Color.WHITE);
        titre.setOpaque(true);
        titre.setBackground(new Color(Color.HSBtoRGB(0.6f, 1f, 0.3f)));
        add(titre, BorderLayout.NORTH);

        /* Panneau central de configuration */
        JPanel panelCentre = new JPanel(new GridLayout(1, 2));
        panelCentre.setOpaque(false);

        /* Panneau gauche : sélection essences et épaisseurs */
        JPanel gauche = new JPanel();
        gauche.setLayout(new BoxLayout(gauche, BoxLayout.Y_AXIS));
        gauche.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        gauche.setOpaque(false);

        /* Panneau droite : slider */
        JPanel droite = new JPanel();
        droite.setLayout(new BoxLayout(droite, BoxLayout.Y_AXIS));
        droite.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        droite.setOpaque(false);

        gauche.add(new JLabel("Sélectionner l’essence de bois :"));

        String[] essences = {"Balsa", "Chêne", "Épicéa", "Mélèze", "Peuplier"};
        ButtonGroup boisGroup = new ButtonGroup();

        this.imageBois = new ImagePanel("img/Balsa.jpg", 240, 240);
        this.imageBois.setAlignmentX(Component.CENTER_ALIGNMENT);
        droite.add(this.imageBois);

        for (String essence : essences) {
            JRadioButton boutonEssence = new JRadioButton(essence);
            boutonEssence.setCursor(new Cursor(Cursor.HAND_CURSOR));  // modifier le curseur quand le curseur survole le bouton
            boisGroup.add(boutonEssence);
            boutonEssence.setOpaque(false);
            gauche.add(boutonEssence);

            if (essence.equals("Balsa")) boutonEssence.setSelected(true);  // défaut

            boutonEssence.addActionListener(e -> {
                essenceChoisie = essence.replace("é", "e").replace("É", "E").replace("è", "e")
                        .replace("ê", "e").replace("à", "a").replace("ç", "c").replace("ù", "u")
                        .replaceAll("\\s+", "");  // traitement du nom
                String nomFichier = "img/" + essenceChoisie + ".jpg";

                ImagePanel nouvelleImage = new ImagePanel(nomFichier, 240, 240);
                nouvelleImage.setAlignmentX(Component.CENTER_ALIGNMENT);
                droite.remove(imageBois);
                imageBois = nouvelleImage;
                droite.add(imageBois, 0);
                droite.revalidate();
                droite.repaint();
            });
        }
        
        /* Champ de saisie manuelle de l'épaisseur */

        gauche.add(Box.createRigidArea(new Dimension(0, 50)));
        JLabel instructionSaisie = new JLabel("Saisir l'épaisseur E (2mm ≤ E ≤ 40mm) ou faire glisser le curseur :");
        gauche.add(instructionSaisie);

        JTextField champEpaisseur = new JTextField("20");  // défaut
        
        // listener clavier au champ de saisie de l'épaisseur
        champEpaisseur.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "validerAction");
        
        // listener souris au champ de saisie de l'épaisseur
        champEpaisseur.addActionListener((ActionEvent e) -> {
            
            boolean estNombre = true;
            int valeurChampEpaisseur;
            
            try {
                valeurChampEpaisseur = Integer.parseInt(champEpaisseur.getText());
            } catch (NumberFormatException error) {
                estNombre = false;
            }
            
            if (estNombre || champEpaisseur.getText().equals("")) {
                valeurChampEpaisseur = Integer.parseInt(champEpaisseur.getText());
            
                if (2 <= valeurChampEpaisseur && valeurChampEpaisseur <= 40) {  // si la saisie est valide
                    PanneauScientifique panScientifique = new PanneauScientifique(essenceChoisie, valeurChampEpaisseur);
                    panScientifique.DessinGraphique.setEpaisseur(valeurChampEpaisseur, true);

                    // séletion du bouton de l'épaisseur saisie si l'épaisseur est classique (de 0 à 20mm + 30mm + 40mm)
                    if (((2 <= valeurChampEpaisseur && valeurChampEpaisseur <= 20) && valeurChampEpaisseur % 2 == 0) || valeurChampEpaisseur == 30 || valeurChampEpaisseur == 40) {
                        for (Component box : panScientifique.panneauEpaisseurs.getComponents()) {
                            if (box instanceof JCheckBox && ((JCheckBox) box).getText().equals(valeurChampEpaisseur + "mm")) {
                                ((JCheckBox) box).doClick();
                            }
                        }
                    }
                    else {  // on effectue une moyenne pondérée des épaisseurs voisines pour toutes les autres épaisseurs
                        panScientifique.DessinGraphique.afficherEpaisseurIntermediaire = true;  // l'épaisseur sélectionnée est inconnue et comprise entre des épaisseurs classiques (de 0 à 20mm + 30mm + 40mm)
                        panScientifique.DessinGraphique.epaisseurIntermediaire = valeurChampEpaisseur;  // affecter la valeur saisie à la variable épaisseur intermédiaire (de 0 à 20mm + 30mm + 40mm) : vaut 0 dans le cas où l'épaisseur est classique connu
                    }

                    // par défaut, sélection des valeurs expérimentales
                    for (Component box : panScientifique.panneauValeurs.getComponents()) {
                        if (box instanceof JCheckBox && ((JCheckBox) box).getText().equals("Valeurs Expérimentales")) {
                            ((JCheckBox) box).doClick();
                        }
                    }

                    cardPanel.add(panScientifique, "GraphiqueTemp");
                    cardLayout.show(cardPanel, "GraphiqueTemp");

                    FPrincipale fenetre = (FPrincipale) SwingUtilities.getWindowAncestor(this);
                    fenetre.afficherVue("GraphiqueTemp");  // autoriser le redimensionnement de la fenêtre des graphiques pour une meilleure lisiblité
                }
            }
        });
        
        
        champEpaisseur.setMaximumSize(new Dimension(80, 25));
        champEpaisseur.setForeground(Color.DARK_GRAY);
        gauche.add(champEpaisseur);

        JLabel erreurEpaisseur = new JLabel(" ");  // défaut
        erreurEpaisseur.setForeground(Color.RED);
        gauche.add(erreurEpaisseur);

        /* Section des citations */
        
        this.citationLabel = new JLabel("\"");
        this.citationLabel.setFont(new Font("Brush Script MT", Font.ITALIC, 18));
        this.citationLabel.setForeground(Color.DARK_GRAY);
        gauche.add(Box.createRigidArea(new Dimension(0, 30)));
        gauche.add(this.citationLabel);

        String[] citations = {
            "<html><b style='color:#f0dcc5;'>Balsa</b> : Bois très léger, excellent isolant <br> thermique, utilisé en modélisme ou <br>"
            + " pour les structures isolantes dans <br> les drones et avions miniatures.</html>",
            
            "<html><b style='color:#a39370;'>Chêne</b> : Bois dense à forte inertie thermique, <br> utilisé pour les parquets chauffants <br>"
            + " ou la fabrication de meubles massifs <br> exposés à des variations de température.</html>",
            
            "<html><b style='color:#c1b07e;'>Épicéa</b> : Bois tendre souvent utilisé dans la <br> construction de chalets et d’instruments <br>"
            + " de musique (tables d’harmonie), <br> caractérisé par une faible inertie thermique.</html>",
            
            "<html><b style='color:#b4ad94;'>Mélèze</b> : Bois résineux naturellement résistant <br> à l'humidité, utilisé pour les "
            + "bardages extérieurs <br> en zones froides, avec une conductivité <br> thermique modérée.</html>",
            
            "<html><b style='color:#f5e4ac;'>Peuplier</b> : Bois clair et léger, souvent employé <br> pour les contreplaqués ou les"
            + " palettes, avec <br> une conductivité thermique faible, utile dans les structures isolées à faible charge.</html>",
        };
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int index = 0;
            @Override
            public void run() {
                citationLabel.setText(citations[index]);
                index = (index + 1) % citations.length;
            }
        }, 0, 5000);

        droite.add(Box.createRigidArea(new Dimension(0, 30)));

        /* Slider choix épaisseurs */
        
        JSlider epaisseurSlider = new JSlider(2, 40, 20);
        epaisseurSlider.setUI(new GradientSliderUI(epaisseurSlider));
        epaisseurSlider.setPaintTicks(true);
        epaisseurSlider.setPaintLabels(true);
        epaisseurSlider.setMajorTickSpacing(5);
        epaisseurSlider.setMinorTickSpacing(1);
        epaisseurSlider.setOpaque(false);

        epaisseurSlider.addChangeListener(e -> {
        if (!champEpaisseur.isFocusOwner()) {
                champEpaisseur.setText(String.valueOf(epaisseurSlider.getValue()));
            }
        });
        
        champEpaisseur.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                String text = champEpaisseur.getText();
                try {
                    int value = Integer.parseInt(text);
                    if (value >= 2 && value <= 40) {
                        epaisseurSlider.setValue(value);
                        erreurEpaisseur.setText(" ");
                    } else {
                        erreurEpaisseur.setText("Valeur hors limites (2–40)");
                    }
                } catch (NumberFormatException ex) {
                    if (text.isEmpty()) {  // le champ de saisie est vide
                        erreurEpaisseur.setText("Aucune épaisseur spécifique saisie");
                    }
                    else {  // la saisie est invalide
                        erreurEpaisseur.setText("Entrée invalide");
                    }
                }
            }
            @Override
            public void insertUpdate(DocumentEvent e) { update(); }
            @Override
            public void removeUpdate(DocumentEvent e) { update(); }
            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        JLabel flechesSlider = new JLabel("← →");
        flechesSlider.setFont(new Font("Arial", Font.BOLD, 24));
        droite.add(flechesSlider, BorderLayout.WEST);
        droite.add(epaisseurSlider);
        droite.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        boutons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        boutons.setOpaque(false);
        
        JButton btnComparatif = new JButton("Lancer la modélisation");  // bouton pour accéder à la page des graphiques
        btnComparatif.setCursor(new Cursor(Cursor.HAND_CURSOR));  // modifier le curseur quand le curseur survole le bouton
        btnComparatif.addActionListener(e -> {
            
            boolean estNombre = true;
            int valeurChampEpaisseur;
            
            try {
                valeurChampEpaisseur = Integer.parseInt(champEpaisseur.getText());
            } catch (NumberFormatException error) {
                estNombre = false;
                System.out.println(error);
            }
            
            if (estNombre || champEpaisseur.getText().equals("")) {
                valeurChampEpaisseur = Integer.parseInt(champEpaisseur.getText());
            
                PanneauScientifique panScientifique = new PanneauScientifique(essenceChoisie, valeurChampEpaisseur);
                panScientifique.DessinGraphique.setEpaisseur(valeurChampEpaisseur, true);

                // séletion du bouton de l'épaisseur saisie si l'épaisseur est classique (de 0 à 20mm + 30mm + 40mm)
                if (((2 <= valeurChampEpaisseur && valeurChampEpaisseur <= 20) && valeurChampEpaisseur % 2 == 0) || valeurChampEpaisseur == 30 || valeurChampEpaisseur == 40) {
                    for (Component box : panScientifique.panneauEpaisseurs.getComponents()) {
                        if (box instanceof JCheckBox && ((JCheckBox) box).getText().equals(valeurChampEpaisseur + "mm")) {
                            ((JCheckBox) box).doClick();
                        }
                    }
                }
                else {  // on effectue une moyenne des épaisseurs voisines pour toutes les autres épaisseurs      
                    panScientifique.DessinGraphique.afficherEpaisseurIntermediaire = true;  // l'épaisseur sélectionnée est inconnue et comprise entre des épaisseurs classiques (de 0 à 20mm + 30mm + 40mm)
                    panScientifique.DessinGraphique.epaisseurIntermediaire = valeurChampEpaisseur;  // affecter la valeur saisie à la variable épaisseur intermédiaire (de 0 à 20mm + 30mm + 40mm) : vaut 0 dans le cas où l'épaisseur est classique connu
                }

                // par défaut, on sélectionne les valeurs expérimentales
                for (Component box : panScientifique.panneauValeurs.getComponents()) {
                    if (box instanceof JCheckBox && ((JCheckBox) box).getText().equals("Valeurs Expérimentales")) {
                        ((JCheckBox) box).doClick();
                    }
                }

                cardPanel.add(panScientifique, "GraphiqueTemp");
                cardLayout.show(cardPanel, "GraphiqueTemp");

                FPrincipale fenetre = (FPrincipale) SwingUtilities.getWindowAncestor(this);
                fenetre.afficherVue("GraphiqueTemp");  // autoriser le redimensionnement de la fenêtre des graphiques pour une meilleure lisiblité
            }
        });
        
        boutons.add(btnComparatif);

        JButton btnAide = new JButton("Notice d'utilisation");
        btnAide.setCursor(new Cursor(Cursor.HAND_CURSOR));  // modifier le curseur quand le curseur survole le bouton
        btnAide.addActionListener(e -> cardLayout.show(cardPanel, "Notice"));
        boutons.add(btnAide);
        
        JButton btnRetourAccueil = new JButton("Accueil →");
        btnRetourAccueil.setSize(new Dimension(200, 20));
        btnRetourAccueil.setCursor(new Cursor(Cursor.HAND_CURSOR));  // modifier le curseur quand le curseur survole le bouton
        btnRetourAccueil.addActionListener(e -> cardLayout.show(cardPanel, "Accueil"));
        boutons.add(btnRetourAccueil);
        
        droite.add(boutons);  // ajout des trois boutons au panneau
        
        panelCentre.add(gauche);
        panelCentre.add(droite);
        add(panelCentre, BorderLayout.CENTER);
        
        JPanel panneauBas = new JPanel();
        panneauBas.setLayout(new BoxLayout(panneauBas, BoxLayout.Y_AXIS));
        panneauBas.setOpaque(false);

        ImagePanel logoValo = new ImagePanel("img/Logo_Valo.png", 80, 80);
        logoValo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoValo.setOpaque(false);

        panneauBas.add(Box.createRigidArea(new Dimension(0, 10)));
        panneauBas.add(logoValo);

        JLabel namesLabel = new JLabel("\u00a9 Loïc.B et Valentin.E", SwingConstants.CENTER);
        namesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        namesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauBas.add(Box.createRigidArea(new Dimension(0, 10)));
        panneauBas.add(namesLabel);

        add(panneauBas, BorderLayout.SOUTH);

    }  // fin du constructeur PModeleChaleur

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }  // fin paintComponent

    private static class GradientSliderUI extends BasicSliderUI {
        public GradientSliderUI(JSlider slider) { super(slider); }
        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            Rectangle trackBounds = trackRect;
            GradientPaint gradient = new GradientPaint(
                    trackBounds.x, trackBounds.y, Color.YELLOW,
                    trackBounds.x + trackBounds.width, trackBounds.y, Color.RED);
            g2d.setPaint(gradient);
            g2d.fillRect(trackBounds.x, trackBounds.y + trackBounds.height / 3,
                    trackBounds.width, trackBounds.height / 3);
            g2d.setColor(Color.GRAY);
            g2d.drawRect(trackBounds.x, trackBounds.y + trackBounds.height / 3,
                    trackBounds.width, trackBounds.height / 3);
        }
    }  // fin GradientSliderUI
}  // fin de la classe PModeleChaleur
