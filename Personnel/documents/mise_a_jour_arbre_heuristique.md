# Mise à jour de l'arbre heuristique

## Nouvelle fonctionnalité ajoutée : Changement d'administrateur

### 📍 Emplacement dans le menu

```
Menu Principal
  → Gérer les ligues (l)
    → Sélectionner une ligue (e)
      → Editer [Nom de la ligue]
        → Afficher la ligue (l)
        → Gérer les employés de [Ligue] (e)
        → Renommer (r)
        → **Changer l'administrateur (a)** ← NOUVEAU
        → Supprimer (d)
```

### 🔄 Flux de changement d'administrateur

```
Changer l'administrateur (a)
  → Affiche la liste des employés de la ligue
  → Sélectionner un employé
  → L'employé devient le nouvel administrateur
  → Message de confirmation affiché
```

### 🎨 Pour mettre à jour l'arbre heuristique dans Draw.io :

1. Ouvrir `arbreheuristique.drawio.html` dans un navigateur
2. Dans le nœud "Editer [Ligue]", ajouter une nouvelle branche :
   - Créer un nœud "Changer l'administrateur (a)"
   - Le relier au nœud "Editer [Ligue]"
   - Ajouter un sous-nœud "Liste des employés"
   - Montrer la sélection qui mène à la confirmation
3. Exporter en PNG et remplacer `arbreheuristique.png`

### 💻 Code implémenté

**Fichier** : `src/main/java/commandLine/LigueConsole.java`

**Méthode ajoutée** :
```java
private List<Employe> changerAdministrateur(final Ligue ligue)
{
    return new List<>("Changer l'administrateur", "a",
            () -> new ArrayList<>(ligue.getEmployes()),
            (employe) -> {
                try {
                    ligue.setAdministrateur(employe);
                    System.out.println(employe.getNom() + " " + employe.getPrenom() + 
                                     " est maintenant administrateur de " + ligue.getNom());
                } catch (DroitsInsuffisants e) {
                    System.err.println("Erreur : Cet employé n'appartient pas à cette ligue.");
                }
            }
    );
}
```

**Option ajoutée au menu** (ligne 82) :
```java
menu.add(changerAdministrateur(ligue));
```

### ✅ Fonctionnalités

- ✅ Affiche la liste des employés de la ligue
- ✅ Permet de sélectionner un employé pour le nommer administrateur
- ✅ Gère l'exception si l'employé n'appartient pas à la ligue
- ✅ Affiche un message de confirmation
