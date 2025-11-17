package personnel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class EmployeTest {

    protected GestionPersonnel gestion;
    protected Ligue ligueTest;
    protected Employe employeTest;
    protected Employe root;

    @BeforeEach
    void setUp() throws Exception {
        Field instanceField = GestionPersonnel.class.getDeclaredField("gestionPersonnel");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        gestion = new GestionPersonnel();
        root = gestion.getRoot();

        ligueTest = gestion.addLigue(1, "Ligue de Test");

        employeTest = ligueTest.addEmploye("Dupont", "Jean", "jean@email.com", "mdp123");
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