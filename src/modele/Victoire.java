package modele;


//import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Victoire extends JDialog {
    public Victoire(JFrame parent) {
        super(parent, "Victory!", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(2, 1));

        JLabel label = new JLabel("Félicitations, vous avez gagné le jeu !");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        panel.add(label);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton returnButton = new JButton("Revenir au Menu");
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 处理返回菜单的逻辑
                // 关闭当前对话框
                setVisible(false);
                // 退出当前程序并重新启动主菜单程序;
            }
        });
        buttonPanel.add(returnButton);

        JButton nextLevelButton = new JButton("Niveau suivant");
        nextLevelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 处理前往下一关的逻辑
                // 关闭当前对话框
                setVisible(false);
                // 返回到开始菜单

            }
        });
        buttonPanel.add(nextLevelButton);

        panel.add(buttonPanel);

        add(panel);
    }
}