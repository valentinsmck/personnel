package gui;

import commandLine.PersonnelConsole;
import personnel.GestionPersonnel;

import javax.swing.SwingUtilities;

public class PersonnelApp
{
	private static final String LINE = "============================================================";

	public static void main(String[] args)
	{
		if (forceCliMode(args) || !canRunSwing())
		{
			launchCLI();
			return;
		}

		SwingUtilities.invokeLater(() ->
		{
			try
			{
				GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
				PersonnelFrame.launch(gestionPersonnel);
			}
			catch (Exception e)
			{
				System.err.println("Erreur GUI: " + e.getMessage());
				launchCLI();
			}
		});
	}

	private static boolean canRunSwing()
	{
		try
		{
			javax.swing.JFrame test = new javax.swing.JFrame();
			test.dispose();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	private static boolean forceCliMode(String[] args)
	{
		for (String arg : args)
			if ("--cli".equalsIgnoreCase(arg))
				return true;
		return false;
	}

	private static void launchCLI()
	{
		System.out.println(LINE);
		System.out.println("  PERSONNEL - Mode Console");
		System.out.println("  (Pas d'interface graphique disponible)");
		System.out.println(LINE);
		System.out.println();

		try
		{
			PersonnelConsole.main(new String[]{});
		}
		catch (Exception e)
		{
			System.err.println("❌ Erreur: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

}
