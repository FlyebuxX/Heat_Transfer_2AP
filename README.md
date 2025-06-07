
Objectif de la modélisation
---------------------------

Le problème à résoudre consiste à modéliser les transferts de chaleur dans différentes essences de bois sec, soumises à un faible flux thermique de 4 kW/m². La température reste inférieure à 200 °C, ce qui évite toute pyrolyse, combustion ou réaction chimique. Le transfert de chaleur est donc le seul phénomène considéré.  
  

Méthodologie numérique
----------------------

L’objectif est de reproduire numériquement l’évolution de la température dans le bois au cours du temps,  en se basant sur les propriétés thermiques du matériau :  
*   Conductivité thermique
*   Capacité thermique massique
*   Masse volumique

Le modèle est unidimensionnel, basé sur l’équation de la chaleur, et résolu par volumes finis avec un schéma explicite.  
 
Conditions aux limites
----------------------

Les échanges de chaleur se font :  
- Par conduction dans le bois  
- Par convection et rayonnement à la surface  
- Par apport de chaleur dû au cône chauffant en face avant  

Validation expérimentale
------------------------

Les données expérimentales fournissent les températures mesurées pendant 7200 secondes pour cinq essences :  
Balsa, Peuplier, Épicéa, Mélèze et Chêne. Ces mesures permettent de comparer les résultats simulés et  
valider le modèle.
