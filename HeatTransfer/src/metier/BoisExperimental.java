package metier;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Classe défininissant une essence de bois à l'aide des données expérimentales
 * @author Loïc BELLEC & Valentin EBERHARDT
 */
public class BoisExperimental {
    public final String cheminFichier;
    public double minTempGlobal;  // température minimale sur l'ensemble des épaisseurs et des temps
    public double maxTempGlobal;  // température maximale sur l'ensemble des épaisseurs et des temps
    public double[][] bornesTemperaturesEpaisseurs = new double[41][2];  // 12 épaisseurs pour deux températures extrêmes
    public double[][] temperaturesBois;  // matrice des températures du bois pour 12 épaissseurs
    public double[][] temperaturesBoisComplet = new double[41][2];  // matrice des températures du bois pour 40 épaisseurs + la face e=0mm
    public Color[] couleursEpaisseur = new Color[41];  // tableaux contenant les couleurs des épaissseurs
    public int nbLignes = 0;  // par défaut, le nombre de lignes du fichier relatif à l'essence de bois est nul
    
    /**
     * Constructeur à un paramètre
     * @param essence String : bois étudié
     */
    public BoisExperimental(String essence) {
        this.cheminFichier = "Temperatures_TXT/" + essence.replace("é", "e").replace("É", "E").replace("è", "e")
                .replace("ê", "e").replace("à", "a").replace("ç", "c").replace("ù", "u")
                .replaceAll("\\s+", "") + "_sec.txt";
        this.remplirTableauDonneesBoisExp();  // remplir la matrice des données de température
        this.determinerTemperaturesExperimentalesIntermediaires();
        this.determinerBornesTemperatures();  // déterminer les températures extrêmes
        this.genererCouleursEpaisseurs();  // générer aléatoirement les couleurs des épaisseurs
    }  // fin du constructeur BoisExperimental
    
    /**
     * Remplir la matrice des températures du bois étudié
     */
    private void remplirTableauDonneesBoisExp() {
        
        try {
            this.determinerNbLignesFichierTexte();
            this.temperaturesBois = new double[this.nbLignes-2][12];  // retirer l'entête des épaisseurs et la ligne ligne blanche de fin de fichier + la colonne des temps

            Scanner lecteur = new Scanner(new File(this.cheminFichier));
            lecteur.nextLine();  // retirer l'entête des épaisseurs
            int indiceEpaisseur = 0;  // indice de colonne 
            
            for (int indiceTemps=0;indiceTemps<this.nbLignes-2;indiceTemps++) { // indice de ligne VALEUR BRUTE A CHANGER
                for (String d : Arrays.copyOfRange(lecteur.nextLine().split("\\s+"), 1, 13)) {  // retirer temps colonne d'indice 0 et passer outre les caractères de séparation
                    temperaturesBois[indiceTemps][indiceEpaisseur] = Double.parseDouble(d);
                    indiceEpaisseur++;    
                }
                indiceEpaisseur=0;  // réinitialisation pour le temps suivant
            }
            this.minTempGlobal = this.temperaturesBois[0][0];
        }
        catch (IOException FileNotFoundException) {
            this.temperaturesBois = new double[1][1];
            this.temperaturesBois[0][0] = -1;  // valeur d'erreur ATTENTION CECI NE FONCTIONNE PAS LERREUR NEST PAS REJETEE CORRECTEMENT
            System.out.println("Chemin de fichier inexistant ; \n " + this.cheminFichier + " n'existe pas.");
        }
    }  // fin remplirTableauDonneesBoisExp
    
    /**
     * Méthode qui permet de déterminer les températures pour les épaisseurs 
     * non-classique (i.e. non comprises entre 0 et 20 et non paires, 
     * ou différentes de e=30mm et e=40mm) à partir des températures des 
     * épaisseurs connues
     */
    private void determinerTemperaturesExperimentalesIntermediaires() {
        
        double coeffPond;  // coefficient de pondération pour estimer la température en certaines épaisseurs
        double temperaturesBoisIntermediaires[][] = new double[this.nbLignes-2][41];  // initialisations
        
        for (int indiceTemps=0;indiceTemps<this.nbLignes-2;indiceTemps++) {
            
            // pour epaisseur 0mm
            temperaturesBoisIntermediaires[indiceTemps][0] = this.temperaturesBois[indiceTemps][0];
            
            // pour epaisseur 1mm : miyenne des températures inférieure et supérieure
            temperaturesBoisIntermediaires[indiceTemps][1] = this.temperaturesBois[indiceTemps][0];
            
            // pour épaisseurs 2mm à 20mm
            for (int epaisseur=2;epaisseur<=20;epaisseur+=2) { // si la valeur est connue et issue des fichiers : épaisseur paire
                    temperaturesBoisIntermediaires[indiceTemps][epaisseur] = this.temperaturesBois[indiceTemps][(int)epaisseur/2-1];
            }
            
            for (int epaisseur=3;epaisseur<=19;epaisseur+=2) {
                temperaturesBoisIntermediaires[indiceTemps][epaisseur] = (temperaturesBoisIntermediaires[indiceTemps][epaisseur-1] + temperaturesBoisIntermediaires[indiceTemps][epaisseur+1]) / 2;
            }

            // pour épaisseurs 21mm à 29mm
            for (int epaisseur=21;epaisseur<30;epaisseur++) {  // epaisseurs 21mm à 29mm            
                // epaisseurs comprises entre 21mm et 30mm : moyenne des températures inférieure et supérieure
                coeffPond = (double)(30 - epaisseur) / 10;
                temperaturesBoisIntermediaires[indiceTemps][epaisseur] = (this.temperaturesBois[indiceTemps][9] * coeffPond + this.temperaturesBois[indiceTemps][10] * (1 - coeffPond));
            }
            
            // pour épaisseur 30mm : valeur connue issue des fichiers
            temperaturesBoisIntermediaires[indiceTemps][30] = this.temperaturesBois[indiceTemps][10];
            
            // pour épaisseur 31mm à 39mm
            for (int epaisseur=31;epaisseur<40;epaisseur++) {
                coeffPond = (double)(40 - epaisseur) / 10;
                temperaturesBoisIntermediaires[indiceTemps][epaisseur] = (this.temperaturesBois[indiceTemps][10] * coeffPond + this.temperaturesBois[indiceTemps][11] * (1 - coeffPond));
            }
            
            // pour épaisseur 40mm : valeurs connues issues des fichiers
            temperaturesBoisIntermediaires[indiceTemps][40] = this.temperaturesBois[indiceTemps][11];
            
        }
        this.temperaturesBoisComplet = temperaturesBoisIntermediaires;
    }  // fin de la méthode determinerTemperaturesExperimentalesIntermediaires
    
    /**
     * Déterminer les bornes min et max des températures du bois étudié pour chaque épaisseur et au global (valeurs expérimentales)
     */
    private void determinerBornesTemperatures() {
        
        for (int i=0;i<41;i++) {  // initialisations
            this.bornesTemperaturesEpaisseurs[i][0] = this.minTempGlobal;  // valeur minimale
            this.bornesTemperaturesEpaisseurs[i][1] = 0;  // valeuur maximale ; nulle par défaut
        }
        
        for (int indiceEpaisseur=0;indiceEpaisseur<41;indiceEpaisseur++) {
            for (int i=0;i<this.nbLignes-2;i++) {
                double temperatureCourante = this.temperaturesBoisComplet[i][indiceEpaisseur];

                if (temperatureCourante < this.bornesTemperaturesEpaisseurs[indiceEpaisseur][0]) {
                    this.bornesTemperaturesEpaisseurs[indiceEpaisseur][0] = temperatureCourante;
                    if (temperatureCourante < this.minTempGlobal)  // température minimale globale
                        this.minTempGlobal = temperatureCourante;
                }
                if (temperatureCourante > this.bornesTemperaturesEpaisseurs[indiceEpaisseur][1]) {
                    this.bornesTemperaturesEpaisseurs[indiceEpaisseur][1] = temperatureCourante;
                    if (temperatureCourante > this.maxTempGlobal)  // température maximale globale
                        this.maxTempGlobal = temperatureCourante;
                }
            }
        }
    }  // fin determinerEpaisseurMinMaxAxesTemperature
    
    /**
     * Déterminer le nombre de lignes du fichier de données du bois
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void determinerNbLignesFichierTexte() throws FileNotFoundException, IOException {
        int n_lignes = 0;
        try (BufferedReader lecteurFichierDonneesBois = new BufferedReader(new FileReader(this.cheminFichier))) {
            while (lecteurFichierDonneesBois.readLine() != null) n_lignes++;
        }
        this.nbLignes = n_lignes;
    }  // fin determinerNbLignesFichierTexte
     
    /**
     * Associer de façon aléatoire une couleur à chaque épaisseur de bois
     */
    private void genererCouleursEpaisseurs() {
        int max, r, v, b;
        for (int i=0; i<41;i++) {
            if ((2 <= i && i<= 20 && i%2==0) || i == 30 || i == 40) {
                max = 256;  // éviter les couleurs trop claires, ie trois valeurs proches de 255
                r = ThreadLocalRandom.current().nextInt(max - i * 2);
                v = ThreadLocalRandom.current().nextInt(max - i * 5);  // tendre vers une valeur de bleu nulle (code rgb(0,0,0 = noir : on évite les couleurs claires)
                b = ThreadLocalRandom.current().nextInt(max - i);  // tendre vers une valeur de bleu nulle (code rgb(0,0,0 = noir : on évite les couleurs claires)
                this.couleursEpaisseur[i] = new Color(r, v, b);  // stockage de la couleur
            }
            else {
                this.couleursEpaisseur[i] = Color.red;  // couleur pour les épaisseurs approximées
            }
        }
    }  // fin genererCouleursEpaisseurs
}  // fin de la classe BoisExperimental
