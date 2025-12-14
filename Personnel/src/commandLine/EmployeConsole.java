package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import commandLineMenus.ListOption;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import personnel.DateInvalide;
import personnel.Employe;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EmployeConsole 
{
	private Option afficher(final Employe employe)
	{
		return new Option("Afficher l'employé", "l", () -> {System.out.println(employe);});
	}

	ListOption<Employe> editerEmploye()
	{
		return (employe) -> editerEmploye(employe);		
	}

	Option editerEmploye(Employe employe)
	{
			Menu menu = new Menu("Gérer le compte " + employe.getNom(), "c");
			menu.add(afficher(employe));
			menu.add(changerNom(employe));
			menu.add(changerPrenom(employe));
			menu.add(changerMail(employe));
			menu.add(changerPassword(employe));
            menu.add(changerDateArrivee(employe));
            menu.add(changerDateDepart(employe));
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
    private Option changerDateArrivee(final Employe employe)
    {
        return new Option("Changer la date d'arrivée","da",
                () ->
                {
                    try
                    {
                        String strda = getString("Nouvelle date d'arrivée (yyyy-mm-dd): ");
                        employe.setDateArrivee(LocalDate.parse(strda));
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
    private Option changerDateDepart(final Employe employe)
    {
        return new Option("Changer la date de départ","dd",
                () ->
                {
                    try
                    {
                        String strdd = getString("Nouvelle date de départ (yyyy-mm-dd): ");
                        employe.setDateArrivee(LocalDate.parse(strdd));
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
}
