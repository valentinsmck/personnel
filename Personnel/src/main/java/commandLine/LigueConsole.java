package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.util.ArrayList;
// --- IMPORTS AJOUTÉS ---
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
// ------------------------

import commandLineMenus.List;
import commandLineMenus.Menu;
import commandLineMenus.Option;

import personnel.*;

public class LigueConsole
{
    private GestionPersonnel gestionPersonnel;
    private EmployeConsole employeConsole;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // YYYY-MM-DD

    public LigueConsole(GestionPersonnel gestionPersonnel, EmployeConsole employeConsole)
    {
        this.gestionPersonnel = gestionPersonnel;
        this.employeConsole = employeConsole;
    }

    Menu menuLigues()
    {
        Menu menu = new Menu("Gérer les ligues", "l");
        menu.add(afficherLigues());
        menu.add(ajouterLigue());
        menu.add(selectionnerLigue());
        menu.addBack("q");
        return menu;
    }

    private Option afficherLigues()
    {
        return new Option("Afficher les ligues", "l", () -> {System.out.println(gestionPersonnel.getLigues());});
    }

    /**
     * MODIFIÉ : Utilise le toString() de Employe (qui affiche les dates)
     */
    private Option afficher(final Ligue ligue)
    {
        return new Option("Afficher la ligue", "l",
                () ->
                {
                    System.out.println(ligue);
                    System.out.println("administrée par " + ligue.getAdministrateur());
                }
        );
    }

    /**
     * MODIFIÉ : Utilise le toString() de Employe (qui affiche les dates)
     */
    private Option afficherEmployes(final Ligue ligue)
    {
        return new Option("Afficher les employes", "l", () -> {System.out.println(ligue.getEmployes());});
    }

    private Option ajouterLigue()
    {
        return new Option("Ajouter une ligue", "a", () ->
        {
            try
            {
                gestionPersonnel.addLigue(getString("nom : "));
            }
            catch(SauvegardeImpossible exception)
            {
                System.err.println("Impossible de sauvegarder cette ligue");
            }
        });
    }

    private Menu editerLigue(Ligue ligue)
    {
        Menu menu = new Menu("Editer " + ligue.getNom());
        menu.add(afficher(ligue));
        menu.add(gererEmployes(ligue));
        //menu.add(changerAdministrateur(ligue));
        menu.add(changerNom(ligue));
        menu.add(supprimer(ligue));
        menu.addBack("q");
        return menu;
    }

    private Option changerNom(final Ligue ligue)
    {
        return new Option("Renommer", "r",
                () -> {ligue.setNom(getString("Nouveau nom : "));});
    }

    private List<Ligue> selectionnerLigue()
    {
        return new List<Ligue>("Sélectionner une ligue", "e",
                () -> new ArrayList<>(gestionPersonnel.getLigues()),
                (element) -> editerLigue(element)
        );
    }

    /**
     * MODIFIÉ pour demander la date d'arrivée
     */
    private Option ajouterEmploye(final Ligue ligue)
    {
        return new Option("ajouter un employé", "a",
                () ->
                {
                    // Saisie standard
                    String nom = getString("nom : ");
                    String prenom = getString("prenom : ");
                    String mail = getString("mail : ");
                    String password = getString("password : ");

                    LocalDate dateArrivee = null;
                    LocalDate dateDepart = null;

                    try {
                        // --- Saisie Date Arrivée ---
                        String dateArriveeStr = getString("Date d'arrivée (YYYY-MM-DD) (ou vide pour aujourd'hui) : ");
                        if (dateArriveeStr != null && !dateArriveeStr.trim().isEmpty()) {
                            dateArrivee = LocalDate.parse(dateArriveeStr, DATE_FORMATTER);
                        } else {
                            dateArrivee = LocalDate.now(); // Défaut si vide
                        }

                        // --- Saisie Date Départ (AJOUTÉ) ---
                        String dateDepartStr = getString("Date de départ (YYYY-MM-DD) (ou vide si 'en poste') : ");
                        if (dateDepartStr != null && !dateDepartStr.trim().isEmpty()) {
                            dateDepart = LocalDate.parse(dateDepartStr, DATE_FORMATTER);
                        }

                        // --- CORRECTION DE L'APPEL (6 arguments) ---
                        Employe nouvelEmploye = ligue.addEmploye(nom, prenom, mail, password, dateArrivee, dateDepart);
                        System.out.println("Employé ajouté : " + nouvelEmploye); // toString() affichera les dates

                    } catch (DateTimeParseException e) {
                        System.err.println("Erreur : Format de date invalide. L'employé n'a PAS été créé.");
                    } catch (DateIncoherenteException e) {
                        // Gère l'erreur si la date de départ est avant l'arrivée (du constructeur Employe)
                        System.err.println("Erreur : " + e.getMessage() + " L'employé n'a PAS été créé.");
                    }
                }
        );
    }

    private Menu gererEmployes(Ligue ligue)
    {
        Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
        menu.add(afficherEmployes(ligue));
        menu.add(ajouterEmploye(ligue));
        menu.add(modifierEmploye(ligue));
        menu.add(supprimerEmploye(ligue));
        menu.addBack("q");
        return menu;
    }

    private List<Employe> supprimerEmploye(final Ligue ligue)
    {
        return new List<>("Supprimer un employé", "s",
                () -> new ArrayList<>(ligue.getEmployes()),
                (index, element) -> {element.remove();}
        );
    }

    private List<Employe> changerAdministrateur(final Ligue ligue)
    {
        return null;
    }

    private List<Employe> modifierEmploye(final Ligue ligue)
    {
        return new List<>("Modifier un employé", "e",
                () -> new ArrayList<>(ligue.getEmployes()),
                employeConsole.editerEmploye() // Utilise l'EmployeConsole mis à jour
        );
    }

    private Option supprimer(Ligue ligue)
    {
        return new Option("Supprimer", "d", () -> {ligue.remove();});
    }

}