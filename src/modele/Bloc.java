package modele;

public class Bloc extends Entite {


    public Bloc(Jeu _jeu, Case c) {
        super(_jeu, c);
    }

    public boolean pousser(Direction d) {
        return jeu.deplacerEntite(this, d);
    }

}
