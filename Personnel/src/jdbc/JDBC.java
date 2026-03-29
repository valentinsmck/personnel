package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import personnel.*;

public class JDBC implements Passerelle {
    Connection connection;

    public JDBC() {
        try {
            Class.forName(Credentials.getDriverClassName());
            connection = DriverManager.getConnection(Credentials.getUrl(), Credentials.getUser(), Credentials.getPassword());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Pilote JDBC non installe.", e);
        } catch (SQLException e) {
            throw new IllegalStateException("Connexion JDBC impossible.", e);
        }
    }

    @Override
    public GestionPersonnel getGestionPersonnel() {
        GestionPersonnel gestionPersonnel = new GestionPersonnel();
        Map<Integer, Ligue> liguesById = new HashMap<>();

        try {
            String requeteLigues =
                    "select l.id_ligue, l.nom_ligue, " +
                    "e.id_employe, e.nom_employe, e.prenom_employe, e.mail_employe, e.password_employe, " +
                    "e.date_arrivee_employe, e.date_départ_employe, e.rôle_employe " +
                    "from LIGUE l left join EMPLOYE e on e.id_ligue = l.id_ligue " +
                    "order by l.id_ligue, e.id_employe";

            try (Statement instruction = connection.createStatement();
                 ResultSet lignes = instruction.executeQuery(requeteLigues)) {
                while (lignes.next()) {
                    int idLigue = lignes.getInt("id_ligue");
                    Ligue ligue = liguesById.get(idLigue);
                    if (ligue == null) {
                        ligue = gestionPersonnel.addLigue(idLigue, lignes.getString("nom_ligue"));
                        liguesById.put(idLigue, ligue);
                    }

                    Object idEmpObject = lignes.getObject("id_employe");
                    if (idEmpObject != null) {
                        int idEmp = ((Number) idEmpObject).intValue();
                        Employe employe = ligue.addEmploye(
                                idEmp,
                                lignes.getString("nom_employe"),
                                lignes.getString("prenom_employe"),
                                lignes.getString("mail_employe"),
                                lignes.getString("password_employe"),
                                lignes.getDate("date_arrivee_employe") != null ? lignes.getDate("date_arrivee_employe").toLocalDate() : null,
                                parseOptionalLocalDate(lignes.getString("date_départ_employe"))
                        );

                        if ("admin".equalsIgnoreCase(lignes.getString("rôle_employe")))
                            ligue.setAdministrateurFromJDBC(employe);
                    }
                }
            }

            String requeteRoot = "select * from EMPLOYE where id_ligue is null limit 1";
            try (Statement instructionRoot = connection.createStatement();
                 ResultSet roots = instructionRoot.executeQuery(requeteRoot)) {
                if (roots.next()) {
                    gestionPersonnel.addRoot(
                            roots.getInt("id_employe"),
                            roots.getString("nom_employe"),
                            roots.getString("prenom_employe"),
                            roots.getString("mail_employe"),
                            roots.getString("password_employe"),
                            roots.getDate("date_arrivee_employe") != null ? roots.getDate("date_arrivee_employe").toLocalDate() : null,
                            parseOptionalLocalDate(roots.getString("date_départ_employe"))
                    );
                } else {
                    gestionPersonnel.addRoot("root", "toor");
                }
            }
        } catch (SQLException | SauvegardeImpossible | DateInvalide e) {
            throw new IllegalStateException("Impossible de charger les donnees depuis la base.", e);
        }
        return gestionPersonnel;
    }

    @Override
    public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible {
        close();
    }

    public void close() throws SauvegardeImpossible {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            throw new SauvegardeImpossible(e);
        }
    }

    @Override
    public int insert(Employe employe) throws SauvegardeImpossible {
        try {
            PreparedStatement instruction = connection.prepareStatement(
                    "insert into EMPLOYE (nom_employe, prenom_employe, mail_employe, password_employe, date_arrivee_employe, date_départ_employe, id_ligue, rôle_employe) values(?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            instruction.setString(1, employe.getNom());
            instruction.setString(2, employe.getPrenom());
            instruction.setString(3, employe.getMail());
            instruction.setString(4, employe.getPassword());
            instruction.setObject(5, employe.getDateArrivee());
            instruction.setString(6, employe.getDateDepart() != null ? employe.getDateDepart().toString() : null);

            if (employe.getLigue() != null)
                instruction.setInt(7, employe.getLigue().getId());
            else
                instruction.setNull(7, java.sql.Types.INTEGER);

            /** Définition du rôle lors de l'insertion */
            instruction.setString(8, (employe.getLigue() != null && employe.estAdmin(employe.getLigue())) ? "admin" : "employe");

            instruction.executeUpdate();
            ResultSet id = instruction.getGeneratedKeys();
            id.next();
            return id.getInt(1);
        } catch (SQLException exception) {
            throw new SauvegardeImpossible(exception);
        }
    }

    @Override
    public int insert(Ligue ligue) throws SauvegardeImpossible {
        try {
            PreparedStatement instruction = connection.prepareStatement("insert into LIGUE (nom_ligue) values(?)", Statement.RETURN_GENERATED_KEYS);
            instruction.setString(1, ligue.getNom());
            instruction.executeUpdate();
            ResultSet id = instruction.getGeneratedKeys();
            id.next();
            return id.getInt(1);
        } catch (SQLException exception) {
            throw new SauvegardeImpossible(exception);
        }
    }

    @Override
    public int update(Ligue ligue) throws SauvegardeImpossible {
        try {
            PreparedStatement instruction = connection.prepareStatement("update LIGUE set nom_ligue = ? where id_ligue = ?");
            instruction.setString(1, ligue.getNom());
            instruction.setInt(2, ligue.getId());
            return instruction.executeUpdate();
        } catch (SQLException exception) {
            throw new SauvegardeImpossible(exception);
        }
    }

    @Override
    public int update(Employe employe) throws SauvegardeImpossible {
        try {
            PreparedStatement instruction = connection.prepareStatement(
                    "update EMPLOYE set nom_employe = ?, prenom_employe = ?, mail_employe = ?, password_employe = ?, date_arrivee_employe = ?, date_départ_employe = ?, id_ligue = ?, rôle_employe = ? where id_employe = ?"
            );
            instruction.setString(1, employe.getNom());
            instruction.setString(2, employe.getPrenom());
            instruction.setString(3, employe.getMail());
            instruction.setString(4, employe.getPassword());
            instruction.setObject(5, employe.getDateArrivee());
            instruction.setString(6, employe.getDateDepart() != null ? employe.getDateDepart().toString() : null);

            if (employe.getLigue() != null)
                instruction.setInt(7, employe.getLigue().getId());
            else
                instruction.setNull(7, java.sql.Types.INTEGER);

            /** Mise à jour dynamique du rôle selon l'état de l'objet */
            instruction.setString(8, (employe.getLigue() != null && employe.estAdmin(employe.getLigue())) ? "admin" : "employe");
            instruction.setInt(9, employe.getId());

            return instruction.executeUpdate();
        } catch (SQLException exception) {
            throw new SauvegardeImpossible(exception);
        }
    }

    @Override
    public int delete(Ligue ligue) throws SauvegardeImpossible {
        int deletedLigues;
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement deleteEmployes = connection.prepareStatement("delete from EMPLOYE where id_ligue = ?");
                 PreparedStatement deleteLigue = connection.prepareStatement("delete from LIGUE where id_ligue = ?")) {

                deleteEmployes.setInt(1, ligue.getId());
                deleteEmployes.executeUpdate();

                deleteLigue.setInt(1, ligue.getId());
                deletedLigues = deleteLigue.executeUpdate();
            }

            connection.commit();
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                exception.addSuppressed(rollbackException);
            }
            throw new SauvegardeImpossible(exception);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
        return deletedLigues;
    }

    @Override
    public int delete(Employe employe) throws SauvegardeImpossible {
        try {
            PreparedStatement instruction = connection.prepareStatement("delete from EMPLOYE where id_employe = ?");
            instruction.setInt(1, employe.getId());
            return instruction.executeUpdate();
        } catch (SQLException exception) {
            throw new SauvegardeImpossible(exception);
        }
    }

    private LocalDate parseOptionalLocalDate(String sqlDate)
    {
        if (sqlDate == null || sqlDate.trim().isEmpty())
            return null;
        return LocalDate.parse(sqlDate);
    }
}