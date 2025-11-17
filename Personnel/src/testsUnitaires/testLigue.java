package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import personnel.*;

import java.time.LocalDate;
import java.lang.reflect.Field;

class testLigue
{
    GestionPersonnel gestionPersonnel;

    @BeforeEach
    void setUp() throws Exception {
        Field instanceField = GestionPersonnel.class.getDeclaredField("gestionPersonnel");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        gestionPersonnel = GestionPersonnel.getGestionPersonnel();
    }

    @Test
    void createLigue() throws SauvegardeImpossible
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        assertEquals("Fléchettes", ligue.getNom());
    }

    @Test
    void addEmploye() throws SauvegardeImpossible
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");

        // --- LIGNE CORRIGÉE (on ajoute la date de départ à null) ---
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.now(), null);

        assertEquals(employe, ligue.getEmployes().first());
    }

    // --- TESTS UNITAIRES AJOUTÉS (Mission 3) ---

    @Test
    void testDateArriveeParDefaut() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("TestDates");
        // On appelle avec 6 arguments (date départ nulle)
        Employe employe = ligue.addEmploye("Date", "Test", "date@test.com", "mdp", LocalDate.now(), null);

        assertNotNull(employe.getDateArrivee());
        assertEquals(LocalDate.now(), employe.getDateArrivee());
        assertNull(employe.getDateDepart());
    }

    @Test
    void testSetDatesValides() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("TestDates");
        Employe employe = ligue.addEmploye("Date", "Test", "date@test.com", "mdp", LocalDate.now(), null);

        LocalDate arrivee = LocalDate.of(2020, 5, 15);
        LocalDate depart = LocalDate.of(2023, 10, 20);

        employe.setDateArrivee(arrivee);
        employe.setDateDepart(depart);

        assertEquals(arrivee, employe.getDateArrivee());
        assertEquals(depart, employe.getDateDepart());
    }

    @Test
    void testSetDateDepartIncoherente() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("TestDates");
        Employe employe = ligue.addEmploye("Date", "Test", "date@test.com", "mdp", LocalDate.of(2023, 1, 1), null);

        LocalDate depart = LocalDate.of(2022, 1, 1); // Antérieure à l'arrivée

        // Doit lever une exception
        assertThrows(DateIncoherenteException.class, () -> {
            employe.setDateDepart(depart);
        });
    }

    @Test
    void testSetDateArriveeIncoherente() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("TestDates");
        Employe employe = ligue.addEmploye("Date", "Test", "date@test.com", "mdp", LocalDate.now(), null);

        LocalDate arriveeInitiale = LocalDate.of(2022, 1, 1);
        LocalDate departValide = LocalDate.of(2023, 1, 1);

        employe.setDateArrivee(arriveeInitiale);
        employe.setDateDepart(departValide);

        LocalDate arriveeIncoherente = LocalDate.of(2024, 1, 1);

        assertThrows(DateIncoherenteException.class, () -> {
            employe.setDateArrivee(arriveeIncoherente);
        });
    }

    @Test
    void testSetDateDepartNulle() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("TestDates");
        Employe employe = ligue.addEmploye("Date", "Test", "date@test.com", "mdp", LocalDate.now(), null);

        employe.setDateDepart(LocalDate.now().plusDays(1));
        assertNotNull(employe.getDateDepart());

        employe.setDateDepart(null);
        assertNull(employe.getDateDepart());
    }

    @Test
    void testConstructeurIncoherente() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("TestDates");
        LocalDate arrivee = LocalDate.of(2023, 1, 1);
        LocalDate depart = LocalDate.of(2022, 1, 1); // Départ avant arrivée

        // Doit lever une exception dès la création
        assertThrows(DateIncoherenteException.class, () -> {
            ligue.addEmploye("Date", "Test", "date@test.com", "mdp", arrivee, depart);
        });
    }
}