
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
                // 创建开始菜单界面
                JFrame startMenuFrame = new JFrame("Start Menu");
                // 设置当用户关闭窗口时默认的关闭操作为退出应用程序。
                startMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // 创建开始菜单视图
                StartMenuView startMenuView = new StartMenuView();
                // 将开始菜单视图添加到主窗口的内容面板中。
                startMenuFrame.getContentPane().add(startMenuView);

                // 调整窗口大小并居中显示
                startMenuFrame.pack();
                startMenuFrame.setSize(600, 400);
                startMenuFrame.setLocationRelativeTo(null);

                // 设置开始菜单界面可见
                startMenuFrame.setVisible(true);

                // 等待用户点击开始按钮
                startMenuView.waitForStart();

                // 关闭开始菜单界面
                startMenuFrame.dispose();

                // 创建游戏对象
                Jeu jeu = new Jeu();

                // 创建视图控制器并显示游戏界面
                VueControleur vc = new VueControleur(jeu);
                vc.setVisible(true);
        }
}

