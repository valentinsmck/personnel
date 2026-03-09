package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import personnel.*;

public class JDBC implements Passerelle
{
    Connection connection;

    public JDBC()
    {
        try
        {
            Class.forName(Credentials.getDriverClassName());
            connection = DriverManager.getConnection(Credentials.getUrl(), Credentials.getUser(), Credentials.getPassword());
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Pilote JDBC non installé.");
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }

    @Override
    public GestionPersonnel getGestionPersonnel()
    {
        GestionPersonnel gestionPersonnel = new GestionPersonnel();
        try
        {
            String requete = "SELECT * FROM LIGUE";
            Statement instruction = connection.createStatement();
            ResultSet ligues = instruction.executeQuery(requete);

            String reqEmp = "SELECT * FROM EMPLOYE WHERE id_ligue = ?";
            PreparedStatement psEmp = connection.prepareStatement(reqEmp);

            while (ligues.next())
            {
                int idLigue = ligues.getInt("id_ligue");
                Ligue ligue = gestionPersonnel.addLigue(idLigue, ligues.getString("nom_ligue"));

                psEmp.setInt(1, idLigue);
                try (ResultSet rsEmp = psEmp.executeQuery())
                {
                    while (rsEmp.next())
                    {
                        int idEmp = rsEmp.getInt("id_employe");
                        String role = rsEmp.getString("rôle_employe");

                        Employe e = new Employe(gestionPersonnel,
                                idEmp,
                                ligue,
                                rsEmp.getString("nom_employe"),
                                rsEmp.getString("prenom_employe"),
                                rsEmp.getString("mail_employe"),
                                rsEmp.getString("password_employe"),
                                rsEmp.getDate("date_arrivee_employe") != null ? rsEmp.getDate("date_arrivee_employe").toLocalDate() : null,
                                rsEmp.getString("date_départ_employe") != null && !rsEmp.getString("date_départ_employe").isEmpty()
                                        ? LocalDate.parse(rsEmp.getString("date_départ_employe")) : null
                        );
                        ligue.add(e);

                        if ("admin".equalsIgnoreCase(role))
                        {
                            ligue.setAdministrateurFromJDBC(e);
                        }
                    }
                }
            }

            requete = "SELECT * FROM EMPLOYE WHERE id_ligue IS NULL LIMIT 1";
            ResultSet roots = instruction.executeQuery(requete);
            if (roots.next())
            {
                gestionPersonnel.addRoot(
                        roots.getInt("id_employe"),
                        roots.getString("nom_employe"),
                        roots.getString("prenom_employe"),
                        roots.getString("mail_employe"),
                        roots.getString("password_employe"),
                        roots.getDate("date_arrivee_employe") != null ? roots.getDate("date_arrivee_employe").toLocalDate() : null,
                        roots.getString("date_départ_employe") != null && !roots.getString("date_départ_employe").isEmpty()
                                ? LocalDate.parse(roots.getString("date_départ_employe")) : null);
            }
            else
            {
                gestionPersonnel.addRoot("root", "toor");
            }
        }
        catch (SQLException | SauvegardeImpossible | DateInvalide e)
        {
            System.out.println(e);
        }
        return gestionPersonnel;
    }

    @Override
    public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible
    {
        close();
    }

    public void close() throws SauvegardeImpossible
    {
        try
        {
            if (connection != null)
                connection.close();
        }
        catch (SQLException e)
        {
            throw new SauvegardeImpossible(e);
        }
    }

    @Override
    public int insert(Employe employe) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO EMPLOYE (nom_employe, prenom_employe, mail_employe, password_employe, date_arrivee_employe, date_départ_employe, id_ligue, rôle_employe) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, employe.getNom());
            ps.setString(2, employe.getPrenom());
            ps.setString(3, employe.getMail());
            ps.setString(4, employe.getPassword());
            ps.setObject(5, employe.getDateArrivee());
            ps.setString(6, employe.getDateDepart() != null ? employe.getDateDepart().toString() : null);

            if (employe.getLigue() != null)
                ps.setInt(7, employe.getLigue().getId());
            else
                ps.setNull(7, java.sql.Types.INTEGER);

            ps.setString(8, (employe.getLigue() != null && employe.estAdmin(employe.getLigue())) ? "admin" : "employe");

            ps.executeUpdate();
            ResultSet id = ps.getGeneratedKeys();
            id.next();
            return id.getInt(1);
        }
        catch (SQLException e)
        {
            throw new SauvegardeImpossible(e);
        }
    }

    @Override
    public int insert(Ligue ligue) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO LIGUE (nom_ligue) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ligue.getNom());
            ps.executeUpdate();
            ResultSet id = ps.getGeneratedKeys();
            id.next();
            return id.getInt(1);
        }
        catch (SQLException e)
        {
            throw new SauvegardeImpossible(e);
        }
    }

    @Override
    public int update(Ligue ligue) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement("UPDATE LIGUE SET nom_ligue = ? WHERE id_ligue = ?");
            ps.setString(1, ligue.getNom());
            ps.setInt(2, ligue.getId());
            return ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new SauvegardeImpossible(e);
        }
    }

    @Override
    public int update(Employe employe) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE EMPLOYE SET nom_employe = ?, prenom_employe = ?, mail_employe = ?, password_employe = ?, date_arrivee_employe = ?, date_départ_employe = ?, id_ligue = ?, rôle_employe = ? WHERE id_employe = ?"
            );
            ps.setString(1, employe.getNom());
            ps.setString(2, employe.getPrenom());
            ps.setString(3, employe.getMail());
            ps.setString(4, employe.getPassword());
            ps.setObject(5, employe.getDateArrivee());
            ps.setString(6, employe.getDateDepart() != null ? employe.getDateDepart().toString() : null);

            if (employe.getLigue() != null)
                ps.setInt(7, employe.getLigue().getId());
            else
                ps.setNull(7, java.sql.Types.INTEGER);

            ps.setString(8, (employe.getLigue() != null && employe.estAdmin(employe.getLigue())) ? "admin" : "employe");
            ps.setInt(9, employe.getId());

            return ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new SauvegardeImpossible(e);
        }
    }

    @Override
    public int delete(Employe employe) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM EMPLOYE WHERE id_employe = ?");
            ps.setInt(1, employe.getId());
            return ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new SauvegardeImpossible(e);
        }
    }
}