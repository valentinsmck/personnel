package testsUnitaires; // 1. Correction du package

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

import personnel.*; // 2. Import des classes métier

class EmployeTest {

    protected GestionPersonnel gestion;
    protected Ligue ligueTest;
    protected Employe employeTest;
    protected Employe root;

    @BeforeEach
    void setUp() throws Exception {
        // Réinitialisation du Singleton
        Field instanceField = GestionPersonnel.class.getDeclaredField("gestionPersonnel");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        gestion = GestionPersonnel.getGestionPersonnel();
        root = gestion.getRoot();

        ligueTest = gestion.addLigue("Ligue de Test");

        // 3. Correction : Ajout des 2 arguments null pour les dates
        employeTest = ligueTest.addEmploye("Dupont", "Jean", "jean@email.com", "mdp123", null, null);
    }

    @Test
    void setNom() {
        employeTest.setNom("NouveauNom");
        assertEquals("NouveauNom", employeTest.getNom());
    }

    @Test
    void setPrenom() {
        employeTest.setPrenom("NouveauPrenom");
        assertEquals("NouveauPrenom", employeTest.getPrenom());
    }

    @Test
    void setMail() {
        employeTest.setMail("nouveau@mail.com");
        assertEquals("nouveau@mail.com", employeTest.getMail());
    }

    @Test
    void setPassword() {
        employeTest.setPassword("nouveauPass");
        assertTrue(employeTest.checkPassword("nouveauPass"));
        assertFalse(employeTest.checkPassword("mdp123"));
    }

    @Test
    void remove() {
        int nbEmployesAvant = ligueTest.getEmployes().size();

        employeTest.remove();

        assertEquals(nbEmployesAvant - 1, ligueTest.getEmployes().size());
        assertFalse(ligueTest.getEmployes().contains(employeTest));
    }
}