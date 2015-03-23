# README #

La vue Carte (dans l’interface Mes annonces et dans celle de la Recherche) sera développée à l’itération 2 
Ajouter plus de réactivité :
Ajouter des spinning wheel lors des temps d’attente pendant l’exécution de certaines fonctionnalités(ex: recherche d’offre de covoiturage)

Peaufiner le design
Certaines vérifations au niveau des champs d’entrée n’ont pas été effectuées :
les champs de départ et d’arrivée dans l’interface de recherche ainsi que ceux de l’interface de l’annonce. 
Dans l’interface de Recherche et d’Annonce, on ne vérifie pas si la date choisie est antérieure ou pas à la date d’aujourd’hui
Dans l’interface de l’offre, on ne vérifie pas si l’heure de fin est supérieure à l’heure de début

Implémenter une startégie de validation des entrée s
si l'utilisateur entre n'importe quoi dans le champ depart l’application plante.
l’heure de départ peut être plus grande que celle de l’arrivé lors de la création d’une offre.
les dates peuvent être dans le passé
une offre avec des champs incomplets ou invalide ne doit pas pouvoir être soumie
une recherche sur une offre qui n’existe pas fait planter l’Application




