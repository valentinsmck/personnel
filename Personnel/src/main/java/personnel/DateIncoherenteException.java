package personnel;

public class DateIncoherenteException extends RuntimeException {

    private static final long serialVersionUID = 1L; // Ajouté pour la sérialisation

    public DateIncoherenteException(String message) {
        super(message);
    }
}