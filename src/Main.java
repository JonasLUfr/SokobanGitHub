
import VueControleur.VueControleur;
import modele.Jeu;
import modele.StartMenuView;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class Main {
        public static void main(String[] args) {
                startGame();
        }

        private static void startGame() {
                // Création d'un écran de menu de démarrage
                JFrame startMenuFrame = new JFrame("Start Menu");
                // Définit l'action de fermeture par défaut pour quitter l'application lorsque l'utilisateur ferme la fenêtre.
                startMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Création d'une vue du menu Démarrer
                StartMenuView startMenuView = new StartMenuView();
                // Ajoute la vue du menu Démarrer au panneau de contenu de la fenêtre principale.
                startMenuFrame.getContentPane().add(startMenuView);

                // Redimensionner et centrer la fenêtre
                startMenuFrame.pack();
                startMenuFrame.setSize(600, 400);
                startMenuFrame.setLocationRelativeTo(null);

                // Réglage de la visibilité de l'écran du menu de démarrage
                startMenuFrame.setVisible(true);

                // Attendre que l'utilisateur clique sur le bouton de démarrage
                startMenuView.waitForStart();

                // Fermer l'écran du menu Démarrer
                startMenuFrame.dispose();

                // Création d'objets de jeu
                Jeu jeu = new Jeu();

                // Création d'un contrôleur de vue et affichage de l'interface de jeu
                VueControleur vc = new VueControleur(jeu);
                vc.setVisible(true);
        }
}

