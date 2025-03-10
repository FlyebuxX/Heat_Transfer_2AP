package IHM;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private int width;
    private int height;
    
    public ImagePanel(String urlImage, int w, int h) {
        this.width = w;
        this.height = h;

        try {
            image = ImageIO.read(new File(urlImage));

        } catch (IOException ex) {
            Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this.width, this.height, this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int widthImage = 700;
                int heightImage = 700;
                JFrame frame = new JFrame();
                
                ImagePanel panel = new ImagePanel(
                "img/balsa.jpg",
                widthImage,
                heightImage);

                frame.setSize(widthImage, heightImage);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                
                frame.add(panel);
            }
        });
    }
}