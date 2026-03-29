package gui;

import personnel.GestionPersonnel;

import javax.swing.JOptionPane;
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
}
