package personnel;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Gestion du personnel. Un seul objet de cette classe existe.
 * Il n'est pas possible d'instancier directement cette classe, 
 * la méthode {@link #getGestionPersonnel getGestionPersonnel} 
 * le fait automatiquement et retourne toujours le même objet.
 * Dans le cas où {@link #sauvegarder()} a été appelé lors 
 * d'une exécution précédente, c'est l'objet sauvegardé qui est
 * retourné.
 */

public class GestionPersonnel implements Serializable
{
	/** Identifiant de serialisation Java (version de classe). */
	private static final long serialVersionUID = -105283113987886425L;
	private static GestionPersonnel gestionPersonnel = null;
	private SortedSet<Ligue> ligues;
	private Employe root;
	public final static int SERIALIZATION = 1, JDBC = 2, 
			TYPE_PASSERELLE = JDBC;
	private static Passerelle passerelle = TYPE_PASSERELLE == JDBC ? new jdbc.JDBC() : new serialisation.Serialization();	
	
	/**
	 * Retourne l'unique instance de cette classe.
	 * Crée cet objet s'il n'existe déjà.
	 * @return l'unique objet de type {@link GestionPersonnel}.
	 */
	
	public static GestionPersonnel getGestionPersonnel()
	{
		if (gestionPersonnel == null)
		{
			gestionPersonnel = passerelle.getGestionPersonnel();
			if (gestionPersonnel == null)
			{
				gestionPersonnel = new GestionPersonnel();
				try
				{
					gestionPersonnel.addRoot("root", "toor");
				}
				catch (SauvegardeImpossible e)
				{
					throw new IllegalStateException("Impossible de creer le root par defaut.", e);
				}
			}
		}
		return gestionPersonnel;
	}

	/**
	 * Constructeur interne (Singleton): initialise l'ensemble des ligues.
	 */
	public GestionPersonnel()
	{
		if (gestionPersonnel != null)
			throw new RuntimeException("Vous ne pouvez créer qu'une seuls instance de cet objet.");
		ligues = new TreeSet<>();
		gestionPersonnel = this;
	}
	
	/**
	 * Delegue la sauvegarde a la passerelle active (JDBC ou serialisation).
	 */
	public void sauvegarder() throws SauvegardeImpossible
	{
		passerelle.sauvegarderGestionPersonnel(this);
	}
	
	/**
	 * Retourne la ligue dont administrateur est l'administrateur,
	 * null s'il n'est pas un administrateur.
	 * @param administrateur l'administrateur de la ligue recherchée.
	 * @return la ligue dont administrateur est l'administrateur.
	 */
	
	public Ligue getLigue(Employe administrateur)
	{
		if (administrateur.estAdmin(administrateur.getLigue()))
			return administrateur.getLigue();
		else
			return null;
	}

	/**
	 * Retourne toutes les ligues enregistrées.
	 * @return toutes les ligues enregistrées.
	 */
	
	public SortedSet<Ligue> getLigues()
	{
		return Collections.unmodifiableSortedSet(ligues);
	}

	/**
	 * Cree et ajoute une ligue en persistance.
	 */
	public Ligue addLigue(String nom) throws SauvegardeImpossible
	{
		Ligue ligue = new Ligue(this, nom); 
		ligues.add(ligue);
		return ligue;
	}
	
	/**
	 * Ajoute une ligue deja connue en base (cas chargement JDBC).
	 */
	public Ligue addLigue(int id, String nom)
	{
		Ligue ligue = new Ligue(this, id, nom);
		ligues.add(ligue);
		return ligue;
	}

	/**
	 * Retire une ligue de la collection metier (appel interne apres suppression).
	 */
	void remove(Ligue ligue)
	{
		ligues.remove(ligue);
	}

	/**
	 * Supprime un employe via la passerelle.
	 */
	public void delete(Employe employe) throws SauvegardeImpossible
	{
		passerelle.delete(employe);
	}

	/**
	 * Supprime une ligue via la passerelle.
	 */
	public void delete(Ligue ligue) throws SauvegardeImpossible
	{
		passerelle.delete(ligue);
	}

	/**
	 * Insere une ligue et retourne l'id genere.
	 */
	int insert(Ligue ligue) throws SauvegardeImpossible
	{
		return passerelle.insert(ligue);
	}

	/**
	 * Insere un employe et retourne l'id genere.
	 */
	int insert(Employe employe) throws SauvegardeImpossible
	{
		return passerelle.insert(employe);
	}

	/**
	 * Met a jour une ligue existante.
	 */
	int update(Ligue ligue) throws SauvegardeImpossible
	{
		return passerelle.update(ligue);
	}

	/**
	 * Met a jour un employe existant.
	 */
    int update(Employe employe) throws SauvegardeImpossible
    {
        return passerelle.update(employe);
    }

	/**
	 * Retourne le root (super-utilisateur).
	 * @return le root.
	 */
	
	public Employe getRoot()
	{
		return root;
	}

	/**
	 * Crée le root à partir de son nom et de son mot de passe,
	 * puis l'affecte à la variable d'instance root.
	 * @param nom le nom du root.
	 * @param password le mot de passe du root.
	 */
	public void addRoot(String nom, String password) throws SauvegardeImpossible
	{
		try
		{
			this.root = new Employe(this, null, nom, "", "", password, null, null);
		}
		catch (DateInvalide e)
		{
			throw new IllegalStateException("Etat de date invalide pour la creation du root.", e);
		}
	}

	/**
	 * Crée le root à partir de données lues en base,
	 * puis l'affecte à la variable d'instance root.
	 */
	public void addRoot(int id, String nom, String prenom, String mail, String password,
			LocalDate dateArrivee, LocalDate dateDepart)
	{
		try
		{
			this.root = new Employe(this, id, null, nom, prenom, mail, password, dateArrivee, dateDepart);
		}
		catch (DateInvalide e)
		{
			throw new IllegalStateException("Impossible de charger le root depuis la base.", e);
		}
	}

	/**
	 * Recherche un compte (root ou employe) a partir d'un identifiant texte.
	 * Identifiants supportes: root, mail, nom, nom.prenom.
	 */
	public Employe findEmployeByIdentifiant(String identifiant)
	{
		if (identifiant == null)
			return null;
		String normalized = identifiant.trim();
		if (normalized.isEmpty())
			return null;

		if (matchesIdentifiant(root, normalized, true))
			return root;

		for (Ligue ligue : ligues)
			for (Employe employe : ligue.getEmployes())
				if (matchesIdentifiant(employe, normalized, false))
					return employe;
		return null;
	}

	/**
	 * Verifie si l'identifiant texte correspond a un employe donne.
	 */
	private boolean matchesIdentifiant(Employe employe, String identifiant, boolean includeRootAlias)
	{
		if (employe == null)
			return false;
		if (includeRootAlias && "root".equalsIgnoreCase(identifiant))
			return true;
		if (equalsIgnoreCaseSafe(employe.getMail(), identifiant))
			return true;
		if (equalsIgnoreCaseSafe(employe.getNom(), identifiant))
			return true;

		String nom = employe.getNom() == null ? "" : employe.getNom().trim();
		String prenom = employe.getPrenom() == null ? "" : employe.getPrenom().trim();
		String nomPrenom = nom.isEmpty() || prenom.isEmpty() ? "" : nom + "." + prenom;
		String prenomNom = nom.isEmpty() || prenom.isEmpty() ? "" : prenom + "." + nom;
		return nomPrenom.equalsIgnoreCase(identifiant) || prenomNom.equalsIgnoreCase(identifiant);
	}

	/**
	 * Comparaison de chaines insensitive a la casse avec gestion des null.
	 */
	private boolean equalsIgnoreCaseSafe(String left, String right)
	{
		if (left == null || right == null)
			return false;
		return left.trim().equalsIgnoreCase(right.trim());
	}

}
