package jdbc;

/**
 * Exemple de configuration JDBC.
 *
 * Ce fichier sert de modele; la copie locale Credentials.java
 * contient les vrais identifiants de developpement.
 */
public class CredentialsExample 
{
	private static String driver = "mysql";
	private static String driverClassName = "com.mysql.cj.jdbc.Driver";
	private static String host = "localhost";
	private static String port = "3306";
	private static String database = "";
	private static String user = "";
	private static String password = "";
	
	/** Construit l'URL JDBC complete. */
	static String getUrl() 
	{
		return "jdbc:" + driver + "://" + host + ":" + port + "/" + database + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
	}
	
	/** Retourne la classe Java du driver JDBC. */
	static String getDriverClassName()
	{
		return driverClassName;
	}
	
	/** Retourne l'utilisateur SQL. */
	static String getUser() 
	{
		return user;
	}

	/** Retourne le mot de passe SQL. */
	static String getPassword() 
	{
		return password;
	}
}
