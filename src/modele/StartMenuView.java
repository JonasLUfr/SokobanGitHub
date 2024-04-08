package modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.System;

public class StartMenuView extends JPanel {  //JPanel 是 Java Swing 中的一个轻量级容器组件，用于组织和管理其他组件。它可以包含其他 Swing 组件，如按钮、标签、文本框等，从而构建更复杂的用户界面。
    private JButton startButton;
    private JButton quitButton;
    private JLabel titleLabel;
    private Object lock = new Object(); // Verrouillage de l'objet pour la synchronisation des threads lock

    private Musique musique; // ajouter l'objet Musique

    public StartMenuView() {
        System.out.println("StartMenuView constructor called"); //debug
        //Utilisez BorderLayout pour configurer le gestionnaire de mise en page du panneau, ce qui aura pour effet de disposer les composants sur le panneau dans une mise en page de bordure.
        setLayout(new BorderLayout());
        //Création de titres de jeux
        titleLabel = new JLabel("PUSHCASE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        //创建一个新的面板（JPanel），使用网格布局（GridLayout），行数为 2，列数为 1，水平和垂直间距均为 10。
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        startButton = new JButton("BEGIN");
        quitButton = new JButton("OUT");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理开始按钮点击事件
                startGame();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理退出按钮点击事件
                quitGame();
            }
        });

        buttonPanel.add(startButton);
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.CENTER);

        // 创建 Musique 对象并播放第一个背景音乐
        musique = new Musique();
        musique.playBackgroundMusic1();

    }

    private void startGame() {
        // 进入游戏
        // 在这里添加代码来启动游戏
        /*
            // 创建游戏对象
            Jeu jeu = new Jeu();
            // 创建视图控制器并显示游戏界面
            VueControleur vc = new VueControleur(jeu);
             vc.setVisible(true);
         * */
        ///停止播放第一个背景音乐，并播放第二个背景音乐

        musique.playBackgroundMusic2();
        synchronized (lock) {
            lock.notifyAll(); // 通知等待的线程可以继续执行
        }
        System.out.println("Game started!!");
    }

    private void quitGame() {
        System.out.println("Game stop!!");
        System.exit(0);
    }

    public void waitForStart() {
        synchronized (lock) {
            try {
                lock.wait(); // Attente d'un clic sur le bouton de démarrage！
            } catch (InterruptedException e) {
                System.out.println("problem lock");
                e.printStackTrace();
            }
        }
    }


}
