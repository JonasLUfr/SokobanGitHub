/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 * Héros du jeu
 */

import java.awt.*;


public class Heros extends Entite
{
    private Point ppreCible;
    public void setPointPre(Point point) {
       this.ppreCible = point;
    }
    public Heros(Jeu _jeu, Case c) {
        super(_jeu, c);
    }
    // 返回英雄到上一个位置
    // 返回英雄到上一个位置
    public void retourner(Direction d) {
        // 获取当前位置的坐标
        Point caseCouranteCoord = jeu.map.get(this.getCase());
        // 获取上一个位置的 Case 对象
        Case casePrecedente = jeu.caseALaPosition(this.ppreCible);
        if (casePrecedente != null) {
            // 将当前位置的英雄对象移动到上一个位置
            jeu.deplacerEntite(this, d);
            // 更新上一个位置的坐标为当前位置
            setPointPre(caseCouranteCoord);
        }
    }

}
