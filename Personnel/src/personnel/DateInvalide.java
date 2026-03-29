package personnel;

/**
 * Exception metier signalee quand une date viole les regles de gestion.
 */
public class DateInvalide extends Exception
{
	/** Cree l'exception avec un message explicite pour l'utilisateur. */
    public DateInvalide(String message)
    {
        super(message);
    }
}