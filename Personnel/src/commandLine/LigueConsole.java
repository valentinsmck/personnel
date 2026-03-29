package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


import commandLineMenus.List;
import commandLineMenus.Menu;
import commandLineMenus.Option;


import personnel.*;

/**
 * Console dediee a la gestion des ligues et de leurs employes.
 */
public class LigueConsole 
{
	private GestionPersonnel gestionPersonnel;
	private EmployeConsole employeConsole;

	/** Injecte les dependances de la console ligue. */
	public LigueConsole(GestionPersonnel gestionPersonnel, EmployeConsole employeConsole)
	{
		this.gestionPersonnel = gestionPersonnel;
		this.employeConsole = employeConsole;
	}

	/** Menu principal de gestion des ligues en CLI. */
	Menu menuLigues()
	{
		Menu menu = new Menu("Gérer les ligues", "l");
		menu.add(afficherLigues());
		menu.add(ajouterLigue());
		menu.add(selectionnerLigue());
		menu.addBack("q");
		return menu;
	}

	/** Affiche la liste brute des ligues. */
	private Option afficherLigues()
	{
		return new Option("Afficher les ligues", "l", () -> {System.out.println(gestionPersonnel.getLigues());});
	}

	/** Affiche les details de la ligue selectionnee. */
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

	/** Affiche tous les employes d'une ligue. */
	private Option afficherEmployes(final Ligue ligue)
	{
		return new Option("Afficher les employes", "l", () -> {System.out.println(ligue.getEmployes());});
	}

	/** Cree une ligue en demandant son nom en console. */
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
	
	/** Menu d'edition d'une ligue precise. */
	private Menu editerLigue(Ligue ligue)
	{
		Menu menu = new Menu("Editer " + ligue.getNom());
		menu.add(afficher(ligue));
		menu.add(gererEmployes(ligue));
		menu.add(changerNom(ligue));
		menu.add(supprimer(ligue));
		menu.addBack("q");
		return menu;
	}

	/** Renomme la ligue selectionnee. */
	private Option changerNom(final Ligue ligue)
	{
		return new Option("Renommer", "r", 
				() ->
				{
					try
					{
						ligue.setNom(getString("Nouveau nom : "));
					}
					catch (SauvegardeImpossible e)
					{
						System.out.println(e.getMessage());
					}
				});
	}

	/** Liste interactive permettant de choisir une ligue. */
	private List<Ligue> selectionnerLigue()
	{
		return new List<Ligue>("Sélectionner une ligue", "e", 
				() -> new ArrayList<>(gestionPersonnel.getLigues()),
				(element) -> editerLigue(element)
				);
	}
	
	/** Ajoute un employe dans la ligue avec saisie des champs en console. */
	private Option ajouterEmploye(final Ligue ligue)
	{
		return new Option("ajouter un employé", "a",
				() -> 
				{
                    try
                    {
                        String strda = getString("Nouvelle date d'arrivée (yyyy-mm-dd) : ");
                        LocalDate dateArrivee = LocalDate.parse(strda);
                        String strdd = getString("Nouvelle date départ (yyyy-mm-dd) : ");
                        LocalDate dateDepart = LocalDate.parse(strdd);
                        ligue.addEmploye(getString("nom : "), getString("prenom : "), getString("mail : "), getString("password : "), dateArrivee, dateDepart);
                    }
                    catch (DateTimeParseException e)
                    {
                        System.out.println("La date doit être au format yyyy-mm-dd. exemple : 2000-01-01");
                    }
                    catch (DateInvalide e)
                    {
                        System.out.println(e.getMessage());
                    }
                    catch(SauvegardeImpossible exception){
                        System.err.println("Impossible de sauvegarder cette ligue");
                    }
                }
		);
    }
	
	/** Menu de gestion des employes pour une ligue. */
	private Menu gererEmployes(Ligue ligue)
	{
		Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
		menu.add(afficherEmployes(ligue));
		menu.add(ajouterEmploye(ligue));
		menu.add(selectionnerEmployes(ligue));
		menu.addBack("q");
		return menu;
	}

    /** Liste interactive de selection d'employe. */
    private List<Employe> selectionnerEmployes(final Ligue ligue)
    {
        return new List<Employe>("Sélectionner un employé", "em",
                () -> new ArrayList<>(ligue.getEmployes()),
                (element) -> gererEmploye(element)
        );
    }

	/** Supprime un employe cible. */
	private Option supprimerEmploye(final Employe employe)
	{
		return new Option("Supprimer", "r", () ->
		{
			try
			{
				employe.remove();
			}
			catch (SauvegardeImpossible e)
			{
				System.out.println(e.getMessage());
			}
		});
	}

		/** Menu de gestion d'un employe. */
    private Menu gererEmploye(Employe employe)
    {
        Menu menu = new Menu("Gérer l'employé : " + employe.getNom(), "a");
        menu.add(supprimerEmploye(employe));
        menu.add(employeConsole.editerEmploye(employe));
        menu.add(changerAdministrateur(employe));
        menu.addBack("q");
        return menu;
    }
	
	/** Defini l'employe cible comme administrateur de sa ligue. */
	private Option changerAdministrateur(Employe employe)
	{
        Ligue ligue = employe.getLigue();
		return new Option("Changer Administrateur","ad", () ->
		{
			try
			{
				ligue.setAdministrateur(employe);
				System.out.println(ligue.getAdministrateur().getNom() + " est à présent l'administrateur de la ligue");
			}
			catch (SauvegardeImpossible e)
			{
				System.out.println(e.getMessage());
			}
		});
	}
	
	/** Supprime la ligue selectionnee. */
	private Option supprimer(Ligue ligue)
	{
		return new Option("Supprimer", "d", () ->
		{
			try
			{
				ligue.remove();
			}
			catch (SauvegardeImpossible e)
			{
				System.out.println(e.getMessage());
			}
		});
	}
	
}
