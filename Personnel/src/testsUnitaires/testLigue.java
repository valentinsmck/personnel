package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import personnel.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

class testLigue
{
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();

    /**
     * Vérifier si la création d'une ligue fonctionne.
     * @throws SauvegardeImpossible
     */
	@Test
	void createLigue() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		assertEquals("Fléchettes", ligue.getNom());
	}

    /**
     * Vérifier si la création d'un employé fonctionne.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
	@Test
	void addEmploye() throws SauvegardeImpossible, DateInvalide
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 10));
		assertEquals(employe, ligue.getEmployes().first());
	}

    /**
     * Vérifier si l'exception DateInvalide fonctionne concernant qu'une date d'arrivée soit au plus tard que la date actuelle.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void ExceptionDateInvalideDA() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        LocalDate dateArrivee = LocalDate.now().plusDays(1);
        assertThrows(DateInvalide.class, () ->
        {
            ligue.addEmploye("Laporte", "Jean", "j.laporte@gmail.com", "pass",dateArrivee,null);
        });
    }

    /**
     * Vérifier si l'exception fonctionne concernant la date de départ ne peut pas être avant la date d'arrivée.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void ExceptionDateInvalideDD() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        assertThrows(DateInvalide.class, () ->
        {
            ligue.addEmploye("Laporte", "Jean", "j.laporte@gmail.com", "pass",LocalDate.now(),LocalDate.now().minusDays(1));
        });
    }

    /**
     * Vérifier si le changement du nom de la ligue fonctionne.
     * @throws SauvegardeImpossible
     */
    @Test
    void TestsetNom() throws SauvegardeImpossible
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        ligue.setNom("Boxe");
        assertEquals("Boxe", ligue.getNom());
    }

    /**
     * Vérifier si cela retourne bien le nom de la ligue.
     * @throws SauvegardeImpossible
     */
    @Test
    void TestgetNom() throws SauvegardeImpossible
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        assertEquals(ligue.getNom(), "Fléchettes");
    }

    /**
     * Vérifier si le changement de l'administrateur fonctionne et retourne bien le nouveau administrateur.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestChangementAdmin() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Laporte", "Jean", "j.laporte@gmail.com", "pass",LocalDate.now(),LocalDate.now().plusDays(1));
        /**
         * Vérifier si le setter fonctionne
         */
        ligue.setAdministrateur(employe);
        /**
         * Vérifier si le getter fonctionne
         */
        assertEquals(employe, ligue.getAdministrateur());
    }

    /**
     * Vérifier si la suppression d'une ligue fonctionne.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestRemoveLigue() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        ligue.remove();
        assertFalse(gestionPersonnel.getLigues().contains(ligue));
    }

    /**
     * Vérifier si supprimer l'admin fonctionne.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestRemoveAdmin() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Laporte", "Jean", "j.laporte@gmail.com", "pass",LocalDate.now(),LocalDate.now().plusDays(1));
        ligue.setAdministrateur(employe);
        employe.remove();
        assertFalse(ligue.getEmployes().contains(employe));
    }
}
