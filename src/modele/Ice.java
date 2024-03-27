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
    //super调用父类的构造方法、成员变量或成员方法,但是case里没有这个push，所以无需继承

}
