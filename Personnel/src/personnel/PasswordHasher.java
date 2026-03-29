package personnel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordHasher
{
	private static final String PREFIX = "sha256$";

	private PasswordHasher()
	{
	}

	public static String hashPassword(String password)
	{
		if (password == null)
			return null;

		try
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder(PREFIX.length() + hashBytes.length * 2);
			sb.append(PREFIX);
			for (byte b : hashBytes)
				sb.append(String.format("%02x", b));
			return sb.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new IllegalStateException("Algorithme SHA-256 indisponible.", e);
		}
	}

	public static String normalizeStoredPassword(String password)
	{
		if (password == null || password.isEmpty())
			return password;
		if (isHashed(password))
			return password;
		return hashPassword(password);
	}

	public static boolean checkPassword(String inputPassword, String storedPassword)
	{
		if (storedPassword == null)
			return inputPassword == null;
		if (isHashed(storedPassword))
			return storedPassword.equals(hashPassword(inputPassword));
		return storedPassword.equals(inputPassword);
	}

	private static boolean isHashed(String password)
	{
		return password != null && password.startsWith(PREFIX);
	}
}