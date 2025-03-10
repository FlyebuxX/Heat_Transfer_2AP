package IHM;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class setGradientColor extends JPanel {

    int x = 0;  // separation between red and orange

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        Color color1 = Color.orange;
        Color color2 = Color.RED;
        GradientPaint gp = new GradientPaint(this.x, 0, color1, 0, 0, color2);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setLayout(new BorderLayout());
                setGradientColor panel = new setGradientColor();
                panel.setPreferredSize(new Dimension(1000, 200));
                frame.add(panel, BorderLayout.WEST);  // lumber modelling on the left of the frame
                JButton btn = new JButton("Progression");
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        panel.x += 25; // propagation step
                        panel.paintComponent(panel.getGraphics());  // repaint the lumber with the new abscissa (no
                        // setter is available to update without repainting directly)
                        System.out.println("x = " + panel.x);
                    }
                    // action
                });
                frame.add(btn, BorderLayout.EAST);  // button for heat propagation on the right of the frame
                frame.setSize(1200, 200);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}