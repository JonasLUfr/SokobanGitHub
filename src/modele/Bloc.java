package modele;

import java.awt.*;

public class Bloc extends Entite {

    private Point ppreCible;
    private Direction ppreDirection;
    public void setPointPre(Point point) {
        this.ppreCible = point;
    }
    public void setPpreDirection(Direction d){
        // 保存方向信息时进行逆转！
        switch (d) {
            case gauche:
                this.ppreDirection = Direction.droite;
                break;
            case droite:
                this.ppreDirection = Direction.gauche;
                break;
            case haut:
                this.ppreDirection = Direction.bas;
                break;
            case bas:
                this.ppreDirection = Direction.haut;
                break;
        }
    }
    public Bloc(Jeu _jeu, Case c) {
        super(_jeu, c);
    }

    public boolean pousser(Direction d) {
        return jeu.deplacerEntite(this, d);
    }

    //Pour Undo Bloc
    public void retourner() {
        // 获取当前位置的坐标
        Point caseCouranteCoord = jeu.map.get(this.getCase());
        // 获取上一个位置的 Case 对象
        Case casePrecedente = jeu.caseALaPosition(this.ppreCible);
        if (casePrecedente != null) {
            // 将当前位置的Bloc对象移动到上一个位置
            jeu.deplacerEntite(this, ppreDirection);
            // 更新上一个位置的坐标为当前位置
            setPointPre(caseCouranteCoord);
        }
    }
}
