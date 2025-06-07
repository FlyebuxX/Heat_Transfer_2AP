package IHM;

import assets.ImagePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Classe défininissant la page de démarrage de l'application
 * @author Loïc BELLEC & Valentin EBERHARDT
 */
public class PAccueil extends JPanel {

    private final Image backgroundImage;

    /**
     * Constructeur du panneau d'accueil
     * @param cardLayout CardLayout : paquet de panneaux à afficher
     * @param cardPanel  JPanel : panneau affiché sur l'écran de l'utilisateur
     */
    public PAccueil(CardLayout cardLayout, JPanel cardPanel) {
        setLayout(new BorderLayout());

        // Image pour l'arrière plan
        backgroundImage = new ImageIcon("img/PAccueil.png").getImage();

        /* Panneau principal transparent pour tous les contenus de la page d'accueil */
        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setOpaque(false);

        /* Section haute de la page d'accueil haut */
        JPanel haut = new JPanel();
        haut.setOpaque(false);
        haut.setLayout(new BoxLayout(haut, BoxLayout.Y_AXIS));

        // Logo
        ImagePanel logo = new ImagePanel("img/Logo_Valo.png", 300, 300);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setOpaque(false);
        haut.add(Box.createVerticalGlue());
        haut.add(logo);

        // Titre du projet
        JLabel titre = new JLabel("Modélisation des transferts de chaleur dans bois sec");
        titre.setFont(new Font("Arial", Font.BOLD, 30));
        titre.setForeground(new Color(Color.HSBtoRGB(0.6f, 1f, 0.3f))); // Couleur bleue unie de texte
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        haut.add(Box.createRigidArea(new Dimension(0, 20)));
        haut.add(titre);

        contenu.add(haut, BorderLayout.NORTH);  // Ajout du contenu de la partie supérieure de la page d'accueil dans le JPanel

        /* JPanel au centre : bouton lancer */
        JPanel centre = new JPanel();
        centre.setOpaque(false);
        JButton lancerBtn = new JButton("LANCER");
        lancerBtn.setFont(new Font("Arial", Font.BOLD, 18));
        lancerBtn.setPreferredSize(new Dimension(150, 50));
        lancerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));  // modifier le curseur quand le curseur survole le bouton
        lancerBtn.addActionListener(e -> cardLayout.show(cardPanel, "ChaleurBois")); // Ouverture dans le card FPrincipale
        centre.add(lancerBtn);
        contenu.add(centre, BorderLayout.CENTER);

        /* JPanel dans la partie inférieure du panneau d'accueil */
        JPanel bas = new JPanel(new BorderLayout());
        bas.setOpaque(false);

        // Lien GitHub gauche avec icône
        ImageIcon iconGithub = new ImageIcon("img/github.png");
        Image img = iconGithub.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JButton lienGithub = new JButton(new ImageIcon(img)); // Ajout d'une icone sur le JButton
        lienGithub.setPreferredSize(new Dimension(40, 40));
        lienGithub.setFocusPainted(false);
        lienGithub.setContentAreaFilled(false);
        lienGithub.setBorderPainted(false);
        lienGithub.setOpaque(false);
        lienGithub.setCursor(new Cursor(Cursor.HAND_CURSOR));  // modifier le curseur quand le curseur survole le lien
        
        lienGithub.addActionListener((ActionEvent e) -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/FlyebuxX/Heat_Transfer_2AP/"));
            } catch (IOException | URISyntaxException ex) {
                System.out.println("Erreur. échec du chargement de l'URL Github");
            }
        });
        bas.add(lienGithub, BorderLayout.WEST);

        /* Noms centrés via un panel dédié */
        JPanel nomsPanel = new JPanel();
        nomsPanel.setOpaque(false);
        JLabel namesLabel = new JLabel("© Loïc.B et Valentin.E");
        namesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        namesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nomsPanel.add(namesLabel);
        bas.add(nomsPanel, BorderLayout.CENTER);

        // Bouton info droite
        JButton infoBtn = new JButton("i");
        infoBtn.setBackground(Color.WHITE);
        infoBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));  // modifier le curseur quand le curseur survole le bouton
        infoBtn.setPreferredSize(new Dimension(40, 40));
        infoBtn.addActionListener(e -> cardLayout.show(cardPanel, "Explain")); // Ouvrir la page PExplain
        bas.add(infoBtn, BorderLayout.EAST);

        contenu.add(bas, BorderLayout.SOUTH);

        // Ajout du panneau de contenu au panneau principal
        add(contenu, BorderLayout.CENTER);
    }  // fin du constructeur

    /**
     * Méthode permettant d'afficher l'image de fond à la page d'accueil
     * @param g Graphics : composant graphique
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.backgroundImage != null) {
            g.drawImage(this.backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }  // fin paintComponent
}  // fin de la classe PAccueil
