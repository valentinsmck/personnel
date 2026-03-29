import jdbc.JDBC;
import personnel.GestionPersonnel;

public class TestJDBCConnection {
    public static void main(String[] args) {
        System.out.println("=== Testing JDBC Connection ===");
        try {
            JDBC jdbc = new JDBC();
            System.out.println("✓ JDBC object created successfully");
            
            GestionPersonnel gp = jdbc.getGestionPersonnel();
            System.out.println("✓ GestionPersonnel loaded from database");
            
            int nbLigues = gp.getLigues().size();
            System.out.println("✓ Number of Ligues: " + nbLigues);
            
            if (gp.getRoot() != null) {
                System.out.println("✓ Root user found: " + gp.getRoot().getNom());
            } else {
                System.out.println("! No root user found in database");
            }
            
            System.out.println("\n=== CONNECTION SUCCESSFUL ===");
            jdbc.close();
            
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getClass().getSimpleName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
