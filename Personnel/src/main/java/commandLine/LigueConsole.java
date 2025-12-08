package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import commandLineMenus.List;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import commandLineMenus.Action;

import personnel.*;

public class LigueConsole
{
    private GestionPersonnel gestionPersonnel;
    private EmployeConsole employeConsole;

    // Pour gérer la saisie des dates
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

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
        menu.add(changerNom(ligue));
        menu.add(changerAdministrateur(ligue));
        menu.add(supprimer(ligue));
        menu.addBack("q");
        return menu;
    }

    private Option changerNom(final Ligue ligue)
    {
        return new Option("Renommer", "r",
                () -> {ligue.setNom(getString("Nouveau nom : "));});
    }

    private List<Employe> changerAdministrateur(final Ligue ligue)
    {
        return new List<Employe>("Changer l'administrateur", "a",
                () -> new ArrayList<>(ligue.getEmployes()),
                new commandLineMenus.ListAction<Employe>() {
                    @Override
                    public void itemSelected(int index, Employe employe) {
                        try {
                            ligue.setAdministrateur(employe);
                            System.out.println(employe.getNom() + " " + employe.getPrenom() + " est maintenant administrateur de " + ligue.getNom());
                        } catch (DroitsInsuffisants e) {
                            System.err.println("Erreur : Cet employé n'appartient pas à cette ligue.");
                        }
                    }
                }
        );
    }

    private List<Ligue> selectionnerLigue()
    {
        return new List<Ligue>("Sélectionner une ligue", "e",
                () -> new ArrayList<>(gestionPersonnel.getLigues()),
                (element) -> editerLigue(element)
        );
    }

    // --- CORRECTION : AJOUT DES DATES POUR SATISFAIRE LE COMPILATEUR ---
    private Option ajouterEmploye(final Ligue ligue)
    {
        return new Option("ajouter un employé", "a",
                () ->
                {
                    String nom = getString("nom : ");
                    String prenom = getString("prenom : ");
                    String mail = getString("mail : ");
                    String password = getString("password : ");

                    LocalDate dateArrivee = null;
                    LocalDate dateDepart = null;

                    try {
                        // On demande les dates (ou on laisse vide)
                        String dateArriveeStr = getString("Date d'arrivée (YYYY-MM-DD) (ou vide pour aujourd'hui) : ");
                        if (dateArriveeStr != null && !dateArriveeStr.trim().isEmpty()) {
                            dateArrivee = LocalDate.parse(dateArriveeStr, DATE_FORMATTER);
                        } else {
                            dateArrivee = LocalDate.now();
                        }

                        String dateDepartStr = getString("Date de départ (YYYY-MM-DD) (ou vide si 'en poste') : ");
                        if (dateDepartStr != null && !dateDepartStr.trim().isEmpty()) {
                            dateDepart = LocalDate.parse(dateDepartStr, DATE_FORMATTER);
                        }

                        // Appel avec 6 arguments (CORRECTION DE L'ERREUR)
                        ligue.addEmploye(nom, prenom, mail, password, dateArrivee, dateDepart);

                    } catch (DateTimeParseException e) {
                        System.err.println("Format de date invalide.");
                    } catch (DateIncoherenteException e) { // On attrape l'exception définie dans votre projet
                        System.err.println("Erreur de dates : " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Erreur : " + e.getMessage());
                    }
                }
        );
    }

    // --- NOUVEAU MENU DE SÉLECTION (Mission Actuelle) ---

    private Menu gererEmployes(Ligue ligue)
    {
        Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
        menu.add(afficherEmployes(ligue));
        menu.add(ajouterEmploye(ligue));
        // L'option clé demandée par la mission :
        menu.add(selectionnerEmploye(ligue));
        menu.addBack("q");
        return menu;
    }

    private List<Employe> selectionnerEmploye(final Ligue ligue)
    {
        return new List<>("Sélectionner un employé", "e",
                () -> new ArrayList<>(ligue.getEmployes()),
                (element) -> gererEmploye(element)
        );
    }

    private Menu gererEmploye(Employe employe)
    {
        Menu menu = new Menu("Gérer " + employe.getNom(), "g");
        menu.add(employeConsole.editerEmploye(employe));
        menu.add(supprimerEmploye(employe));
        menu.addBack("q");
        return menu;
    }

    private Option supprimerEmploye(final Employe employe)
    {
        return new Option("Supprimer", "s", () -> {
            employe.remove();
            Action.QUIT.optionSelected();
        });
    }

    private Option supprimer(Ligue ligue)
    {
        return new Option("Supprimer", "d", () -> {ligue.remove();});
    }
}