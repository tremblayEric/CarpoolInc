# README #
Projet de maitrise en génie logiciel développé en équipe de 4 dans le cadre du cours MGL7130
**Gaëlle Claude
**Jeanne Darisca
**Amira Dahnes
**Eric Tremblay


# cle APK#
La cle de l'APK est la suivante: ERAMGAJE

#Note#
<!-- 26 avril 2015 -->

1. Ne pas faire de réservation sur une offre que l’on a posté soit même; ça ne fonctionne pas.

2. On n'interdit pas les annonces postées à des dates antérieures. Mais il ne sera pas possible de les voir dans mes annonces ni même de trouver ces annonces lorsqu'on fait des recherches.

3. Les destinations avec accent sont a éviter. Ex: Montréal.
 
4. le nombre de places n'est pas correctement géré.

5. Il y a un problème avec le spinner au niveau de l’interface. Il tourne parfois indéfiniment.

6. On ne vérifie pas dans l’ajout d’offre si l’heure de début est inférieure a l’heure de fin.


#Note#
<!-- 22 mars 2015 -->

1. La version gratuite de l'auto completion des champs de google est limitée à 1000 requête par jour.Il peut arriver que l'offre et la recherche de coivoiturage ne fonctionne pas correctement  dans le cas ou cette limite serait dépassée. ref: https://developers.google.com/places/webservice/usage

2. Pour le cellulaire, le mode paysage n'a pas été pris en compte. A utiliser en mode portrait

#Chose à faire pour la prochaine itération#

1. La vue Carte (dans l’interface Mes annonces et dans celle de la Recherche)  
2. Ajouter plus de réactivité  
    *Ajouter des spinning wheel lors des temps d’attente pendant l’exécution de certaines fonctionnalités(ex: recherche d’offre de covoiturage)  
3. Peaufiner le design  
4. Développement le reste des fonctionnalités

#Bogues connus#
**Certaines vérifations au niveau des champs d’entrée n’ont pas été effectuées :**  
       Les champs de départ et d’arrivée dans l’interface de recherche ainsi que ceux de l’interface de l’annonce.   
       Dans l’interface de Recherche et d’Annonce, on ne vérifie pas si la date choisie est antérieure ou pas à la date d’aujourd’hui  
       Dans l’interface de l’offre, on ne vérifie pas si l’heure de fin est supérieure à l’heure de début  

**Implémenter une stratégie de validation des entrées** 
      Si l'utilisateur entre n'importe quoi dans le champ depart l’application plante.  
      L’heure de départ peut être plus grande que celle de l’arrivée lors de la création d’une offre.  
      Les dates peuvent être dans le passé  
      Une offre avec des champs incomplets ou invalides ne doit pas pouvoir être soumise  
      Une recherche sur une offre qui n’existe pas fait planter l’Application
