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
        //Inversé lorsque les informations sur la direction sont sauvegardées !
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
        //Point caseCouranteCoord = jeu.map.get(this.getCase());
        // Obtenir l'objet Case de la position précédente
        Case casePrecedente = jeu.caseALaPosition(this.ppreCible);
        if (casePrecedente != null) {
            // Déplacer l'objet Bloc à la position actuelle vers la position précédente
            jeu.deplacerEntite(this, ppreDirection);
            // Mise à jour des coordonnées de la position précédente à la position actuelle
            setPointPre(ppreCible);
        }
    }
}
