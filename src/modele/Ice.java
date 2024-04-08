package modele;

import java.awt.Point;
public class Ice extends Case {
    public Ice(Jeu _jeu) {
        super(_jeu);
    }

    // Override the method to specify whether ice can be traversed
    @Override
    public boolean peutEtreParcouru() {
        return e == null; // Ice can be traversed
    }

    //super
    public boolean entrerSurLaCase(Entite e) {

        //Case c = e.getCase();
        //if (c !=null) {
        //    c.quitterLaCase();
        //}

        setEntite(e);
        return true;
    }
    //Push method to push the entity an extra square when it moves onto ice
    //not need @Override
    //super，Appel d'un constructeur, d'une variable membre ou d'une méthode membre d'une classe mère

}
