# README #

#Note#
1. La version gratuite de l'auto completion des champs de google est limitée à 1000 requête par jour.Il peut arriver que l'offre et la recherche de coivoiturage ne fonctionne pas correctement  dans le cas ou cette limite serait dépassée. ref: https://developers.google.com/places/webservice/usage

2. Pour le cellulaire, le mode paysage n'a pas été pris en compte. A utiliser en mode portrait

#Chose à faire pour la prochaine itération#

1. La vue Carte (dans l’interface Mes annonces et dans celle de la Recherche)  
2. Ajouter plus de réactivité  
    *Ajouter des spinning wheel lors des temps d’attente pendant l’exécution de certaines fonctionnalités(ex: recherche d’offre de covoiturage)  
3. Peaufiner le design  

#Bogue connu#
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