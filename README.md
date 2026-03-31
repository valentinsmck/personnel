# Personnel - Gestion des ligues et employes

## Objectif du projet
Ce projet implemente une application de gestion du personnel des ligues, dans le contexte M2L.

L'idee centrale est de manipuler un modele metier simple et coherent :
- un gestionnaire global du personnel ;
- des ligues ;
- des employes rattaches a chaque ligue ;
- un compte root avec des droits superieurs.

## Fonctionnement metier
L'application permet de :
- authentifier un utilisateur (root ou employe) ;
- creer, renommer et supprimer des ligues ;
- creer, modifier et supprimer des employes ;
- attribuer ou changer l'administrateur d'une ligue ;
- gerer les dates d'arrivee et de depart d'un employe avec validation metier ;
- proteger les mots de passe par hachage (SHA-256).

Les identifiants de connexion acceptent plusieurs formats (root, mail, nom ou nom.prenom).

## Architecture
Le projet suit une separation en couches :
- couche metier : regles de gestion et objets du domaine ;
- couche persistance : acces aux donnees via une interface de passerelle ;
- couche presentation : interface graphique Swing et interface console.

La persistance est abstraite par l'interface Passerelle avec deux implementations :
- JDBC (base MySQL), utilisee par defaut ;
- Serialization (historique), conservee pour compatibilite.

## Organisation du depot
- DOC : documents de suivi et script SQL.
- Personnel/src/personnel : classes metier (GestionPersonnel, Ligue, Employe, exceptions).
- Personnel/src/jdbc : implementation JDBC et configuration des credentials.
- Personnel/src/serialisation : implementation alternative par serialisation Java.
- Personnel/src/gui : interface graphique.
- Personnel/src/commandLine : interface console.
- Personnel/src/testsUnitaires : tests unitaires.

## Etat d'avancement
Le projet couvre une grande partie des objectifs de Personnel 3 et une partie de Personnel 4.

Les points deja bien avances concernent notamment :
- l'integration JDBC ;
- la gestion du root ;
- la lecture et l'ecriture des administrateurs ;
- l'insertion et la suppression des employes.

Des points restent a consolider selon le suivi des taches :
- certaines evolutions de la couche graphique ;
- des cas de modification/lecture/suppression encore signales comme incomplets.

Le suivi detaille est maintenu dans DOC/taches_personnel.md.