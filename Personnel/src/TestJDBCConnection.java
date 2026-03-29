import jdbc.JDBC;
import personnel.GestionPersonnel;

/**
 * Petit utilitaire de diagnostic pour verifier la connexion JDBC.
 *
 * Ce programme n'est pas un test unitaire JUnit: il sert de smoke test
 * manuel pour confirmer que la base est joignable et chargeable.
 */
public class TestJDBCConnection {
	/** Lance le diagnostic de connexion JDBC et affiche les informations clefs. */
    public static void main(String[] args) {
        System.out.println("=== Testing JDBC Connection ===");
        try {
            JDBC jdbc = new JDBC();
            System.out.println("[OK] JDBC object created successfully");
            
            GestionPersonnel gp = jdbc.getGestionPersonnel();
            System.out.println("[OK] GestionPersonnel loaded from database");
            
            int nbLigues = gp.getLigues().size();
            System.out.println("[OK] Number of Ligues: " + nbLigues);
            
            if (gp.getRoot() != null) {
                System.out.println("[OK] Root user found: " + gp.getRoot().getNom());
            } else {
                System.out.println("[WARN] No root user found in database");
            }
            
            System.out.println("\n=== CONNECTION SUCCESSFUL ===");
            jdbc.close();
            
        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getClass().getSimpleName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
