package modele;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Ghost extends Entite {
    private Case currentCase;
    public Ghost(Jeu _jeu, Case _c) {
        super(_jeu, _c);
        currentCase = _c;
    }

    public boolean entrerSurLaCase(Entite e) {
        currentCase.setEntite(e);
        return true;
    }

    public void quitterLaCase() {
        currentCase.setEntite(null);
    }

    @Override
    public boolean peutEtreParcouru() {
        return true;
    }
    @Override
    public boolean pousser(Direction direction) {
        // Ghost 不会被推动
        return false;
    }
}
