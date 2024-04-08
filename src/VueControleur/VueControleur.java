package VueControleur;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

import modele.*;

/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;

    // icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoVide;
    private ImageIcon icoMur;
    private ImageIcon icoBloc;
    private ImageIcon icoIce;  //ajouter Ice

    private ImageIcon icoPorte; // la sortie

    private ImageIcon icoCoin; // Coin
    private ImageIcon icoGhost; //fantome
    private boolean isPaused; // Pour Indiquer l'etat courant
    private ImageIcon pauseImage; // Image Pause
    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)
    private JDialog dialog; // Enregistre une référence au dialogue de pause.
    private Direction InversDirection;  // Enregistre une  InversDirection
    private int retournerCount = 0;; // Compteur de retournner()

    // Créer une instance du dialogue et spécifier sa taille
    int dialogWidth = 549;
    int dialogHeight = 541;

    public VueControleur(Jeu _jeu) {
        sizeX = jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;
        jeu = _jeu;

        isPaused = false;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();

        jeu.addObserver(this);

        mettreAJourAffichage();

        // Demande de focus du jeu
        this.requestFocusInWindow();

    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Key pressed: " + e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_P) {  //Quand on appuie le P sur clavier
                    if (isPaused) {
                        isPaused = false;
                        mettreAJourAffichage();
                    } else {
                        isPaused = true;
                        afficherPauseMenu(); // Le menu pause s'affiche pendant la pause.
                    }
                } else if (!isPaused) { // Répondre aux pressions sur les touches fléchées uniquement lorsque le jeu n'est pas en pause
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT: InversDirection = Direction.droite; jeu.deplacerHeros(Direction.gauche); break;
                        case KeyEvent.VK_RIGHT: InversDirection = Direction.gauche; jeu.deplacerHeros(Direction.droite); break;
                        case KeyEvent.VK_DOWN: InversDirection = Direction.haut; jeu.deplacerHeros(Direction.bas); break;
                        case KeyEvent.VK_UP: InversDirection = Direction.bas; jeu.deplacerHeros(Direction.haut); break;
                        case KeyEvent.VK_R: retournerHero(InversDirection);  break;// Ramène le héros à la position précédente lorsque la touche R est enfoncée.
                        case KeyEvent.VK_Q: System.exit(0);  break; //Quand on appuie le Q sur clavier
                        case KeyEvent.VK_S: int choix = JOptionPane.showConfirmDialog(null, "Vous êtes sûr de redémarrer le jeu ?", "Redémarrer", JOptionPane.YES_NO_OPTION);
                            if (choix == JOptionPane.YES_OPTION) {
                            // Si Yes:
                            redemarrerJeu();
                            }
                    }
                }
            }
        });
    }
    private void redemarrerJeu() {
        // Logique de redémarrage du jeu
        jeu.deleteObserver(this); // Supprimer l'observateur du jeu en cours
        //Recréer l'objet du jeu et rafraîchir l'interface utilisateur.
        retournerCount = 0;
        jeu = new Jeu(); // Recréer l'objet du jeu
        jeu.addObserver(this); // Recréer l'observateur
        mettreAJourAffichage();
    }
    public void incrementerRetournerCount() {// 这样做是为了显示retournerCount在菜单中！提升为全局变量
        retournerCount++;
    }
    /*
    public void decrementerRetournerCount() {
        retournerCount--;
    }
     */
    //Undo hero avec bloc Max 5 fois
    public void retournerHero(Direction d) {
        if (retournerCount < 5) { // vérifie si la valeur du compteur est inférieure à 5.
            Heros hero = jeu.getHeros();
            Bloc bloc = jeu.getBloc(); // Obtenir l'objet bloc
            if (hero != null) {
                hero.retourner(d); // Retour du héros à la position précédente, méthode 1
                bloc.retourner(); // Retour du bloc à la position précédente, méthode 2
                incrementerRetournerCount();
                //retournerCount++; // incre
            }
            mettreAJourAffichage(); // UpdateUI
        } else {
            System.out.println("retournerHero Undo deja 5 fois。"); // printout
        }
    }
    private void chargerLesIcones() {
        icoHero = chargerIcone("Images/Pacman.png");
        icoVide = chargerIcone("Images/Vide.png");
        icoMur = chargerIcone("Images/Mur.png");
        icoBloc = chargerIcone("Images/Colonne.png");
        icoIce = chargerIcone("Images/Ice.png"); //Ice 18*19
        pauseImage =chargerPause("Images/Pause.png"); //pauseImage
        icoPorte =  chargerIcone("Images/Porte.png");
        icoCoin =  chargerIcone("Images/Coin.png");
        icoGhost =  chargerIcone("Images/Fantome.png");

    }

    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
            Image resizedImage = image.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            //mettre toutes les images de la forme concrete 18*18 pour eviter les problemes de la forme

            return new ImageIcon(resizedImage);
        } catch (IOException ex) {
            Logger.getLogger(VueControleur.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        ///return new ImageIcon(image);
    }
    private ImageIcon chargerPause(String urlIcone) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(urlIcone));

            return new ImageIcon(image);
        } catch (IOException ex) {
            Logger.getLogger(VueControleur.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    // Créer une boîte de dialogue pour afficher l'image de pause
    private void afficherPauseMenu() {
        if (dialog == null) {
            dialog = new JDialog(this, "Pause", true);
            //注意这里为了优化Pause的图片显示，使得图片跟随窗口大小改变而改变，如果仅使用上一条，若窗口大小小于原图片尺寸，则会出现图片的剪裁！
            //Adapter l'image à la taille de la boîte de dialogue “Image.SCALE_SMOOTH”
            Image scaledImage = pauseImage.getImage().getScaledInstance(dialogWidth, dialogHeight, Image.SCALE_SMOOTH);
            // Création d'icônes à échelle réduite “创建缩放后的图像图标”
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            // Création d'étiquettes pour l'affichage des images
            JLabel label = new JLabel(scaledIcon);
            // Ajout d'étiquettes au panneau de contenu d'une boîte de dialogue
            dialog.getContentPane().add(label);

            dialog.setSize(dialogWidth, dialogHeight);
            dialog.setLocationRelativeTo(null);
            //dialog.setVisible(true);
            dialog.addKeyListener(new KeyAdapter() {
                // Ajoutez un contrôleur d'événements clavier pour masquer le menu de pause et poursuivre le jeu lorsque l'utilisateur appuie sur la touche 'P'.
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_P) {
                        isPaused = false;
                        if (dialog != null) {
                            dialog.dispose(); // fermer le
                        }
                    }


                }
            });
        }
        dialog.setVisible(true);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Sokoban");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Création de panneaux de jeu
        JPanel gamePanel = new JPanel(new GridLayout(sizeY, sizeX));
        tabJLabel = new JLabel[sizeX][sizeY];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab;
                gamePanel.add(jlab);
            }
        }

        // Créer un panneau de texte et ajouter du texte
        JPanel textPanel = new JPanel();
        JPanel textPanel2 = new JPanel();
        JLabel textLabel = new JLabel("Appuyez P:pause | Q:quitter | S:redemarrer | R:undo(5Max)");
        JLabel textLabel2 = new JLabel("Object: Reach the Final Point.");
        textPanel.add(textLabel);
        textPanel2.add(textLabel2);

        // Créer un panneau entier contenant le panneau de jeu et le panneau de texte
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(textPanel, BorderLayout.NORTH);
        mainPanel.add(textPanel2, BorderLayout.SOUTH);


        // Ajout ce panneau intégral à une boîte
        add(mainPanel);
    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */

    private void mettreAJourAffichage() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                Case c = jeu.getGrille()[x][y];

                if (c != null) {

                    Entite e = c.getEntite();

                    if (e != null) {
                        if (c.getEntite() instanceof Heros) {
                            tabJLabel[x][y].setIcon(icoHero);
                        } else if (c.getEntite() instanceof Bloc) {
                            tabJLabel[x][y].setIcon(icoBloc);
                        } else if (c.getEntite() instanceof Ghost) {
                            tabJLabel[x][y].setIcon(icoGhost);  //charger Ghost
                        }
                    } else {
                        if (jeu.getGrille()[x][y] instanceof Mur) {
                            tabJLabel[x][y].setIcon(icoMur);
                        } else if (jeu.getGrille()[x][y] instanceof Vide) {
                            tabJLabel[x][y].setIcon(icoVide);
                        } else if (jeu.getGrille()[x][y] instanceof Ice) {
                            tabJLabel[x][y].setIcon(icoIce); // Définir l'icône de l'entité Ice
                        } else if (jeu.getGrille()[x][y] instanceof Porte) {
                            tabJLabel[x][y].setIcon(icoPorte); // Définir l'icône de l'entité porte
                        } else if (jeu.getGrille()[x][y] instanceof Coin) {
                            tabJLabel[x][y].setIcon(icoCoin); // Définir l'icône de l'entité coin
                        }

                    }
                }
            }
        }

        // centrer le boit dialog
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void update(Observable o, Object arg) {
        //mettreAJourAffichage();
        //jeu.moveGhost(jeu.getGhost());

        // Appelez la méthode moveGhost dans le jeu.java sur chaque fantôme pour les déplacer dans chaque update.
        for (Ghost ghost : jeu.getGhosts()) {
            jeu.moveGhost(ghost);
        }

        // récupérer le processus graphique pour rafraichir
        // (normalement, à l'inverse, a l'appel du modèle depuis le contrôleur, utiliser un autre processus, voir classe Executor)
        //在 Java Swing 中，要实现 UI 的实时更新，您可以使用 SwingUtilities.invokeLater() 方法。这样可以确保更新操作在事件分派线程（Event Dispatch Thread）上执行，以避免多线程并发问题。
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                });

    }
}


