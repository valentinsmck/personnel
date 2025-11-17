package personnel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

class GestionPersonnelTest {

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
    void testChangementAdminLigueSucces() {
        ligueTest.setAdministrateur(employeTest);
        assertEquals(employeTest, ligueTest.getAdministrateur());
    }

    @Test
    void testChangementAdminLigueEchecDroitsInsuffisants() {
        Ligue ligueB = gestion.addLigue(2, "Ligue B");
        Employe employeLigueB = ligueB.addEmploye("Autre", "Employe", "autre@mail.com", "pass");

        assertThrows(DroitsInsuffisants.class, () -> {
            ligueTest.setAdministrateur(employeLigueB);
        });
    }

    @Test
    void testSuppressionEmployeSimple() {
        assertTrue(ligueTest.getEmployes().contains(employeTest));
        int nbEmployesAvant = ligueTest.getEmployes().size();

        employeTest.remove();

        assertFalse(ligueTest.getEmployes().contains(employeTest));
        assertEquals(nbEmployesAvant - 1, ligueTest.getEmployes().size());
    }

    @Test
    void testSuppressionEmployeQuiEstAdmin() {
        ligueTest.setAdministrateur(employeTest);
        assertEquals(employeTest, ligueTest.getAdministrateur());

        employeTest.remove();

        assertEquals(root, ligueTest.getAdministrateur());
        assertFalse(ligueTest.getEmployes().contains(employeTest));
    }

    @Test
    void testSuppressionRootImpossible() {
        assertThrows(ImpossibleDeSupprimerRoot.class, () -> {
            root.remove();
        });
    }

    @Test
    void remove() {
        assertTrue(gestion.getLigues().contains(ligueTest));

        ligueTest.remove();

        assertFalse(gestion.getLigues().contains(ligueTest));
    }
}