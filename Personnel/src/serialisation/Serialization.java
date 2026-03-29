package serialisation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

/**
 * Implementation historique de la passerelle en serialisation Java.
 *
 * L'application courante utilise principalement JDBC. Cette classe est
 * conservee pour compatibilite et pour illustrer une autre strategie
 * de persistance.
 */
public class Serialization implements personnel.Passerelle
{
	/** Nom du fichier local contenant l'etat serialise. */
	private static final String FILE_NAME = "GestionPersonnel.srz";

	/**
	 * Charge l'instance serialisee du gestionnaire.
	 *
	 * @return l'objet charge, ou null si aucun fichier lisible n'est disponible.
	 */
	@Override
	public GestionPersonnel getGestionPersonnel()
	{
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME)))
		{
			return (GestionPersonnel) ois.readObject();
		}
		catch (IOException | ClassNotFoundException e)
		{
			return null;
		}
	}
	
	/**
	 * Sauvegarde le gestionnaire pour qu'il soit ouvert automatiquement 
	 * lors d'une exécution ultérieure du programme.
	 * @throws SauvegardeImpossible Si le support de sauvegarde est inaccessible.
	 */
	@Override
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME)))
		{
			oos.writeObject(gestionPersonnel);
		}
		catch (IOException e)
		{
			throw new SauvegardeImpossible(e);
		}
	}
	
	/**
	 * Non utilise en mode serialisation: conserve la signature de la passerelle.
	 */
	@Override
	public int insert(Ligue ligue) throws SauvegardeImpossible
	{
		return -1;
	}

	/**
	 * Non utilise en mode serialisation: conserve la signature de la passerelle.
	 */
	@Override
	public int insert(Employe employe) throws SauvegardeImpossible
	{
		return -1;
	}

	/**
	 * Non utilise en mode serialisation: conserve la signature de la passerelle.
	 */
	@Override
	public int update(Ligue ligue) throws SauvegardeImpossible
	{
		return -1;
	}

	/**
	 * Non utilise en mode serialisation: conserve la signature de la passerelle.
	 */
	@Override
	public int delete(Ligue ligue) throws SauvegardeImpossible
	{
		return 0;
	}

	/**
	 * Non utilise en mode serialisation: conserve la signature de la passerelle.
	 */
	@Override
	public int delete(Employe employe) throws SauvegardeImpossible
	{
		return 0;
	}

	/**
	 * Non utilise en mode serialisation: conserve la signature de la passerelle.
	 */
	@Override
	public int update(Employe employe) throws SauvegardeImpossible
	{
		return 0;
	}
}
