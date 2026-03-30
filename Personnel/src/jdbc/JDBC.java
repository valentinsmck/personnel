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
            String requete = "SELECT ligue.id_ligue, nom_ligue, id_employe, nom_employe, prenom_employe, mail_employe, password_employe, date_arrivee_employe, date_depart_employe, role_employe FROM ligue LEFT JOIN employe ON ligue.id_ligue = employe.id_ligue ORDER BY ligue.id_ligue";
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(requete);

            Ligue ligueActuelle = null;

            while (result.next())
            {
                int idLigue = result.getInt("id_ligue");

                if (ligueActuelle == null || ligueActuelle.getId() != idLigue)
                    ligueActuelle = gestionPersonnel.addLigue(idLigue, result.getString("nom_ligue"));

                if (result.getObject("id_employe") != null)
                {
                    Employe employe = ligueActuelle.addEmploye(
                            result.getInt("id_employe"),
                            result.getString("nom_employe"),
                            result.getString("prenom_employe"),
                            result.getString("mail_employe"),
                            result.getString("password_employe"),
                            result.getObject("date_arrivee_employe", LocalDate.class),
                            result.getObject("date_depart_employe", LocalDate.class)
                    );

                    if ("admin".equalsIgnoreCase(result.getString("role_employe")))
                        ligueActuelle.setAdministrateur(employe);
                }
            }

            PreparedStatement instructionRoot = connection.prepareStatement("SELECT * FROM employe WHERE id_ligue IS NULL");
            ResultSet root = instructionRoot.executeQuery();
            if (root.next())
                gestionPersonnel.addRoot(root.getInt("id_employe"), root.getString("nom_employe"), root.getString("prenom_employe"), root.getString("mail_employe"), root.getString("password_employe"), root.getObject("date_arrivee_employe", LocalDate.class), root.getObject("date_depart_employe", LocalDate.class));
            else
                gestionPersonnel.addRoot("root", "toor");
        }
		catch (SQLException e)
		{
			System.out.println(e);
		}
        catch (SauvegardeImpossible e){
            System.out.println(e.getMessage());
        }
        catch (DateInvalide e){
            System.out.println(e.getMessage());
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
	public int insert(Ligue ligue) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into ligue (nom_ligue) values(?)", Statement.RETURN_GENERATED_KEYS);
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
    public int insert(Employe employe) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement instruction;
            instruction = connection.prepareStatement("insert into EMPLOYE (nom_employe, prenom_employe, mail_employe, password_employe, date_arrivee_employe, date_depart_employe, id_ligue) values (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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
    public void update(Ligue ligue) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement instruction;
            instruction = connection.prepareStatement("update ligue set nom_ligue = ? WHERE id_ligue = ?");
            instruction.setString(1, ligue.getNom());
            instruction.setInt(2, ligue.getId());

            instruction.executeUpdate();

        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
            throw new SauvegardeImpossible(exception);
        }
    }
    public void update(Employe employe) throws SauvegardeImpossible
    {
        if (employe.getId() == -1)
            return;
        try
        {
            PreparedStatement instruction;
            instruction = connection.prepareStatement("UPDATE employe SET nom_employe = ?, prenom_employe = ?, mail_employe = ?, password_employe = ?, date_arrivee_employe = ?, date_depart_employe = ?, id_ligue = ?, role_employe = ? WHERE id_employe = ?");
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
            if (employe.getLigue() != null && employe.estAdmin(employe.getLigue()))
                instruction.setString(8, "admin");
            else
                instruction.setNull(8, java.sql.Types.VARCHAR);
            instruction.setInt(9, employe.getId());

            instruction.executeUpdate();

        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
            throw new SauvegardeImpossible(exception);
        }
    }
    @Override
    public void delete(Employe employe) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement instruction = connection.prepareStatement("delete from employe WHERE id_employe = ?");
            instruction.setInt(1, employe.getId());
            instruction.executeUpdate();
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
            throw new SauvegardeImpossible(exception);
        }
    }

    @Override
    public void delete(Ligue ligue) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement instruction = connection.prepareStatement("DELETE FROM employe WHERE id_ligue = ?");
            instruction.setInt(1, ligue.getId());
            instruction.executeUpdate();

            instruction = connection.prepareStatement("DELETE FROM ligue WHERE id_ligue = ?");
            instruction.setInt(1, ligue.getId());
            instruction.executeUpdate();
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
            throw new SauvegardeImpossible(exception);
        }
    }
}
