package IHM;

import metier.BoisExperimental;
import metier.BoisTheorique;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * Classe défininissant le panneau des graphiques au sein du panneau scientifique
 * @author Loïc BELLEC & Valentin EBERHARDT
 */
public class PanneauGraphique extends JPanel {
    
    private final int nbSecondes = 7200;
    private final int ecart = 50; // écart avec le bord des autres composants pour une garantir une interface aérée et agréable à utiliser
    private final int epaisseurSelectionnee;  // épaisseur sélectionnée par l'utilisateur
    private final int[] listeEpaisseurs = new int[41]; // 40 épaisseurs + face e=0mm
    private final Color BGCOLOR = new Color(225, 230, 235);  // couleur de fond du graphique
    private final String abscisse = "Temps (s)", ordonnee = "Température (°C)";  // noms des axes des abscisses et ordonnées
    private final boolean epaisseursAffichees[] = new boolean[41];  // tableau de booléens : afficher ou pas chacune des 41 épaisseurs
    private final double coordsGraphiqueTh[][][] = new double[7200][41][2];  // coordonnes théoriques pour tracer T(t)
    private final double coordsGraphiqueExp[][][] = new double[7200][41][2];  // coordonnées expérimentales pour tracer T(t)
    
    private final BoisExperimental boisExp;  // objet bois expérimental
    private final BoisTheorique boisTh;  // objet bois théorique
    
    private int indiceEpaisseurSelectionnee = -1; // par défaut, aucune épaisseur n'est affichée : on attribue la valeur arbitraire e=-1mm
    private Dimension taillePanneau = this.getSize();  // dimension du panneau graphique
    public boolean afficherExp = false;  // au temps initial on n' affiche pas les courebs expérimentales
    public boolean afficherTh = false;  // au temps initial, on n'affiche pas les courbes théoriques
    private boolean coordsCalculees = false;  // au temps initial, les coordonnées du graphique n'ont pas été calculées
    public int epaisseurIntermediaire;
    public boolean afficherEpaisseurIntermediaire;
    
    /**
     * Constructeur de la classe PanneauGraphique
     * @param essence String : essence de bois à afficher
     * @param valeurChampEpaisseur int : valeur de l'épaisseur saisie par l'utilisateur
     */
    public PanneauGraphique(String essence, int valeurChampEpaisseur) {       
        this.epaisseurSelectionnee = valeurChampEpaisseur;  // assignation de la valeur de la saisie à l'attribut
        this.boisExp = new BoisExperimental(essence);  // initialisation de l'objet BoisExpérimental
        this.boisTh = new BoisTheorique(essence);  // initialisation de l'objet BoisThéorique
        
        for (int e=0;e<41;e++) {  // remplir le tableau d'indices des épaisseurs
            this.listeEpaisseurs[e] = e;
        }

        this.setBackground(BGCOLOR);  // définition d'une couleur de fond pour le graphique
        this.setBorder(new LineBorder(Color.black, 3));  // ajout d'une bordure au graphique
    }  // fin constructeur PanneauGraphique à un seul paramètre
    
    /**
     * DEFINITION DES SETTERS
     */
    
    /**
     * Setter épaisseur
     * @param e int : épaisseur
     * @param estAffiche boolean : autorisation ou interdiction de dessiner le graphe associé à l'épaisseur e
     */
    public void setEpaisseur(int e, boolean estAffiche) {
        this.indiceEpaisseurSelectionnee = e;  // assignation de l'épaisseur sélectionnée
        int indiceEpaisseur = 0;
        for (int epaisseur : this.listeEpaisseurs) {  // parcours des épaisseurs de e=2mm à e=40mm
            if (epaisseur == e) {
                this.epaisseursAffichees[indiceEpaisseur] = estAffiche; // le graphique est à afficher
            }
            else {
                indiceEpaisseur++;
            }
        }
        this.repaint();  // redessiner les graphiques
    }  // fin setEpaisseur
    
    /**
     * Setter afficher les valeurs expérimentales
     * @param affiche boolean
     */
    public void setValeursExperimentales(boolean affiche) {
        this.afficherExp = affiche;
        this.repaint();  // mise à jour de l'affciahge des graphiques
    }  // fin setValeursExperimantales
    
    /**
     * Setter affiches les valeurs théoriques
     * @param affiche 
     */
    public void setValeursTheoriques(boolean affiche) {
        this.afficherTh = affiche;
        this.repaint();  // mise à jour de l'affichage des graphiques
    }  // fin setValeursTheoriques
    
    /**
     * FIN DES SETTERS
     */

    /**
     * Dessins des axes et des graphes
     * @param g Graphics : composant graphique
     */
    @Override
    protected void paintComponent(Graphics g) {
               
        if (this.boisExp.temperaturesBois[0][0] == -1 || this.boisTh.temperaturesBoisGlobal[0][0] == -1) {  // se prémunir d'erreur de chemins de fichiers
            String cheminInexistant;
            if (this.boisExp.temperaturesBois[0][0] == -1) {
                cheminInexistant = this.boisExp.cheminFichier;
            }
            else {
                cheminInexistant = this.boisTh.cheminFichier;
            }
            g.setColor(Color.red);
            g.drawString("ERREUR PROGRAMME. Le chemin de fichier renseigné est erroné.", this.getWidth()/4, this.getHeight()/2);
            g.drawString(cheminInexistant + " n'existe pas." , this.getWidth()/4, 3*this.getHeight()/4);
            g.setColor(Color.black);
            return;
        }
        
        super.paintComponent(g);
        Dimension taillePanneauCourante = this.getSize();  // récupérer la taille actuelle de la fenêtre
        
        if (!this.coordsCalculees || (this.taillePanneau != taillePanneauCourante)) {  // si les coordonnées des T(t) n'ont pas encore été calculées ou si un redimensionnement de la fenêtre a été effectué
            this.calculerCoordonneesGraphiques();  // calculer les coordonnees si pas déjà fait / recalculer les coordonnées si resize de la fenêtre
            this.coordsCalculees = true;  // mise à jour de l'attribut
            this.taillePanneau = taillePanneauCourante;  // mise à jour de la taille du panneau graphique
        }
        this.dessinerAxes(g);  // tracer les axes des abscisses et des ordonnées
        this.dessinerGraphique(g);  // tracer les graphiques pour toutes les épaisseurs comprises entre 2 et 40mm
    }  // fin paintComponent
    
    /**
     * Calculer les coordonnées des points expérimentaux et théoriques
     */
    private void calculerCoordonneesGraphiques() {
        final int NVALMAXABSCISSE = this.getWidth() - this.ecart;  // nombres de valeurs pouvant être afficher (dépend de la taille de la fenêtre)
        final double VALMAXY = this.boisExp.maxTempGlobal;  // définir la valeur maximale de l'axe des ordonnées

        int differenceAbscisses = 1;  // par défaut la distance entre deux abscisses est de 1
        int x = this.ecart;
        int indiceTemps = 0;  // indice pour remplir le tableau des coordonées expérimentales

        double tempPrecTh, tempSuivTh, yPrecTh, ySuivTh;  // variables théoriques
        double tempPrecExp, tempSuivExp, yPrecExp, ySuivExp;  // variables expérimentales
        
        for (int e=0;e<41;e++) {  // initialisation des deux tableaux de coordonnées
            for (int t=0;t<this.nbSecondes;t++) {
                this.coordsGraphiqueExp[t][e][0] = 0.0;
                this.coordsGraphiqueExp[t][e][1] = 0.0;
                this.coordsGraphiqueTh[t][e][0] = 0.0;
                this.coordsGraphiqueTh[t][e][1] = 0.0;
            }
        }

        for (int indiceEpaisseur=0; indiceEpaisseur<41;indiceEpaisseur++) {

            tempPrecExp = this.boisExp.temperaturesBoisComplet[0][indiceEpaisseur];  // température initiale à une épaisseur donnée pour le bois expérimental
            tempPrecTh = this.boisTh.temperaturesBoisGlobal[0][indiceEpaisseur] - 273;  // température initiale à une épaisseur donnée pour le bois théorique        
            
            for (int i=1;i<7200;i+= (int) this.nbSecondes / NVALMAXABSCISSE) {  // parcours de toutes les lignes à partir de 1 pour enelver l'entête des fichiers comportant les températures

                tempSuivExp = this.boisExp.temperaturesBoisComplet[i][indiceEpaisseur];
                tempSuivTh = this.boisTh.temperaturesBoisGlobal[i][indiceEpaisseur] - 273;  // conversion en degrés Celsius

                if (x < NVALMAXABSCISSE) { // si l'affichage est possible

                    /* CALCUL COORDONNEES DES VALEURS EXPERIMENTALES */
                    yPrecExp = (this.getHeight() - this.ecart) - (this.getHeight() - 2 * this.ecart) * (tempPrecExp / VALMAXY);
                    ySuivExp = (this.getHeight() - this.ecart) - (this.getHeight() - 2 * this.ecart) * (tempSuivExp / VALMAXY);
                    this.coordsGraphiqueExp[indiceTemps][indiceEpaisseur][0] = yPrecExp;
                    this.coordsGraphiqueExp[indiceTemps][indiceEpaisseur][1] = ySuivExp;

                    /* CALCUL COORDONNEES DES VALEURS THEORIQUES */
                    yPrecTh = (this.getHeight() - this.ecart) - (this.getHeight() - 2 * this.ecart) * (tempPrecTh / VALMAXY);
                    ySuivTh = (this.getHeight() - this.ecart) - (this.getHeight() - 2 * this.ecart) * (tempSuivTh / VALMAXY);
                    this.coordsGraphiqueTh[indiceTemps][indiceEpaisseur][0] = yPrecTh;
                    this.coordsGraphiqueTh[indiceTemps][indiceEpaisseur][1] = ySuivTh;

                    if (indiceTemps < 7199) {
                        indiceTemps++; // incrémentation de l'indice temporel
                    }

                    differenceAbscisses = 1;  // réinitialisation
                    x++;  // incrémentation de l'abscisse
                    tempPrecExp = tempSuivExp;  // passer à la température suivante pour le bois expérimental
                    tempPrecTh = tempSuivTh;  // passer à la température suivante pour le bois théorique
                }
                else {
                    differenceAbscisses++;  // incrémentation de l'écart entre deux points affichés
                }
            }
            indiceTemps = 0;  // réinitialisation pour l'épaisseur suivante
            x = ecart;  // réinitialisation pour l'épaisseur suivante
            differenceAbscisses = 1;  // réinitialisation pour l'épaisseur suivante
        }   
    }  // fin calculerCoordonneesGraphiques
    
    /**
     * Dessiner les graphiques T(t)
     * @param g Graphics : composant graphique
     */
    private void dessinerGraphique(Graphics g) {
        boolean valMinYExp = true;  // par défaut, afficher la graduation minimale expérimentale
        boolean valMinYTh = true;  // par défaut, afficher la graduation minimale théorique
        int x = this.ecart;  // commencer à dessiner à partir de la marge imposée
        final double VALMAXY = this.boisExp.maxTempGlobal;  // définir la valeur maximale de l'axe des ordonnées
        double yPrecTh, ySuivTh, yPrecExp, ySuivExp;

        for (int indiceEpaisseur=0;indiceEpaisseur<41;indiceEpaisseur++) {

            if (this.epaisseursAffichees[indiceEpaisseur]) {  // si l'épaisseur est à afficher

                for (int indiceTemps=0;indiceTemps<this.nbSecondes;indiceTemps++) {
                    if (this.afficherTh) {  // afficher les valeurs théoriques
                        
                        this.graduerAxesTemperature(g, indiceEpaisseur, VALMAXY, valMinYTh, false);  // graduer les axes pour l'épaisseur d'indice indiceEpaisseur
                        valMinYTh = false;

                        g.setColor(new Color(
                            this.boisExp.couleursEpaisseur[indiceEpaisseur].getRed(), 
                            this.boisExp.couleursEpaisseur[indiceEpaisseur].getGreen(), 
                            this.boisExp.couleursEpaisseur[indiceEpaisseur].getBlue(), 
                            80));  // diminuer l'opacité par  rapport aux valeurs théoriques

                        yPrecTh = this.coordsGraphiqueTh[indiceTemps][indiceEpaisseur][0];
                        ySuivTh = this.coordsGraphiqueTh[indiceTemps][indiceEpaisseur][1];

                        g.drawLine(x - 1, (int) yPrecTh, x, (int) ySuivTh);
                        if (!this.afficherExp) {  // si seules valeurs théoriques sont à afficher
                            x++;
                        }
                    }

                    if (this.afficherExp) {  // afficher les valeurs expérimentales
                        
                        g.setColor(this.boisExp.couleursEpaisseur[indiceEpaisseur]);
                        
                        this.graduerAxesTemperature(g, indiceEpaisseur, VALMAXY, valMinYExp, true);  // graduer les axes pour l'épaisseur d'indice indiceEpaisseur
                        valMinYExp = false;
                        
                        yPrecExp = this.coordsGraphiqueExp[indiceTemps][indiceEpaisseur][0];
                        ySuivExp = this.coordsGraphiqueExp[indiceTemps][indiceEpaisseur][1];

                        g.drawLine(x - 1, (int) yPrecExp, x, (int) ySuivExp);
                        x++;
                    }
                    
                    if (this.afficherEpaisseurIntermediaire) {
                        g.drawString("Attention, les valeurs affichées pour l'épaisseur " + this.epaisseurIntermediaire + " mm sont une approximation.", 50, this.getHeight() - 10);
                    }
                }
            }
            x = this.ecart;  // réinitialisation de l'abscisse pour tracer l'épaisseur suivante
        }
        g.setColor(Color.BLACK);  // réinitialisation de la couleur par défaut
    }  // fin dessinerGraphique
    
        /**
     * Dessiner les axes de la section comportants les graphes
     * @param g Graphics : composant graphique
     */
    private void dessinerAxes(Graphics g) {
        /* TRACER AXE ABSCISSES */
        g.drawLine(this.ecart, this.getHeight() - this.ecart, this.getWidth() - this.ecart, this.getHeight() - this.ecart); // abscisses
        g.drawLine(this.getWidth() - this.ecart, this.getHeight() - this.ecart, this.getWidth() - this.ecart/2, this.getHeight() - this.ecart);  // ajotuer supplément de ligne pour la tête de flèche
        dessinerTeteFleche(g, this.getWidth() - this.ecart, this.getHeight() - this.ecart, this.getWidth() - this.ecart/2, this.getHeight() - this.ecart);  // ajouter tête flèche axe des abscisses
        g.drawString(this.abscisse, this.getWidth() - 7 * ecart/5, this.getHeight() - 3 * ecart / 2);  // afficher le nom de l'axe des abscisses
        
        /* TRACER AXE ORDONNEES */
        g.drawLine(this.ecart, this.getHeight() - this.ecart, this.ecart, this.ecart); // ordonnées
        g.drawLine(this.ecart, this.ecart, this.ecart, this.ecart / 2);  // ajotuer supplément de ligne pour la tête de flèche
        dessinerTeteFleche(g, this.ecart, this.ecart, this.ecart, this.ecart/2);  // ajouter tête flèche axe
        g.drawString(this.ordonnee, 3*this.ecart/2, this.ecart/2);  // afficher le nom de l'axe des ordonnées

        if (this.indiceEpaisseurSelectionnee != -1) {  // si au moins une épaisseur a été sélectionnée
            g.drawString("Sélection : e = " + this.epaisseurSelectionnee + "mm", this.getWidth() - 3 * ecart, ecart/2);  // afficher l'épaisseur dernièrement sélectionnée
        }
    }  // fin dessinerAxes
    
    /**
     * Tracer la tête de flèche des axes du graphique
     * @param g Graphics : composant graphique
     * @param x1 int : abscisse de l'origine de l'axe
     * @param y1 int : ordonnée de l'origine de l'axe
     * @param x2 int : abscisse de l'extrémité de l'axe
     * @param y2 int : ordonnée de l'extrémité de l'axe
     */
    private static void dessinerTeteFleche(Graphics g, int x1, int y1, int x2,
            int y2) {

        Polygon p = new Polygon();
        double direction = Math.atan2(x1 - x2, y1 - y2);
        int taille = 16;  // taille de la tête de flèche

        p.addPoint(x2, y2);
        p.addPoint(x2 + (int)(taille * Math.sin(direction + .5)), y2 + (int)(taille * Math.cos(direction + .5)));
        p.addPoint(x2 + (int)(taille * Math.sin(direction - .5)), y2 + (int)(taille * Math.cos(direction - .5)));

        g.fillPolygon(p);  // remplir la tête de la flèche
    }  // fin dessinerTeteFleche
    
    /**
     * Dessiner les graduations minimales et maximales d'une caractéristique sur l'axe des abscisses
     * @param g Graphics : composant graphique où dessiner
     * @param minX double : valeur min abscisses
     * @param maxX double : valeur max abscisses
     */
    private void dessinerGraduationsX(Graphics g, double minX, double maxX) {
        int longueurGaucheGraduation = 5 * this.ecart / 6;
        int longueurDroiteGraduation = 7 * this.ecart / 6;
        double xMin, xMax;
        
        // graduation minimale X
        xMin = this.ecart + (this.getWidth() - 2 * this.ecart) * (minX / maxX);
        g.drawLine((int)xMin, this.getHeight() - longueurGaucheGraduation, (int)xMin, this.getHeight() - longueurDroiteGraduation);
        g.drawString(Integer.toString((int)minX), (int)xMin - 4, this.getHeight() - this.ecart / 2);
        
        // graduation maximale X
        xMax = this.getWidth() - this.ecart;
        g.drawLine((int)xMax, this.getHeight() - longueurGaucheGraduation, (int)xMax, this.getHeight() - longueurDroiteGraduation);
        g.drawString(Integer.toString((int)maxX), (int)xMax - 4, this.getHeight() - this.ecart / 2);
    }  // fin dessinerGraduationsX
    
    /**
     * Dessiner les graduations minimales et maximales d'une caractéristique sur l'axe des ordonnées
     * @param g Graphics : composant graphique où dessiner
     * @param minY double : valeur min ordonnees
     * @param //maxY double : valeur max ordonnees
     */
    private void dessinerGraduationsY(Graphics g, double minY, double maxYLocal, double maxYGlobal, boolean afficherMin, boolean afficherMax) {
        int longueurGaucheGraduation = 5 * this.ecart / 6;
        int longueurDroiteGraduation = 7 * this.ecart / 6;
        double yMin, yMax;
        
        // graduation minimale Y
        if (afficherMin) {
            yMin = this.getHeight() - this.ecart - (this.getHeight() - 2 * this.ecart) * (minY / maxYGlobal);
            g.drawLine(longueurGaucheGraduation, (int)yMin, longueurDroiteGraduation, (int)yMin);
            g.drawString(Integer.toString((int)minY), this.ecart / 4, (int)yMin + 4);
        }
        
        // graduation maximale Y
        if (afficherMax) {
            yMax = this.getHeight() - this.ecart - (this.getHeight() - 2 * this.ecart) * (maxYLocal / maxYGlobal);
            g.drawLine(longueurGaucheGraduation, (int)yMax, longueurDroiteGraduation, (int)yMax);
            g.drawString(Integer.toString((int)maxYLocal), this.ecart / 4, (int)yMax + 4);
        }
    }  // fin dessinerGraduationsY
    
    /**
     * Graduer localement les axes
     * @param g Graphics : composant graphique
     * @param indiceEpaisseur : int indice de l'épaisseur pour laquelle il faut graduer les axes
     * @param maxY double : valeur maximale sur l'axe des ordonnées
     * @param valEXp boolean : booléen pour savoir sur quelles valeurs on fixe les graduations de l'axe des ordonnées
     */
    private void graduerAxesTemperature(Graphics g, int indiceEpaisseur, 
        double maxY, boolean afficherYMin, boolean gradExp) {
        double yMinLocal, yMaxLocal;

        if (gradExp) { // dessiner les graduations pour les valeurs expérimentales
            yMinLocal = this.boisExp.bornesTemperaturesEpaisseurs[indiceEpaisseur][0];
            yMaxLocal = this.boisExp.bornesTemperaturesEpaisseurs[indiceEpaisseur][1];
        }
        else {  // dessiner les graduations pour les valeurs théoriques
            yMinLocal = this.boisTh.bornesTemperaturesEpaisseurs[indiceEpaisseur][0];
            yMaxLocal = this.boisTh.bornesTemperaturesEpaisseurs[indiceEpaisseur][1];
        }
        this.dessinerGraduationsY(g, yMinLocal, yMaxLocal, maxY, afficherYMin, true);
        this.dessinerGraduationsX(g, 0, 7200);
    }  // fin graduerAxesTemperature
} // fin de la classe PanneauGraphique
