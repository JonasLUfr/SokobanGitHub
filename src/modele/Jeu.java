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

    //初始化游戏界面大小init
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;



    //Create Hero
    private Heros heros;
    private Bloc bloc;

    private Porte porte;

    private Coin coin;


    // permet de récupérer la position d'une case à partir de sa référence
    public HashMap<Case, Point> map = new  HashMap<Case, Point>();
    // permet de récupérer une case à partir de ses coordonnées
    private Case[][] grilleEntites = new Case[SIZE_X][SIZE_Y];

    private ArrayList<Point> coinPositions = new ArrayList<>();





    public Jeu() {
        initialisationNiveau();
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

    public void deplacerHeros(Direction d) {
        Point heroPosition = map.get(heros.getCase());
        //if (heroPosition != null) { // 检查英雄的当前位置是否为null
            heros.avancerDirectionChoisie(d);
            checkAndRemoveCoin();
            setChanged();
            notifyObservers();
        //} else {
         ///   System.err.println("Error: Hero position is null.");
        //}
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
        // 将门放置在右下角
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
    private void afficherVictoire() {
        // 创建并显示胜利对话框
        Victoire victoireDialog = new Victoire(null);
        victoireDialog.setVisible(true);
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

            // si la case est libérée
            if (caseALaPosition(pCible).peutEtreParcouru()) {
                e.getCase().quitterLaCase();
                caseALaPosition(pCible).entrerSurLaCase(e);

                // Vérifier si la position cible est une porte
                if (e instanceof Bloc && caseALaPosition(pCible) instanceof Porte) {
                    // La logique qui déclenche un gain de jeu
                    System.out.println("Congratulations! You have won the game!");
                    afficherVictoire();
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


