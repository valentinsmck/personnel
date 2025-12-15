package testsUnitaires;

import org.junit.jupiter.api.Test;
import personnel.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class testEmploye
{
    GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();

    /**
     * Vérifier si retourne le nom de l'employé.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestgetNom() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 10));
        assertEquals(employe.getNom(),"Bouchard");
    }

    /**
     * Vérifier si le setter nom fonctionne.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestsetNom() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 10));
        employe.setNom("Valentin");
        assertEquals(employe.getNom(),"Valentin");
    }

    /**
     * Vérifier si retourne le prénom de l'employé.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestgetPrenom() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 10));
        assertEquals(employe.getPrenom(),"Gérard");
    }

    /**
     * Vérifier si le setter prénom fonctionne.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestsetPrenom() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 10));
        employe.setPrenom("Meka");
        assertEquals(employe.getPrenom(),"Meka");
    }

    /**
     * Vérifier si retourne le mail de l'employé.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestgetMail() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 10));
        assertEquals(employe.getMail(),"g.bouchard@gmail.com");
    }

    /**
     * Vérifier si le setter mail fonctionne.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestsetMail() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 10));
        employe.setMail("g.bouchard@gmail.com");
        assertEquals(employe.getMail(),"g.bouchard@gmail.com");
    }

    /**
     * Vérifier si le setter password fonctionne.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestsetPassword() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 10));
        employe.setPassword("qwerty");
        assertEquals(employe.checkPassword("qwerty"),true);
    }

    /**
     * Vérifier si retourne la ligue de l'employé.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestgetLigue() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 10));
        assertEquals(employe.getLigue(),ligue);
    }

    /**
     * Vérifier si retourne la date d'arrivée de l'employé.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestgetDateArrivee() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 10));
        assertEquals(employe.getDateArrivee(),LocalDate.of(2006, 2, 10));
    }

    /**
     * Vérifier si le setter dateArrivee fonctionne.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestsetDateArrivee() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 12));
        employe.setDateArrivee(LocalDate.of(2006, 2, 11));
        assertEquals(employe.getDateArrivee(),LocalDate.of(2006, 2, 11));
    }

    /**
     * Vérifier si retourne la date de départ de l'employé.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestgetDateDepart() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 11));
        assertEquals(employe.getDateDepart(),LocalDate.of(2006, 2, 11));
    }

    /**
     * Vérifier si le setter dateDepart fonctionne.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestsetDateDepart() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 12));
        employe.setDateDepart(LocalDate.of(2006, 2, 14));
        assertEquals(employe.getDateDepart(),LocalDate.of(2006, 2, 14));
    }

    /**
     * Vérifier si la suppression d'un employé fonctionne.
     * @throws SauvegardeImpossible
     * @throws DateInvalide
     */
    @Test
    void TestRemoveEmploye() throws SauvegardeImpossible,DateInvalide
    {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2006, 2, 10),LocalDate.of(2006, 2, 12));
        employe.remove();
        assertFalse(ligue.getEmployes().contains(employe));
    }
}
