package modele;

/**
 * Niveau le plus abstrait des objets manipulés dans le modèle (attention, il ne s'agit pas de la classe Objet ...))
 */
public abstract class Obj {
    protected Jeu jeu;
    public Obj(Jeu _jeu) {
        jeu = _jeu;
    }
}
