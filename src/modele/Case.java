/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

public abstract class Case extends Obj {

    protected Entite e;
    public abstract boolean peutEtreParcouru();


    // Cette fonction (a redéfinir) détermine le comportement (qui peut être complexe) lorsque l'entité entre dans la case
    public boolean entrerSurLaCase(Entite e) {

        //Case c = e.getCase();
        //if (c !=null) {
        //    c.quitterLaCase();
        //}

        setEntite(e);
        return true;
    }

    public void quitterLaCase() {
        e = null;
    }



    public Case(Jeu _jeu) {
        super(_jeu);
    }

    public Entite getEntite() {
        return e;
    }

    public void setEntite(Entite _e) {

        e = _e;
        e.setCase(this);}


   }
