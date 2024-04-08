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

    public void retourner(Direction d) {
        // 获取当前位置的坐标
        //Point caseCouranteCoord = jeu.map.get(this.getCase());
        // Obtenir l'objet Case de la position précédente
        Case casePrecedente = jeu.caseALaPosition(this.ppreCible);
        if (casePrecedente != null) {
            // Déplacer l'objet Bloc à la position actuelle vers la position précédente
            jeu.deplacerEntite(this, d);
            //  Mise à jour des coordonnées de la position précédente à la position actuelle
            //setPointPre(caseCouranteCoord);
            setPointPre(ppreCible);
        }
    }

}
