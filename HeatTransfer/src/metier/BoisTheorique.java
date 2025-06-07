package metier;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe défininissant une essence de bois théorique
 * @author Loïc BELLEC & Valentin EBERHARDT
 */
public class BoisTheorique {
    public final String cheminFichier;
    public double minTempGlobal;
    public double maxTempGlobal;
    public double[][] bornesTemperaturesEpaisseurs = new double[41][2];  // 12 épaisseurs pour deux températures extrêmes
    public double[][] temperaturesBoisGlobal;
    public double[][] temperaturesBois;  // matrice des températures du bois affiché
    public Color[] couleursEpaisseur = new Color[12];
    public int nbLignes = 0;  // défaut
    public int indiceBois;  // par défaut, le nombre de lignes du fichier relatif à l'essence de bois est nul
    
    // Dans l'ordre alphabétique : balsa (i=0), chêne(i=1), épicéa(i=2), mélèze(i=3), peuplier(i=4)
    public final double[] emissivitesThermiques = {0.855, 0.93, 0.9, 0.93, 0.875};  // tableau des émissivités epsilon
    public final double[] conductivitesThermiques = {0.07, 0.125, 0.1, 0.123, 0.08};  // tableau des conductivités thermiques lambda
    public final double[] massesVolumiques = {110, 750, 480, 650, 350};  // tableau des masses volumiques rho
    
    /**
     * Constructeur à un paramètre
     * @param essence String : bois étudié
     */
    public BoisTheorique(String essence) {
        this.cheminFichier = "Temperatures_TXT/" + essence + "_sec.txt";
        this.determinerIndiceBois(essence);
        this.remplirTableauDonneesBoisTh(this.temperaturesBoisGlobal);  // remplir la matrice des données de température
        this.determinerEpaisseurMinMaxAxesTemperature(this.temperaturesBoisGlobal);  // déterminer les températures extrêmes
    }  // fin du constructeur BoisTheorique
    
    /**
     * Déterminer l'indice de l'essence
     * @param essence String : essence de bois à considérer
     */
    private void determinerIndiceBois(String essence) {
        int i = 0;
        String[] essencesBois = {"Balsa", "Chene", "Epicea", "Meleze", "Peuplier"};
        for (String e : essencesBois) {
            if (e.equals(essence)) {
                this.indiceBois = i;
            }
            i++;
        }
    }  // fin determinerIndiceBois
    
    /**
     * Remplir le tableau des températures à l'aide des équations de la physique
     */
    private void remplirTableauDonneesBoisTh(double[][] tab) {
        // Intialisation des constantes physiques
        final int T0 = 293; // K on pose T0=20°C
        final double cteStefanBoltzmann = 0.0000000567; // W / m2 / K4
        final int hConvectifAvant = 10; // W / m2 / K
        final int hConvectifArriere = 12; // W / m2 / K
        final int alpha = 1;
        final int capaciteThermique = 1950; // kJ / kg / K
        final double deltaTemps = 1;  // delta t=1s
        final double deltaEpaisseurs = 0.001;  // delta e=0.001m
        final double fluxCone = 4000; // W / m2
        final double mv = this.massesVolumiques[indiceBois];  // kg / m3
        final double emissivite = this.emissivitesThermiques[indiceBois];  // sans unité
        final double conductivite = this.conductivitesThermiques[indiceBois];
        double a = conductivite / (mv * capaciteThermique);  // a = lambda / pCp;
        
        double deltaA, deltaB, deltaC;
        double tempPrecMemeEpaisseur, tempPrecEpaisseurPlus, tempPrecEpaisseurMoins;
        final int temps = 7200;
        
        try {
            this.determinerNbLignesFichierTexte();  // remplir le tableau des températures au temps initial
            tab = new double[this.nbLignes-2][41];  // retirer entête + ligne blanche fin de fichier + colonne temps ; 13 pour épasseurs COMMENCANT A 0 et allant jusqu'à 40mm
            
            for (int epaisseur=0;epaisseur<41;epaisseur++) {  // initialisations
                tab[0][epaisseur] = T0;  // a t=0s, toutes les épaisseurs sont à T0
            }

            for (int t=1; t<=temps; t++) {  // itération du temps en secondes

                tempPrecMemeEpaisseur = tab[t-1][0];
                tempPrecEpaisseurPlus = tab[t-1][1];

                // Face exposée en A e=0mm
                deltaA = a * (deltaTemps / Math.pow(deltaEpaisseurs, 2)) * (tempPrecEpaisseurPlus - tempPrecMemeEpaisseur) + 
                    ((deltaTemps) / (mv * capaciteThermique * deltaEpaisseurs)) * (alpha * fluxCone - hConvectifAvant * (tempPrecMemeEpaisseur - T0) - 
                    emissivite * cteStefanBoltzmann * (Math.pow(tempPrecMemeEpaisseur, 4) - Math.pow(T0, 4)));

                tab[t][0] = tempPrecMemeEpaisseur + deltaA;  // assignation de la température

                // Face exposée en B : diffusion jusqu'à e=38mm
                for (int e=1; e<40; e+=1){  // épaisseurs allant de 2mm jusque 40 mm

                    tempPrecMemeEpaisseur = tab[t-1][e];
                    tempPrecEpaisseurPlus = tab[t-1][e+1];
                    tempPrecEpaisseurMoins = tab[t-1][e-1];

                    deltaB = a * (deltaTemps / Math.pow(deltaEpaisseurs, 2)) * (tempPrecEpaisseurPlus - 2 * tempPrecMemeEpaisseur + tempPrecEpaisseurMoins);

                    tab[t][e] = tempPrecMemeEpaisseur + deltaB;  // assignation de la température
                }

                tempPrecMemeEpaisseur = tab[t-1][40];
                tempPrecEpaisseurMoins = tab[t-1][39];

                // Face exposée en C : e=40mm
                deltaC = a * (deltaTemps / Math.pow(deltaEpaisseurs, 2)) * (tempPrecEpaisseurMoins - tempPrecMemeEpaisseur) + (deltaTemps / (mv * capaciteThermique * deltaEpaisseurs)) * hConvectifArriere * (T0 - tempPrecMemeEpaisseur);

                tab[t][40] = tab[t-1][40] + deltaC;
            }
            
            this.temperaturesBoisGlobal = tab;
            this.reconstruireTableauTheorique(this.temperaturesBoisGlobal);
        }
        catch (IOException FileNotFoundException) {
            tab = new double[1][1];
            tab[0][0] = -1;  // valeur d'erreur
        }
    }  // fin remplirTableauDonneesBoisTh
    
    /**
     * Sélectionner uniquement les épaisseurs de bois demandées (épaisseurs paire de 2mm à 20mm + 30mm + 40mm)
     * (i.e. exclure celles en trop calculées durant la phase qui précède)
     * @param tab double[][] : tableau de températures à trier
     */
    private void reconstruireTableauTheorique(double tab[][]) {     
        this.temperaturesBois = new double[tab.length][12];  // initialisation (e=2mm-20mm + 30mm + 40mm)
        int indice = 0;
        
        for (int t=0;t<=tab.length-2;t++) {
            for (int indice_e=2;indice_e<tab[0].length;indice_e++) {  
                if (((2<=indice_e && indice_e<=20) && indice_e % 2 == 0) || indice_e == 30 || indice_e == 40) {  // exclure la face A (e=0mm) et les épaisseurs 22 à 28mm et 32 à 38mm
                    this.temperaturesBois[t][indice] = tab[t][indice_e];  // sélection
                    indice++;
                }    
            }
            indice=0;  // réinitialisation pour le temps suivant
        }
    }  // fin reconstruireTableauTheorique
    
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
     * Déterminer les bornes min et max des températures du bois étudié pour chacune des 41 épaisseurs
     */
    private void determinerEpaisseurMinMaxAxesTemperature(double tab[][]) {
        double temperatureCourante;
        
        if (tab[0][0] != -1) {  // si le fichier renseigné est correct
            for (int i=0;i<tab[0].length;i++) {  // initialisations
                this.bornesTemperaturesEpaisseurs[i][0] = this.minTempGlobal;  // initialisation température minimale
                this.bornesTemperaturesEpaisseurs[i][1] = 0;  // initialisation température maximale
            }

            for (int iColonne =0;iColonne<tab[0].length;iColonne++) {
                for (int i=0;i<7200;i++) {
                    temperatureCourante = tab[i][iColonne];

                    if (temperatureCourante < this.bornesTemperaturesEpaisseurs[iColonne][0]) {
                        this.bornesTemperaturesEpaisseurs[iColonne][0] = (int) temperatureCourante;
                        if (temperatureCourante < this.minTempGlobal)  // température minimale globale
                            this.minTempGlobal = temperatureCourante;
                    }
                    if (temperatureCourante > this.bornesTemperaturesEpaisseurs[iColonne][1]) {
                        this.bornesTemperaturesEpaisseurs[iColonne][1] = (int) temperatureCourante;
                        if (temperatureCourante > this.maxTempGlobal)  // température maximale globale
                            this.maxTempGlobal = temperatureCourante;
                    }
                }
            }
        }
    }  // fin determinerEpaisseurMinMaxAxesTemperature
}  // fin de la classe BoisTheorique
