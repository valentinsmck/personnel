package gui;

import commandLine.PersonnelConsole;
import personnel.GestionPersonnel;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

public class PersonnelApp
{
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
				if (!authenticateRoot(gestionPersonnel))
					return;
				PersonnelFrame.launch(gestionPersonnel);
			}
			catch (Exception e)
			{
				System.err.println("Erreur GUI: " + e.getMessage());
				System.exit(1);
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
		System.out.println("=".repeat(60));
		System.out.println("  PERSONNEL - Mode Console");
		System.out.println("  (Pas d'interface graphique disponible)");
		System.out.println("=".repeat(60));
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

	private static boolean authenticateRoot(GestionPersonnel gestionPersonnel)
	{
		personnel.Employe root = gestionPersonnel.getRoot();
		while (true)
		{
			JPasswordField passwordField = new JPasswordField();
			int result = JOptionPane.showConfirmDialog(
					null,
					passwordField,
					"Connexion root - entrez le mot de passe",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE
			);

			if (result != JOptionPane.OK_OPTION)
				return false;

			String password = new String(passwordField.getPassword());
			if (root.checkPassword(password))
				return true;

			JOptionPane.showMessageDialog(
					null,
					"Mot de passe incorrect.",
					"Authentification",
					JOptionPane.WARNING_MESSAGE
			);
		}
	}

}
