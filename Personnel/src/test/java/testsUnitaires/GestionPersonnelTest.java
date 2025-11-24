package testsUnitaires;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

import personnel.*;

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

        gestion = GestionPersonnel.getGestionPersonnel();
        root = gestion.getRoot();

        // addLigue peut lancer SauvegardeImpossible, mais setUp() throws Exception, donc c'est bon ici
        ligueTest = gestion.addLigue("Ligue de Test");

        employeTest = ligueTest.addEmploye("Dupont", "Jean", "jean@email.com", "mdp123", null, null);
    }

    @Test
    void testChangementAdminLigueSucces() {
        ligueTest.setAdministrateur(employeTest);
        assertEquals(employeTest, ligueTest.getAdministrateur());
    }

    // --- CORRECTION ICI : Ajout de "throws SauvegardeImpossible" ---
    @Test
    void testChangementAdminLigueEchecDroitsInsuffisants() throws SauvegardeImpossible {
        Ligue ligueB = gestion.addLigue("Ligue B"); // Cette ligne nécessitait le throws
        Employe employeLigueB = ligueB.addEmploye("Autre", "Employe", "autre@mail.com", "pass", null, null);

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