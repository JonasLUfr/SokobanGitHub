package modele;


//import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Victoire extends JDialog {
    private int coinsEaten;
    public Victoire(JFrame parent, int coinsEaten) {
        super(parent, "Victory!", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        this.coinsEaten = coinsEaten;

        JPanel panel = new JPanel(new GridLayout(3, 1));

        JLabel label = new JLabel("Félicitations, vous avez gagné le jeu !");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        panel.add(label);

        JLabel coinLabel = new JLabel("Coins restants : " + this.coinsEaten + "/4"); // Afficher le nombre de jetons d'or restants
        coinLabel.setHorizontalAlignment(JLabel.CENTER);
        coinLabel.setVerticalAlignment(JLabel.CENTER);
        panel.add(coinLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton returnButton = new JButton("Réessayez");
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                setVisible(false);
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
                // Pas Fini
                try {
                    Robot robot = new Robot();
                    // Simule l'appui sur la touche R sur clavier
                    robot.keyPress(KeyEvent.VK_Q);
                    robot.keyRelease(KeyEvent.VK_Q);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }

            }
        });
        buttonPanel.add(nextLevelButton);

        panel.add(buttonPanel);

        add(panel);
    }

    //Pour l'affichage en temps réel (non terminé encore)
    public void setCoinsEaten(int coinsEaten) {
            this.coinsEaten = coinsEaten;}
    public int getCoinsEaten() {
        return coinsEaten;
    }

}