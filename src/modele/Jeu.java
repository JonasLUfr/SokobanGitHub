/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;


import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;
import java.util.ArrayList;


public class Jeu extends Observable {

    //初始化游戏界面大小init
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;

    //Create Hero
    private Heros heros;

    // permet de récupérer la position d'une case à partir de sa référence
    private HashMap<Case, Point> map = new  HashMap<Case, Point>();
    // permet de récupérer une case à partir de ses coordonnées
    private Case[][] grilleEntites = new Case[SIZE_X][SIZE_Y];



    public Jeu() {
        initialisationNiveau();
    }


    
    public Case[][] getGrille() {
        return grilleEntites;
    }
    
    public Heros getHeros() {
        return heros;
    }

    public void deplacerHeros(Direction d) {
        heros.avancerDirectionChoisie(d);
        setChanged();
        notifyObservers();
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

        Random random = new Random();
        for (int i = 0; i < 20; i++) { // Add as many obstacles as necessary
            int x, y;
            do {
                x = random.nextInt(18) + 1; // Generate a random position between 1 and 18
                y = random.nextInt(8) + 1; // Generate a random position between 1 and 8
            } while ((x == 4 && y == 4) || (x == 6 && y == 6));
            System.out.println("Generated obstacle at: (" + x + ", " + y + ")");
            addCase(new Mur(this), x, y);
        }

        //addCase(new Ice(this), 10, 3);
        placeIceBlocks();
        heros = new Heros(this, grilleEntites[4][4]);
        Bloc b = new Bloc(this, grilleEntites[6][6]);
    }

    private void addCase(Case e, int x, int y) {
        grilleEntites[x][y] = e;
        map.put(e, new Point(x, y));
    }

    private void placeIceBlocks() {
        Random random = new Random();
        ArrayList<Point> murCoordinates = new ArrayList<>(); // 存储已生成墙壁的坐标

        // 将已生成的墙壁坐标添加到列表中
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                if (grilleEntites[x][y] instanceof Mur) {
                    murCoordinates.add(new Point(x, y));
                }
            }
        }

        int numIceBlocks = 5; // 需要生成的Ice数量
        for (int i = 0; i < numIceBlocks; i++) {
            // 生成随机坐标
            int x, y;
            do {
                x = random.nextInt(SIZE_X - 2) + 1; // 生成1到SIZE_X-2之间的随机数
                y = random.nextInt(SIZE_Y - 2) + 1; // 生成1到SIZE_Y-2之间的随机数
            } while (murCoordinates.contains(new Point(x, y))); // 如果生成的坐标已经是墙壁的坐标，则重新生成

            // 在游戏中添加Ice实体
            addCase(new Ice(this), x, y);
        }
    }


    /** Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     * Sinon, rien n'est fait.
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        boolean retour = true;
        
        Point pCourant = map.get(e.getCase());
        
        Point pCible = calculerPointCible(pCourant, d);

        if (contenuDansGrille(pCible)) {
            Entite eCible = caseALaPosition(pCible).getEntite();
            if (eCible != null) {
                eCible.pousser(d);
            }

            // si la case est libérée
            if (caseALaPosition(pCible).peutEtreParcouru()) {
                e.getCase().quitterLaCase();
                caseALaPosition(pCible).entrerSurLaCase(e);

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
