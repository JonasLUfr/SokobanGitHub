package modele;

public class Vide extends Case {

    public Vide(Jeu _jeu) { super(_jeu); }

    @Override  //class Child extends Parent，当再Case父类中的
    public boolean peutEtreParcouru() {
        return e == null;
    }
}
