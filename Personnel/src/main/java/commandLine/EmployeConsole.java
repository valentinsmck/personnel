package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

// Imports ajoutés
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import commandLineMenus.ListOption;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import personnel.DateIncoherenteException; // Import de notre exception
import personnel.Employe;

public class EmployeConsole
{
    // Ajout d'un formatteur de date standard
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // Format YYYY-MM-DD

    /**
     * Modifié : utilise maintenant le toString() de Employe (qui inclut les dates)
     */
    private Option afficher(final Employe employe)
    {
        // Le simple System.out.println(employe) suffit,
        // car nous avons modifié la méthode toString() de la classe Employe.
        return new Option("Afficher l'employé", "l", () -> {System.out.println(employe);});
    }

    ListOption<Employe> editerEmploye()
    {
        return (employe) -> editerEmploye(employe);
    }

    /**
     * Modifié pour ajouter les options de dates
     */
    Option editerEmploye(Employe employe)
    {
        Menu menu = new Menu("Gérer le compte " + employe.getNom(), "c");
        menu.add(afficher(employe));
        menu.add(changerNom(employe));
        menu.add(changerPrenom(employe));
        menu.add(changerMail(employe));
        menu.add(changerPassword(employe));

        // --- LIGNES AJOUTÉES ---
        menu.add(changerDateArrivee(employe));
        menu.add(changerDateDepart(employe));
        // -----------------------

        menu.addBack("q");
        return menu;
    }

    private Option changerNom(final Employe employe)
    {
        return new Option("Changer le nom", "n",
                () -> {employe.setNom(getString("Nouveau nom : "));}
        );
    }

    private Option changerPrenom(final Employe employe)
    {
        return new Option("Changer le prénom", "p", () -> {employe.setPrenom(getString("Nouveau prénom : "));});
    }

    private Option changerMail(final Employe employe)
    {
        return new Option("Changer le mail", "e", () -> {employe.setMail(getString("Nouveau mail : "));});
    }

    private Option changerPassword(final Employe employe)
    {
        return new Option("Changer le password", "x", () -> {employe.setPassword(getString("Nouveau password : "));});
    }

    // --- MÉTHODES AJOUTÉES ---

    /**
     * Nouvelle méthode pour changer la date d'arrivée, avec gestion des erreurs
     */
    private Option changerDateArrivee(final Employe employe)
    {
        return new Option("Changer la date d'arrivée", "da",
                () -> {
                    String dateStr = getString("Nouvelle date d'arrivée (YYYY-MM-DD) : ");
                    try {
                        LocalDate newDate = LocalDate.parse(dateStr, DATE_FORMATTER);
                        employe.setDateArrivee(newDate); // Le setter peut lever une exception
                        System.out.println("Date d'arrivée mise à jour.");
                    } catch (DateTimeParseException e) {
                        System.err.println("Erreur : Format de date invalide. Utilisez YYYY-MM-DD.");
                    } catch (DateIncoherenteException e) {
                        System.err.println("Erreur de cohérence : " + e.getMessage());
                    }
                }
        );
    }

    /**
     * Nouvelle méthode pour changer la date de départ, avec gestion des erreurs
     * et gestion de la saisie "vide" pour (null)
     */
    private Option changerDateDepart(final Employe employe)
    {
        return new Option("Changer la date de départ", "dd",
                () -> {
                    String dateStr = getString("Nouvelle date de départ (YYYY-MM-DD) (ou vide pour 'en poste') : ");
                    try {
                        LocalDate newDate = null;
                        if (dateStr != null && !dateStr.trim().isEmpty()) {
                            newDate = LocalDate.parse(dateStr, DATE_FORMATTER);
                        }
                        employe.setDateDepart(newDate); // Le setter peut lever une exception

                        if (newDate == null) {
                            System.out.println("Date de départ retirée (employé en poste).");
                        } else {
                            System.out.println("Date de départ mise à jour.");
                        }

                    } catch (DateTimeParseException e) {
                        System.err.println("Erreur : Format de date invalide. Utilisez YYYY-MM-DD.");
                    } catch (DateIncoherenteException e) {
                        System.err.println("Erreur de cohérence : " + e.getMessage());
                    }
                }
        );
    }
}