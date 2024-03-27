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
    private boolean isPaused; // Pour Indiquer l'etat courant
    private ImageIcon pauseImage; // Image Pause
    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)
    private JDialog dialog; // Enregistre une référence au dialogue de pause.

    // Créer une instance du dialogue et spécifier sa taille
    int dialogWidth = 400;
    int dialogHeight = 250;

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

    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
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
                        case KeyEvent.VK_LEFT: jeu.deplacerHeros(Direction.gauche); break;
                        case KeyEvent.VK_RIGHT: jeu.deplacerHeros(Direction.droite); break;
                        case KeyEvent.VK_DOWN: jeu.deplacerHeros(Direction.bas); break;
                        case KeyEvent.VK_UP: jeu.deplacerHeros(Direction.haut); break;
                    }
                }
            }
        });
    }

    private void chargerLesIcones() {
        icoHero = chargerIcone("Images/Pacman.png");
        icoVide = chargerIcone("Images/Vide.png");
        icoMur = chargerIcone("Images/Mur.png");
        icoBloc = chargerIcone("Images/Colonne.png");
        icoIce = chargerIcone("Images/Fantome.png"); //temp
        pauseImage =chargerIcone("Images/Pause.png"); //pauseImage
    }

    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleur.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new ImageIcon(image);
    }

    // Créer une boîte de dialogue pour afficher l'image de pause
    private void afficherPauseMenu() {
        if (dialog == null) {
            dialog = new JDialog(this, "Pause", true);
            //dialog.getContentPane().add(new JLabel(pauseImage));
            //注意这里为了优化Pause的图片显示，使得图片跟随窗口大小改变而改变，如果仅使用上一条，若窗口大小小于原图片尺寸，则会出现图片的剪裁！
            // 缩放图像以适应对话框大小
            Image scaledImage = pauseImage.getImage().getScaledInstance(dialogWidth, dialogHeight, Image.SCALE_SMOOTH);
            // 创建缩放后的图像图标
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            // 创建显示图像的标签
            JLabel label = new JLabel(scaledIcon);
            // 将标签添加到对话框的内容面板中
            dialog.getContentPane().add(label);

            dialog.setSize(dialogWidth, dialogHeight);
            dialog.setLocationRelativeTo(null);
            //dialog.setVisible(true);
            dialog.addKeyListener(new KeyAdapter() { // 添加键盘事件监听器，以便在用户按下 'P' 键时隐藏暂停菜单并继续游戏
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_P) {
                        isPaused = false;
                        if (dialog != null) {
                            dialog.dispose(); // 关闭暂停对话框
                        }
                        //cacherPauseMenu(); // 隐藏暂停菜单并继续游戏
                    }


                }
            });
        }
        dialog.setVisible(true);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Sokoban");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
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
                        }
                    } else {
                        if (jeu.getGrille()[x][y] instanceof Mur) {
                            tabJLabel[x][y].setIcon(icoMur);
                        } else if (jeu.getGrille()[x][y] instanceof Vide) {
                            tabJLabel[x][y].setIcon(icoVide);
                        } else if (jeu.getGrille()[x][y] instanceof Ice) {
                            // 设置 Ice 实体的图标
                            tabJLabel[x][y].setIcon(icoIce);
                        }

                    }
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        /*

        // récupérer le processus graphique pour rafraichir
        // (normalement, à l'inverse, a l'appel du modèle depuis le contrôleur, utiliser un autre processus, voir classe Executor)


        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }
}
