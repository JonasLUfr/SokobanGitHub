package modele;

public class Mur extends Case {
    public Mur(Jeu _jeu) {
        super(_jeu);
    }

    @Override
    public boolean peutEtreParcouru() {
        return false;
    }
}
