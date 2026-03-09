package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
			String requete = "select * from LIGUE";
			Statement instruction = connection.createStatement();
			ResultSet ligues = instruction.executeQuery(requete);

			String reqEmp = "select * from EMPLOYE where id_ligue = ?";
			PreparedStatement psEmp = connection.prepareStatement(reqEmp);

			while (ligues.next())
			{
				int idLigue = ligues.getInt("id_ligue");
				Ligue ligue = gestionPersonnel.addLigue(idLigue, ligues.getString("nom_ligue"));

				// On charge les employés pour cette ligue
				psEmp.setInt(1, idLigue);
				try (ResultSet rsEmp = psEmp.executeQuery())
				{
					while (rsEmp.next())
					{
						Employe e = new Employe(gestionPersonnel,
								rsEmp.getInt("id_employe"),
								ligue,
								rsEmp.getString("nom_employe"),
								rsEmp.getString("prenom_employe"),
								rsEmp.getString("mail_employe"),
								rsEmp.getString("password_employe"),
								rsEmp.getDate("date_arrivee_employe") != null ? rsEmp.getDate("date_arrivee_employe").toLocalDate() : null,
								rsEmp.getString("date_départ_employe") != null && !rsEmp.getString("date_départ_employe").isEmpty() ? java.time.LocalDate.parse(rsEmp.getString("date_départ_employe")) : null
						);
						ligue.add(e);
					}
				}
			}

			// 2. Chargement du Root
			requete = "select id_employe, nom_employe, prenom_employe, mail_employe, password_employe, date_arrivee_employe, date_départ_employe from EMPLOYE where id_ligue is null limit 1";
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
						roots.getString("date_départ_employe") != null && !roots.getString("date_départ_employe").isEmpty() ? java.time.LocalDate.parse(roots.getString("date_départ_employe")) : null);
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
			PreparedStatement instruction;
			instruction = connection.prepareStatement(
				"insert into EMPLOYE (nom_employe, prenom_employe, mail_employe, password_employe, date_arrivee_employe, date_départ_employe, id_ligue) values(?, ?, ?, ?, ?, ?, ?)", 
				Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, employe.getNom());
			instruction.setString(2, employe.getPrenom());
			instruction.setString(3, employe.getMail());
			instruction.setString(4, employe.getPassword());
			instruction.setObject(5, employe.getDateArrivee());
			instruction.setObject(6, employe.getDateDepart());
			if (employe.getLigue() != null)
				instruction.setInt(7, employe.getLigue().getId());
			else
				instruction.setNull(7, java.sql.Types.INTEGER);
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}
	
	@Override
	public int insert(Ligue ligue) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into LIGUE (nom_ligue) values(?)", Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, ligue.getNom());		
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}

	@Override
	public int update(Ligue ligue) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("update LIGUE set nom_ligue = ? where id_ligue = ?");
			instruction.setString(1, ligue.getNom());
			instruction.setInt(2, ligue.getId());
			return instruction.executeUpdate();
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

    @Override
    public int update(Employe employe) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement instruction = connection.prepareStatement(
                    "UPDATE employe SET nom = ?, prenom = ?, mail = ?, password = ?, date_arrivee = ?, date_depart = ?, id_ligue = ? WHERE id = ?"
            );
            instruction.setString(1, employe.getNom());
            instruction.setString(2, employe.getPrenom());
            instruction.setString(3, employe.getMail());
            instruction.setString(4, employe.getPassword());
            instruction.setObject(5, employe.getDateArrivee());
            instruction.setObject(6, employe.getDateDepart());

            if (employe.getLigue() != null)
                instruction.setInt(7, employe.getLigue().getId());
            else
                instruction.setNull(7, java.sql.Types.INTEGER);

            instruction.setInt(8, employe.getId());

            return instruction.executeUpdate();
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
            throw new SauvegardeImpossible(exception);
        }
    }
	@Override
	public int delete(Employe employe) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction = connection.prepareStatement("DELETE FROM employe WHERE id = ?");
			instruction.setInt(1, employe.getId());
			return instruction.executeUpdate();
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}
	@Override
	public int update(Employe employe) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction = connection.prepareStatement(
					"UPDATE employe SET nom = ?, prenom = ?, mail = ?, password = ?, date_arrivee = ?, date_depart = ?, id_ligue = ? WHERE id = ?"
			);
			instruction.setString(1, employe.getNom());
			instruction.setString(2, employe.getPrenom());
			instruction.setString(3, employe.getMail());
			instruction.setString(4, employe.getPassword());
			instruction.setDate(5, employe.getDateArrivee() != null ? java.sql.Date.valueOf(employe.getDateArrivee()) : null);
			instruction.setDate(6, employe.getDateDepart() != null ? java.sql.Date.valueOf(employe.getDateDepart()) : null);

			// Gestion de la ligue (le root n'en a pas)
			if (employe.getLigue() != null)
				instruction.setInt(7, employe.getLigue().getId());
			else
				instruction.setNull(7, java.sql.Types.INTEGER);

			instruction.setInt(8, employe.getId()); // La clause WHERE

			return instruction.executeUpdate();
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}
}
