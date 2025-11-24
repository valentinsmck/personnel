package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import personnel.*;
import java.lang.reflect.Field;
import java.time.LocalDate;

class LigueTest
{
    GestionPersonnel gestion;
    Ligue ligue;

    @BeforeEach
    void setUp() throws Exception
    {
        // Réinitialisation du Singleton
        Field instanceField = GestionPersonnel.class.getDeclaredField("gestionPersonnel");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        gestion = GestionPersonnel.getGestionPersonnel();
        ligue = gestion.addLigue("Ligue de Test");
    }

    @Test
    void testSetNom()
    {
        ligue.setNom("Nouveau Nom");
        assertEquals("Nouveau Nom", ligue.getNom());
    }

    @Test
    void testSetAdministrateur()
    {
        Employe employe = ligue.addEmploye("Nom", "Prenom", "mail@test.com", "mdp", null, null);

        assertEquals(gestion.getRoot(), ligue.getAdministrateur());

        ligue.setAdministrateur(employe);
        assertEquals(employe, ligue.getAdministrateur());
    }

    // --- CORRECTION ICI : Ajout de "throws SauvegardeImpossible" ---
    @Test
    void testSetAdministrateurDroitsInsuffisants() throws SauvegardeImpossible
    {
        Ligue autreLigue = gestion.addLigue("Autre Ligue"); // Cette ligne peut lancer l'exception
        Employe employeAutreLigue = autreLigue.addEmploye("Intrus", "Paul", "intrus@test.com", "pass", null, null);

        assertThrows(DroitsInsuffisants.class, () -> {
            ligue.setAdministrateur(employeAutreLigue);
        });
    }

    @Test
    void testSuppressionEmploye()
    {
        Employe employe = ligue.addEmploye("A_Supprimer", "Test", "suppr@test.com", "mdp", null, null);

        assertTrue(ligue.getEmployes().contains(employe));

        employe.remove();

        assertFalse(ligue.getEmployes().contains(employe));
    }

    @Test
    void testChangementAdministrateur()
    {
        Employe adminPotentiel = ligue.addEmploye("Futur", "Admin", "admin@test.com", "mdp", null, null);
        ligue.setAdministrateur(adminPotentiel);
        assertEquals(adminPotentiel, ligue.getAdministrateur());
    }

    @Test
    void testSuppressionAdministrateur()
    {
        Employe admin = ligue.addEmploye("Admin", "Test", "admin@test.com", "mdp", null, null);

        ligue.setAdministrateur(admin);

        assertEquals(admin, ligue.getAdministrateur());

        admin.remove();

        assertEquals(gestion.getRoot(), ligue.getAdministrateur());
        assertFalse(ligue.getEmployes().contains(admin));
    }

    @Test
    void testAddEmploye()
    {
        Employe e = ligue.addEmploye("Dupont", "Jean", "j.dupont@mail.com", "mdp", LocalDate.now(), null);
        assertEquals(e, ligue.getEmployes().last());
        assertEquals(1, ligue.getEmployes().size());
    }

    @Test
    void testSuppressionLigue()
    {
        ligue.remove();
        assertFalse(gestion.getLigues().contains(ligue));
    }
}