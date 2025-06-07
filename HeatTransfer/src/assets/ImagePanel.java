package assets;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe qui permet de définir un objet image et de l'afficher
 * /@author Loïc BELLEC & Valentin EBERHARDT
 */
public class ImagePanel extends JPanel {
    private BufferedImage image;  // image
    private int largeurImage;  // largeur de l'image
    private int hauteurImage;  // hauteur de l'image

    /**
     * Constructeur à 3 paramètres du panneau contenant l'image
     * @param imageUrl chemin absolu de l'image
     * @param lImage largeur de l'image
     * @param hImage hauteur de l'image
     */
    public ImagePanel(String imageUrl, int lImage, int hImage) {
        super();
        this.largeurImage = lImage;
        this.hauteurImage = hImage;
        this.setPreferredSize(new Dimension(this.largeurImage, this.hauteurImage));
        this.setBackground(Color.white);  // panneua contenant l'image au fond blanc

        try {
            image = ImageIO.read(new File(imageUrl));
        } catch (IOException ex) {
            Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  // fin du constructeur ImagePanel

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, (this.getWidth() - this.largeurImage)/ 2, (this.getHeight()- this.hauteurImage) / 2, this.largeurImage, this.hauteurImage, this);
    }  // fin paintComponent
}  // fin de la classe ImagePanel