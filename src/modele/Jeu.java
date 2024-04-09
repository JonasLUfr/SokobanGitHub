/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;


import VueControleur.VueControleur;

import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;
import java.util.ArrayList;
import modele.StartMenuView;



public class Jeu extends Observable {

    //init InterfaceG
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;



    //Create Hero
    private Heros heros;
    private Bloc bloc;

    private Porte porte;

    private Coin coin;
    private Ghost ghost;

    private int coinsEaten;


    // permet de récupérer la position d'une case à partir de sa référence
    public HashMap<Case, Point> map = new  HashMap<Case, Point>();
    // permet de récupérer une case à partir de ses coordonnées
    private Case[][] grilleEntites = new Case[SIZE_X][SIZE_Y];

    private ArrayList<Point> coinPositions = new ArrayList<>();
    private ArrayList<Point> ghostPositions = new ArrayList<>();

    //Stocke l'emplacement de chaque objet fantôme pour la suite Func possible(Futur)
    private void initGhostPositions() {
        ghostPositions = new ArrayList<>();
        for (Ghost ghost : getGhosts()) {
            Point position = map.get(ghost.getCase());
            ghostPositions.add(position);
        }
    }

    public Jeu() {
        initialisationNiveau();
        coinsEaten = 0;
    }
    public Case[][] getGrille() {
        return grilleEntites;
    }
    
    public Heros getHeros() {
        return heros;
    }
    public Bloc getBloc() {
        return bloc;
    }

    //public Ghost getGhost() { return ghost; }

    public void incrementCoinsEaten() {
        coinsEaten++;
    }

    public int getCoinsEaten() {
        return coinsEaten;
    }

    //Parcourir la grille du jeu, vérifier si les entités à chaque endroit sont des objets Ghost et les ajouter à une liste.
    public ArrayList<Ghost> getGhosts() {
        ArrayList<Ghost> ghosts = new ArrayList<>();
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                if (grilleEntites[x][y].getEntite() instanceof Ghost) {
                    ghosts.add((Ghost) grilleEntites[x][y].getEntite());
                }
            }
        }
        return ghosts;
    }
    public void deplacerHeros(Direction d) {
        Point heroPosition = map.get(heros.getCase());
        if (heroPosition != null) { // Vérifier si la position actuelle du héros est nulle
            heros.avancerDirectionChoisie(d);
            checkAndRemoveCoin();
            //checkPositionGhost();
            setChanged();
            notifyObservers();
        } else {
            System.err.println("Error: Hero position is null.");
        }
    }


    private void initialisationNiveau() {

        // murs extérieurs horizontaux
        for (int x = 0; x < 20; x++) {
            addCase(new Mur(this), x, 0);
            addCase(new Mur(this), x, 9);
        }

        // murs extérieurs verticaux
        for (int y = 1; y < 9; y++) {
            addCase(new Mur(this), 0, y);
            addCase(new Mur(this), 19, y);
        }

        for (int x = 1; x < 19; x++) {
            for (int y = 1; y < 9; y++) {
                addCase(new Vide(this), x, y);
            }

        }
/*
        Random random = new Random();
        for (int i = 0; i < 25; i++) { // Add as many obstacles as necessary
            int x, y;
            do {
                x = random.nextInt(18) + 1; // Generate a random position between 1 and 18
                y = random.nextInt(8) + 1; // Generate a random position between 1 and 8
            } while ((x == 4 && y == 4) || (x == 6 && y == 6));
            System.out.println("Generated obstacle at: (" + x + ", " + y + ")");
            addCase(new Mur(this), x, y);
        }
*/

        // Ajouter des obstacles aux positions fixes
        addCase(new Mur(this), 2, 2);
        addCase(new Mur(this), 8, 3);
        addCase(new Mur(this), 14, 5);
        addCase(new Mur(this), 10, 7);
        addCase(new Mur(this), 5, 4);
        addCase(new Mur(this), 12, 2);
        addCase(new Mur(this), 18, 4);
        addCase(new Mur(this), 3, 6);
        addCase(new Mur(this), 17, 6);
        addCase(new Mur(this), 15, 3);
        addCase(new Mur(this), 15, 3);
        //addCase(new Ice(this), 10, 3);
        placeIceBlocks();
        heros = new Heros(this, grilleEntites[4][4]);
        bloc = new Bloc(this, grilleEntites[6][6]);

        ghost = new Ghost(this, grilleEntites[11][8]);
        ghost = new Ghost(this, grilleEntites[15][8]);
        ghost = new Ghost(this, grilleEntites[11][4]);
        ghost = new Ghost(this, grilleEntites[15][4]);

        // mettre la porte au bon endroit
        porte = new Porte(this);
        addCase(new Porte(this), SIZE_X - 2, SIZE_Y - 2);


        addCoin(4, 2);
        addCoin(13, 7);
        addCoin(8, 8);
        addCoin(18, 3);
    }
    private void addCoin(int x, int y) {
        coinPositions.add(new Point(x, y));
        addCase(new Coin(this), x, y);
    }

    private void addCase(Case e, int x, int y) {
        grilleEntites[x][y] = e;
        map.put(e, new Point(x, y));
    }

    public void checkAndRemoveCoin() {
        Point heroPosition = map.get(heros.getCase());

        for (int i = 0; i < coinPositions.size(); i++) {
            Point coinPosition = coinPositions.get(i);
            if (heroPosition.equals(coinPosition)) {
                // Si les positions du héros et de coin se chevauchent, retirez la pièce d'or.
                coinPositions.remove(i);
                //grilleEntites[coinPosition.x][coinPosition.y] = new Vide(this); //La position de coin à vide sur la carte est incorrecte et une erreur est signalée lorsque l'on revient à cette coordonnée.
                addCase(new Vide(this), coinPosition.x, coinPosition.y); // Doit placer l'emplacement de coin sur la carte dans un nouveau cas vide.
                incrementCoinsEaten();
                break; // Une fois le coin trouvée et supprimée, quittez la boucle.
            }
        }
    }

    private void placeIceBlocks() {
        Random random = new Random();
        ArrayList<Point> OccupeCoordinates = new ArrayList<>(); // Utilisé pour stocker les coordonnées des murs générés

        // Ajout à la liste des coordonnées des murs déja générés
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                if (grilleEntites[x][y] instanceof Mur) {
                    OccupeCoordinates.add(new Point(x, y));
                }
            }
        }

        // Ajouter aussi les Coordinates de la porte à la liste
        Point doorPosition = map.get(porte);
        OccupeCoordinates.add(doorPosition);

        for (Point coinPosition : coinPositions) {
            OccupeCoordinates.add(coinPosition);
        }

        int numIceBlocks = 8; // Nombre de glaces à générer
        for (int i = 0; i < numIceBlocks; i++) {
            // Générer des coordonnées aléatoires
            int x, y;
            do {
                x = random.nextInt(SIZE_X - 2) + 1; // Générer des nombres aléatoires entre 1 et SIZE_X-2
                y = random.nextInt(SIZE_Y - 2) + 1; // Générer des nombres aléatoires entre 1 et SIZE_Y-2
            } while (OccupeCoordinates.contains(new Point(x, y))); // Si les coordonnées générées sont déjà les coordonnées du mur, régénérer les.

            // Ajout d'entités de glace au jeu
            addCase(new Ice(this), x, y);
        }
    }
    private void afficherVictoire(int coinsEaten) {
        // Création et affichage des dialogues de victoire
        Victoire victoireDialog = new Victoire(null, coinsEaten);
        victoireDialog.setVisible(true);
    }

    private void afficherEchouer() {
        // Création et affichage de dialogues d'échec
        Echouer EchouerDialog = new Echouer(null);
        EchouerDialog.setVisible(true);
    }
    public boolean deplacerEntite(Entite e, Direction d) {
        boolean retour = true;
        Point pCourant = map.get(e.getCase());
        
        Point pCible = calculerPointCible(pCourant, d);

        // Sauvegarder la position actuelle comme position précédente
        if (e instanceof Heros) {
            ((Heros) e).setPointPre(pCourant);
            System.out.println("Save pCourant Hero at: " + pCourant);
        }

        if (e instanceof Bloc) {
            ((Bloc) e).setPointPre(pCourant);
            ((Bloc) e).setPpreDirection(d);  //注意此时方向是正常的方向，需要再Bloc里将方向变反从而实现Undo返回
            System.out.println("Save pCourant Bloc at: " + pCourant);
        }

        if (contenuDansGrille(pCible)) {
            Entite eCible = caseALaPosition(pCible).getEntite();
            if (eCible != null) {
                eCible.pousser(d);
            }

            // Si la case cible est occupée par un Ghost et que l'entité est un Heros, afficher l'échec
            if (e instanceof Heros && eCible instanceof Ghost) {
                afficherEchouer();
                System.out.println("NO! CEST GHOST!!"); //Debug
                return false; // Retourner false pour indiquer que le déplacement n'est pas autorisé
            }

            // si la case est libérée
            if (caseALaPosition(pCible).peutEtreParcouru()) {
                e.getCase().quitterLaCase();
                caseALaPosition(pCible).entrerSurLaCase(e);
                //caseALaPosition(pCible).getEntite().allerSurCase(e);


                // Vérifier si la position cible est une porte
                if (e instanceof Bloc && caseALaPosition(pCible) instanceof Porte) {
                    // La logique qui déclenche un gain de jeu
                    System.out.println("Congratulations! You have won the game!");  //Debug
                    int coinsEaten = 4 - getCoinsEaten();
                    afficherVictoire(coinsEaten);
                }

                // Vérifier si la position cible est Ice---Partie Modifier
                if (caseALaPosition(pCible) instanceof Ice) {
                    // Déplacez le Bloc ou Hero d'une case supplémentaire dans la même direction.
                    Point pNext = calculerPointCible(pCible, d);
                    if (contenuDansGrille(pNext) && caseALaPosition(pNext).peutEtreParcouru()) {
                        retour = deplacerEntite(e, d);
                        // 将当前位置保存为上一个位置
                    } else {
                        retour = false; // Cannot move the Bloc one more square
                    }
                }
            } else {
                retour = false;
            }

        } else {
            retour = false;
        }

        return retour;
    }

    public void moveGhost(Entite e) {
        // Obtenir la position actuelle de Ghost
        Point currentPos = map.get(e.getCase());

        // Obtenir la direction dans laquelle Ghost peut se déplacer
        ArrayList<Direction> possibleDirections = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Point nextPos = calculerPointCible(currentPos, direction);
            if (contenuDansGrille(nextPos)) {
                Case nextCase = caseALaPosition(nextPos);
                if (nextCase.peutEtreParcouru() && !(nextCase instanceof Porte) && !(nextCase.getEntite() instanceof Bloc) ) {
                    //Vérifier le contenu de l'entité ou de la grille suivante
                    possibleDirections.add(direction);
                }
            }
        }

        // S'il existe une direction réalisable, choisir au hasard une direction pour déplacer Ghost.
        if (!possibleDirections.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(possibleDirections.size());
            Direction randomDirection = possibleDirections.get(randomIndex);
            deplacerEntite(e, randomDirection);
        }
    }

    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;
        
        switch(d) {
            case haut: pCible = new Point(pCourant.x, pCourant.y - 1); break;
            case bas : pCible = new Point(pCourant.x, pCourant.y + 1); break;
            case gauche : pCible = new Point(pCourant.x - 1, pCourant.y); break;
            case droite : pCible = new Point(pCourant.x + 1, pCourant.y); break;     
            
        }
        
        return pCible;
    }
    
    /** Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    public Case caseALaPosition(Point p) {
        Case retour = null;
        
        if (contenuDansGrille(p)) {
            retour = grilleEntites[p.x][p.y];
        }
        
        return retour;
    }

}


