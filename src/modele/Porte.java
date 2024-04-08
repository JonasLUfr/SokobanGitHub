package modele;



public class Porte extends Case {
    public Porte(Jeu _jeu) {
        super(_jeu);
    }

    @Override
    public boolean peutEtreParcouru() {
        return true;
    }


}