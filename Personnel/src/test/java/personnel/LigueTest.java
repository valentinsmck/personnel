package personnel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

class LigueTest {

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
        ligueTest.setNom("Nouveau Nom de Ligue");
        assertEquals("Nouveau Nom de Ligue", ligueTest.getNom());
    }

    @Test
    void setAdministrateur() {
        assertEquals(root, ligueTest.getAdministrateur());
        ligueTest.setAdministrateur(employeTest);
        assertEquals(employeTest, ligueTest.getAdministrateur());
    }

    @Test
    void testSetAdministrateurDroitsInsuffisants() {
        Ligue ligueB = gestion.addLigue(2, "Ligue B");
        Employe employeEtranger = ligueB.addEmploye("Etranger", "Paul", "paul@mail.com", "pass");

        assertThrows(DroitsInsuffisants.class, () -> {
            ligueTest.setAdministrateur(employeEtranger);
        });
    }

    @Test
    void addEmploye() {
        Employe nouvelEmploye = ligueTest.addEmploye("Durand", "Alice", "alice@mail.com", "pass");

        assertEquals(2, ligueTest.getEmployes().size());
        assertTrue(ligueTest.getEmployes().contains(employeTest));
        assertTrue(ligueTest.getEmployes().contains(nouvelEmploye));
        assertEquals(ligueTest, nouvelEmploye.getLigue());
    }

    @Test
    void remove() {
        assertTrue(gestion.getLigues().contains(ligueTest));

        ligueTest.remove();

        assertFalse(gestion.getLigues().contains(ligueTest));
    }
}