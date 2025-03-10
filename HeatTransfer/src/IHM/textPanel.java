package IHM;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class textPanel extends JPanel {

    public textPanel (int width, int height, String text) {
        this.setLocation(400, 300);
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.orange);
        this.setBorder(new LineBorder(Color.black, 3));

        JLabel textLabel = new JLabel(text);

        this.add(textLabel);

    }

}
