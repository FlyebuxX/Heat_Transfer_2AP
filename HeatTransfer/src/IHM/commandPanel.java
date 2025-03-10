package IHM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class commandPanel extends JPanel{

    private JButton back;
    private JButton next;

    public commandPanel(String backName, String nextName) {
        this.setLayout(new FlowLayout());

        back = new JButton(backName);
        next = new JButton(nextName);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Bouton back");
                if (Objects.equals(backName, "Quitter")) {
                    System.exit(0);
                }
                 else {
                     // action
                    int a;
                }
            }
            // action
        });

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Bouton next.");

            }
        });

        this.add(back);
        this.add(next);

    }

}
