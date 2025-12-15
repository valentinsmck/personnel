package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


import commandLineMenus.List;
import commandLineMenus.Menu;
import commandLineMenus.Option;


import personnel.*;

public class LigueConsole 
{
	private GestionPersonnel gestionPersonnel;
	private EmployeConsole employeConsole;

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
	
	private Option ajouterEmploye(final Ligue ligue)
	{
		return new Option("ajouter un employé", "a",
				() -> 
				{
                    try
                    {
                        String strda = getString("Nouvelle date d'arrivée (yyyy-mm-dd) : ");
                        String strdd = getString("Nouvelle date départ (yyyy-mm-dd) : ");
                        ligue.addEmploye(getString("nom : "), getString("prenom : "), getString("mail : "), getString("password : "), LocalDate.parse(strda),LocalDate.parse(strdd));
                    }
                    catch (DateTimeParseException e)
                    {
                        System.out.println("La date doit être au format yyyy-mm-dd. exemple : 2000-01-01");
                    }
                    catch (DateInvalide e)
                    {
                        System.out.println(e.getMessage());
                    }
                }
		);
    }
	
	private Menu gererEmployes(Ligue ligue)
	{
		Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
		menu.add(afficherEmployes(ligue));
		menu.add(ajouterEmploye(ligue));
		menu.add(selectionnerEmployes(ligue));
		menu.addBack("q");
		return menu;
	}
    private List<Employe> selectionnerEmployes(final Ligue ligue)
    {
        return new List<Employe>("Sélectionner un employé", "em",
                () -> new ArrayList<>(ligue.getEmployes()),
                (element) -> gererEmploye(element)
        );
    }

	private Option supprimerEmploye(final Employe employe)
	{
        return new Option("Supprimer", "r", () -> {employe.remove();});
	}

    private Menu gererEmploye(Employe employe)
    {
        Menu menu = new Menu("Gérer l'employé : " + employe.getNom(), "a");
        menu.add(supprimerEmploye(employe));
        menu.add(employeConsole.editerEmploye(employe));
        menu.addBack("q");
        return menu;
    }
	
	private List<Employe> changerAdministrateur(final Ligue ligue)
	{
		return null;
	}
	
	private Option supprimer(Ligue ligue)
	{
		return new Option("Supprimer", "d", () -> {ligue.remove();});
	}
	
}
