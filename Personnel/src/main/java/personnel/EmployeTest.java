package personnel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;

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

        gestion = GestionPersonnel.getGestionPersonnel();
        root = gestion.getRoot();

        ligueTest = gestion.addLigue(1, "Ligue de Test");

        // --- LIGNE 31 CORRIGÉE (6 arguments, dateDepart = null) ---
        employeTest = ligueTest.addEmploye("Dupont", "Jean", "jean@email.com", "mdp123", LocalDate.now(), null);
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

    // --- TESTS UNITAIRES AJOUTÉS (Mission 3) ---

    @Test
    void testDateArriveeParDefaut() {
        assertNotNull(employeTest.getDateArrivee());
        assertEquals(LocalDate.now(), employeTest.getDateArrivee());
        assertNull(employeTest.getDateDepart());
    }

    @Test
    void testSetDatesValides() {
        LocalDate arrivee = LocalDate.of(2020, 5, 15);
        LocalDate depart = LocalDate.of(2023, 10, 20);

        employeTest.setDateArrivee(arrivee);
        employeTest.setDateDepart(depart);

        assertEquals(arrivee, employeTest.getDateArrivee());
        assertEquals(depart, employeTest.getDateDepart());
    }

    @Test
    void testSetDateDepartIncoherente() {
        // employeTest est arrivé aujourd'hui (via setup)
        LocalDate departInvalide = LocalDate.of(2022, 1, 1); // Date antérieure

        assertThrows(DateIncoherenteException.class, () -> {
            employeTest.setDateDepart(departInvalide);
        });
    }

    @Test
    void testSetDateArriveeIncoherente() {
        LocalDate arriveeInitiale = LocalDate.of(2022, 1, 1);
        LocalDate departValide = LocalDate.of(2023, 1, 1);

        employeTest.setDateArrivee(arriveeInitiale);
        employeTest.setDateDepart(departValide);

        LocalDate arriveeIncoherente = LocalDate.of(2024, 1, 1); // Après le départ

        assertThrows(DateIncoherenteException.class, () -> {
            employeTest.setDateArrivee(arriveeIncoherente);
        });
    }

    @Test
    void testSetDateDepartNulle() {
        employeTest.setDateDepart(LocalDate.now().plusDays(1)); // Date valide
        assertNotNull(employeTest.getDateDepart());

        employeTest.setDateDepart(null); // On la retire
        assertNull(employeTest.getDateDepart());
    }

    @Test
    void testConstructeurIncoherente() throws SauvegardeImpossible {
        // CORRIGÉ : On utilise 'gestion' (comme le reste de ta classe)
        // au lieu de 'gestionPersonnel'
        Ligue ligue = gestion.addLigue("TestDates");

        LocalDate arrivee = LocalDate.of(2023, 1, 1);
        LocalDate depart = LocalDate.of(2022, 1, 1); // Départ avant arrivée

        // Teste si le constructeur (appelé par addEmploye) lève bien l'exception
        assertThrows(DateIncoherenteException.class, () -> {
            ligue.addEmploye("Date", "Test", "date@test.com", "mdp", arrivee, depart);
        });
    }
}