package modele;



public class Coin extends Case {
    public Coin(Jeu _jeu) {
        super(_jeu);
    }

    @Override
    public boolean peutEtreParcouru() {
        return true;
    }


}