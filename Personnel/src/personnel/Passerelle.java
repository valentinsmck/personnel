package personnel;

/**
 * Contrat d'acces aux donnees (persistence).
 *
 * Terme technique: une passerelle (gateway) isole la couche metier
 * des details de stockage (JDBC, serialisation, etc.).
 */
public interface Passerelle 
{
	/** Charge l'etat global du gestionnaire depuis le support. */
	public GestionPersonnel getGestionPersonnel();
	/** Sauvegarde l'etat global du gestionnaire. */
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel)  throws SauvegardeImpossible;
	/** Insere une ligue et retourne son id. */
	public int insert(Ligue ligue) throws SauvegardeImpossible;
	/** Insere un employe et retourne son id. */
	public int insert(Employe employe) throws SauvegardeImpossible;
	/** Met a jour une ligue existante. */
	public int update(Ligue ligue) throws SauvegardeImpossible;
	/** Supprime une ligue existante. */
	public int delete(Ligue ligue) throws SauvegardeImpossible;
	/** Supprime un employe existant. */
	public int delete(Employe employe) throws SauvegardeImpossible;
	/** Met a jour un employe existant. */
	public int update(Employe employe) throws SauvegardeImpossible;
}
