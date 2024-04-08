package modele;


import VueControleur.VueControleur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
public class Echouer extends JDialog {
    public Echouer(JFrame parent) {
        super(parent, "Echouer!", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(2, 1));

        JLabel label = new JLabel("Opss!Echouer, Vous avez été mangé !");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        panel.add(label);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton returnButton = new JButton("Réessayez");
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                // Création d'objets pour le robot, Il peut être utilisé pour simuler les opérations du clavier et de la souris de l'utilisateur
                try {
                    Robot robot = new Robot();
                    // Simule l'appui sur la touche R sur clavier
                    robot.keyPress(KeyEvent.VK_S);
                    robot.keyRelease(KeyEvent.VK_S);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonPanel.add(returnButton);

        JButton nextLevelButton = new JButton("Quitter");
        nextLevelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                System.exit(0);

            }
        });
        buttonPanel.add(nextLevelButton);

        panel.add(buttonPanel);

        add(panel);
    }
}
