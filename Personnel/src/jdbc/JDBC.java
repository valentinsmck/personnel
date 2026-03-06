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
			String requete = "select * from ligue";
			Statement instruction = connection.createStatement();
			ResultSet ligues = instruction.executeQuery(requete);
			while (ligues.next())
				gestionPersonnel.addLigue(ligues.getInt(1), ligues.getString(2));

			requete = "select id, nom, prenom, mail, password, date_arrivee, date_depart from employe where id_ligue is null limit 1";
			ResultSet roots = instruction.executeQuery(requete);
			if (roots.next())
			{
				gestionPersonnel.addRoot(
						roots.getInt("id"),
						roots.getString("nom"),
						roots.getString("prenom"),
						roots.getString("mail"),
						roots.getString("password"),
						roots.getDate("date_arrivee") != null ? roots.getDate("date_arrivee").toLocalDate() : null,
						roots.getDate("date_depart") != null ? roots.getDate("date_depart").toLocalDate() : null);
			}
			else
			{
				gestionPersonnel.addRoot("root", "toor");
			}
		}
		catch (SQLException | SauvegardeImpossible e)
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
				"insert into employe (nom, prenom, mail, password, date_arrivee, date_depart, id_ligue) values(?, ?, ?, ?, ?, ?, ?)", 
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
			instruction = connection.prepareStatement("insert into ligue (nom) values(?)", Statement.RETURN_GENERATED_KEYS);
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
			instruction = connection.prepareStatement("update ligue set nom = ? where id = ?");
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
}
