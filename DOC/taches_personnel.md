# Tâches – Personnel 3 & 4

## Légende
- ✅ Terminé
- ❌ Échec – à refaire
- 🔄 À remettre

---

## ✅ Personnel 3 – Connexion à la base de données

- Créer une base de données dédiée à ce projet dans un serveur MySQL
- Exécuter le script de création de tables
- Créer un compte n'ayant accès qu'à cette base de données
- Créer une copie de `CredentialsExample.java` nommée `Credentials.java`
- Renseigner les identifiants de connexion à la base de données de développement (vérifier que cette copie n'est pas versionnée)
- Modifier la ligne 25 de `GestionPersonnel` pour utiliser JDBC plutôt que la sérialisation
- Vérifier que l'application lit les ligues depuis la base de données et peut en ajouter de nouvelles

### Barème

| Critère | Points |
|---|---|
| Variable de classe `TYPE_PASSERELLE` | Non : 0 / Oui : 1 |
| `Credentials.java` non versionné | Versionné : 0 / Pas versionné : 1 |

---

## ❌ Personnel 3 – Maquettes interface graphique

- Réaliser des maquettes de l'interface graphique
- Préciser quelles fenêtres créer et leur contenu
- Inutile de soigner le design ou la mise en page
- Ne pas créer de fenêtre pour le menu du dialogue en ligne de commande ; exploiter les possibilités d'une interface graphique
- Versionner les maquettes avec GitHub

### Barème

| Critère | Points |
|---|---|
| Existe | Non : 0 / Oui : 1 |
| Connexion | Non : 0 / Presque : 1 / Oui : 2 |
| Liste des ligues | Non : 0 / Presque : 1 / Oui : 2 |
| Ajouter/modifier/supprimer ligues | Non : 0 / Presque : 1 / Oui : 2 |
| Liste des employés | Non : 0 / Presque : 1 / Oui : 2 |
| Ajouter/modifier/supprimer employés | Non : 0 / Presque : 1 / Oui : 2 |
| Admin des ligues | Non : 0 / Presque : 1 / Oui : 2 |
| Gestion du Root | Non : 0 / Presque : 1 / Oui : 2 |

> ⚠️ **Feedback prof :**
> - Le bouton supprimer aurait été mieux placé dans le tableau. Pourquoi 2 boutons renommer ?
> - Pourquoi un bouton par champ ?
> - Le bouton admin est présent, mais que se passe-t-il quand on clique dessus ?

---

## ✅ Personnel 3 – Insertion root

- Ajouter dans la Passerelle une méthode `public int insert(Employe employe) throws SauvegardeImpossible`
- Ajouter une variable d'instance `id` dans la classe `Employe`
- Implémenter cette méthode dans la classe `JDBC` (modèle : `insert(Ligue ligue)`)
- Créer une méthode `int insert(Employe employe)` dans `GestionPersonnel` (modèle : `int insert(Ligue ligue)`)
- Adapter le constructeur de `Employe` (sur le modèle du constructeur de `Ligue`) pour insérer le root à la création
- Créer une méthode `addRoot(...)` dans `GestionPersonnel` pour créer le root à partir de son nom et mot de passe, puis l'affecter à la variable d'instance `root`

### Barème

| Critère | Points |
|---|---|
| Méthode dans la passerelle | Non : 0 / Oui : 1 |
| Implémentation dans Serialization | Non : 0 / Oui : 1 |
| Implémentation dans JDBC | Non : 0 / Oui : 1 |
| Relais dans GestionPersonnel | Non : 0 / Oui : 1 |
| Méthode `addRoot()` dans GestionPersonnel | Non : 0 / Oui : 1 |
| Constructeur dans Employe | Non : 0 / Oui : 1 |
| Variable d'instance `id` dans Employe | Non : 0 / Oui : 1 |

> ⚠️ **Feedback prof :** Le setter n'est pas utile.

---

## ✅ Personnel 3 – Lecture root

- Créer un deuxième constructeur de `Employe` pour instancier un objet depuis des données MySQL (modèle : surcharge du constructeur de `Ligue`)
- Créer une méthode `addRoot(...)` dans `GestionPersonnel` pour créer le root depuis la base de données et l'affecter à `root`
- Dans `getGestionPersonnel()` de la classe `JDBC`, remplacer la valeur de `root` par les informations lues en base

### Barème

| Critère | Points |
|---|---|
| Surcharge constructeur Employe | Non : 0 / Presque : 1 / Oui : 2 |
| Méthode `addRoot()` dans GestionPersonnel | Non : 0 / Presque : 1 / Oui : 2 |
| Chargement Root depuis la base | Non : 0 / Presque : 1 / Oui : 2 |

> ⚠️ **Feedback prof :** Votre MCD n'est pas présent dans votre dépôt.

---

## ❌ Personnel 3 – Modification ligue

- Ajouter dans la Passerelle une méthode `update(Ligue ligue)`
- Implémenter cette méthode dans la classe `JDBC` (modèle : `insert(Ligue ligue)`)
- Créer une méthode `update(Ligue ligue)` dans `GestionPersonnel` (modèle : `int insert(Ligue ligue)`)
- Dans chaque setter de la classe `Ligue`, insérer un appel à `gestionPersonnel.update(ligue)`

### Barème

| Critère | Points |
|---|---|
| Méthode `update(ligue)` dans la passerelle | Non : 0 / Oui : 1 |
| Implémentation dans JDBC | Non : 0 / Presque : 1 / Oui : 2 |
| Méthode `update(ligue)` dans GestionPersonnel | Non : 0 / Presque : 1 / Oui : 2 |
| Appel à `gestionPersonnel.update(ligue)` | Non : 0 / Presque : 1 / Oui : 2 |

> ⚠️ **Feedback prof :** Ne pas rattraper l'exception dans la couche métier ! Il faut propager l'exception pour la rattraper dans `commandLine`, et ne surtout pas faire d'entrées/sorties depuis la couche métier.

---

## ✅ Personnel 3 – Modification root

- Ajouter dans la Passerelle une méthode `update(Employe employe)`
- Implémenter cette méthode dans la classe `JDBC`
- Créer une méthode `update(Employe employe)` dans `GestionPersonnel`
- Dans chaque setter de la classe `Employe`, insérer un appel à `gestionPersonnel.update(employe)`

### Barème

| Critère | Points |
|---|---|
| Méthode `update(employe)` dans la passerelle | Non : 0 / Oui : 1 |
| Implémentation dans JDBC | Non : 0 / Presque : 1 / Oui : 2 |
| Méthode `update(employe)` dans GestionPersonnel | Non : 0 / Presque : 1 / Oui : 2 |
| Appel à `gestionPersonnel.update(employe)` | Non : 0 / Presque : 1 / Oui : 2 |

---

## ❌ Personnel 3 – Lecture employés

- Pour chaque ligue parcourue dans `getGestionPersonnel()` de `JDBC`, faire une requête SQL pour lire les employés de la ligue
- Instancier un objet `Employe` pour chaque ligne lue (via le constructeur créé à l'étape Lecture root)

### Barème

| Critère | Points |
|---|---|
| Requête avec jointure | Non : 0 / Presque : 1 / Oui : 2 |
| Création de l'objet Employé | Non : 0 / Presque : 1 / Oui : 2 |

> ⚠️ **Feedback prof :** Le constructeur de `Employe` ne devrait pas être visible dans la classe `JDBC`.

---

## ✅ Personnel 3 – Insertion employés

- Adapter le code pour pouvoir insérer un `Employe` dans la base de données

### Barème

| Critère | Points |
|---|---|
| Jointure avec la ligue | Non : 0 / Presque : 1 / Oui : 2 |

---

## ✅ Personnel 3 – Suppression employés

- Adapter le code pour pouvoir supprimer un `Employe` de la base de données

### Barème

| Critère | Points |
|---|---|
| Passerelle | Non : 0 / Presque : 1 / Oui : 2 |
| Implémentation dans la passerelle | Non : 0 / Presque : 1 / Oui : 2 |
| Appel dans GestionPersonnel | Non : 0 / Presque : 1 / Oui : 2 |
| Appel dans `.delete()` | Non : 0 / Presque : 1 / Oui : 2 |

---

## 🔄 Personnel 3 – Suppression ligue

- Adapter le code pour pouvoir supprimer une ligue de la base de données
- Attention : tenir compte des employés présents dans la ligue

### Barème

| Critère | Points |
|---|---|
| Passerelle | Non : 0 / Presque : 1 / Oui : 2 |
| Implémentation dans la passerelle | Non : 0 / Presque : 1 / Oui : 2 |
| Appel dans GestionPersonnel | Non : 0 / Presque : 1 / Oui : 2 |
| Appel dans `.delete()` | Non : 0 / Presque : 1 / Oui : 2 |
| Gestion des employés | Non : 0 / Presque : 1 / Oui : 2 |

---

## ❌ Personnel 3 – Modification employés

- Adapter le code pour pouvoir modifier les données d'un employé déjà présent dans la base de données

### Barème

| Critère | Points |
|---|---|
| Passerelle | Non : 0 / Presque : 1 / Oui : 2 |
| Implémentation | Non : 0 / Presque : 1 / Oui : 2 |
| Relais | Non : 0 / Presque : 1 / Oui : 2 |
| Déclenchement | Non : 0 / Presque : 1 / Oui : 2 |

---

## ✅ Personnel 3 – Lecture administrateur

- Dans la boucle de `getGestionPersonnel()` de `JDBC`, extraire de la base de données l'administrateur de chaque ligue
- Appeler (voire adapter) le code de la couche métier pour mettre en relation chaque ligue avec son administrateur

### Barème

| Critère | Points |
|---|---|
| Lecture dans la base | Non : 0 / Presque : 1 / Oui : 2 |
| Écriture dans la couche métier | Non : 0 / Presque : 1 / Oui : 2 |

> ⚠️ **Feedback prof :** `setAdminFromJdbc` c'est bof.

---

## ✅ Personnel 3 – Écriture administrateur

- Adapter le code pour enregistrer dans la base de données un changement d'administrateur (selon la modélisation choisie)

### Barème

| Critère | Points |
|---|---|
| Lecture dans la couche métier | Non : 0 / Presque : 1 / Oui : 2 |
| Écriture dans la base de données | Non : 0 / Presque : 1 / Oui : 2 |

---

## 🔄 Personnel 4 – Interface graphique

- Réaliser une interface graphique (Swing ou autre framework)
- Un sous-programme par composant
- Un sous-programme par conteneur
- Un sous-programme par événement

### Barème

| Critère | Points |
|---|---|
| Variable de classe `TYPE_PASSERELLE` | Non : 0 / Oui : 1 |
| `Credentials.java` non versionné | Versionné : 0 / Pas versionné : 1 |

---

## 🔄 Personnel 4 – Hachage des mots de passe

- Hacher les mots de passe avant de les stocker dans la base de données

### Barème

| Critère | Points |
|---|---|
| Écriture | Non : 0 / Presque : 1 / Oui : 2 |
| Authentification | Non : 0 / Presque : 1 / Oui : 2 |
