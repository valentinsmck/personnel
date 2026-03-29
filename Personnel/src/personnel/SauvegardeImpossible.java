package personnel;

/**
 * Exception levée lorsqu'il est impossible de sauvegarder le gestionnaire.
 */

public class SauvegardeImpossible extends Exception
{
	/** Identifiant de serialisation Java. */
	private static final long serialVersionUID = 6651919630441855001L;	
	/** Cause technique d'origine (SQL, IO, etc.). */
	Exception exception;
	
	/**
	 * Cree une exception metier en encapsulant la cause technique.
	 */
	public SauvegardeImpossible(Exception exception)
	{
		this.exception = exception;
	}
	
	/**
	 * Affiche la pile de l'exception puis la cause racine.
	 */
	@Override
	public void printStackTrace() 
	{
			super.printStackTrace();
			System.err.println("Causé par : ");
			exception.printStackTrace();			
	}
}
