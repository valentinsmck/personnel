package testsUnitaires;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

import personnel.*;

class EmployeTest {

    protected GestionPersonnel gestion;
    protected Ligue ligueTest;
    protected Employe employeTest;
    protected Employe root;

    @BeforeEach
    void setUp() throws Exception {
        // 1. Réinitialisation du Singleton pour avoir des tests propres
        Field instanceField = GestionPersonnel.class.getDeclaredField("gestionPersonnel");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        gestion = GestionPersonnel.getGestionPersonnel();
        root = gestion.getRoot();

        ligueTest = gestion.addLigue("Ligue de Test");

        // 2. Initialisation d'un employé avec une date d'arrivée (Aujourd'hui) et pas de départ
        employeTest = ligueTest.addEmploye("Dupont", "Jean", "jean@email.com", "mdp123", LocalDate.now(), null);
    }

    // --- TESTS CLASSIQUES (Mission de base) ---

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

    // --- TESTS DES DATES (Mission 3) ---

    @Test
    void testDateArriveeParDefaut() {
        // Vérifie que la date mise dans le setUp est bien là
        assertNotNull(employeTest.getDateArrivee());
        assertEquals(LocalDate.now(), employeTest.getDateArrivee());
        assertNull(employeTest.getDateDepart());
    }

    @Test
    void testSetDatesValides() throws DateIncoherenteException {
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
        // On essaie de le faire partir HIER (Incohérent)
        LocalDate departInvalide = LocalDate.now().minusDays(1);

        assertThrows(DateIncoherenteException.class, () -> {
            employeTest.setDateDepart(departInvalide);
        });
    }

    @Test
    void testSetDateArriveeIncoherente() throws DateIncoherenteException {
        LocalDate arriveeInitiale = LocalDate.of(2022, 1, 1);
        LocalDate departValide = LocalDate.of(2023, 1, 1);
        employeTest.setDateArrivee(arriveeInitiale);
        employeTest.setDateDepart(departValide);

        // On essaie de changer l'arrivée pour qu'elle soit APRÈS le départ (Incohérent)
        LocalDate arriveeIncoherente = LocalDate.of(2024, 1, 1);

        assertThrows(DateIncoherenteException.class, () -> {
            employeTest.setDateArrivee(arriveeIncoherente);
        });
    }

    @Test
    void testConstructeurIncoherente() throws SauvegardeImpossible {
        Ligue ligue = gestion.addLigue("TestDates");
        LocalDate arrivee = LocalDate.of(2023, 1, 1);
        LocalDate depart = LocalDate.of(2022, 1, 1); // Départ AVANT arrivée

        // Teste si la création directe échoue bien
        assertThrows(DateIncoherenteException.class, () -> {
            ligue.addEmploye("Date", "Test", "date@test.com", "mdp", arrivee, depart);
        });
    }
}