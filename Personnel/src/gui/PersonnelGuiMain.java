package gui;

import personnel.Employe;
import personnel.GestionPersonnel;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

public class PersonnelGuiMain
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() ->
		{
			try
			{
				GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
				if (!authenticateRoot(gestionPersonnel))
					return;
				PersonnelFrame.launch(gestionPersonnel);
			}
			catch (RuntimeException e)
			{
				JOptionPane.showMessageDialog(
						null,
						"Impossible de lancer l'interface: " + e.getMessage(),
						"Erreur",
						JOptionPane.ERROR_MESSAGE
				);
			}
		});
	}

	private static boolean authenticateRoot(GestionPersonnel gestionPersonnel)
	{
		Employe root = gestionPersonnel.getRoot();
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
