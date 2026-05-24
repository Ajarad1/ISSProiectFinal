package map.teledon.server.repository;

import map.teledon.domain.Voluntar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class VoluntarDbRepo implements VoluntarRepository {
    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger(VoluntarDbRepo.class);

    public VoluntarDbRepo(Properties props) {
        logger.info("Initializare VoluntarDbRepo");
        jdbcUtils = new JdbcUtils(props);
    }

    @Override
    public Voluntar findByUsername(String username) {
        logger.traceEntry("Cautare voluntar dupa username {}", username);
        Connection conn = jdbcUtils.getConnection();
        Voluntar voluntar = null;

        try (PreparedStatement preSt = conn.prepareStatement("SELECT * FROM Voluntari WHERE username=?")) {
            preSt.setString(1, username);
            try (ResultSet rs = preSt.executeQuery()) {
                if (rs.next()) {
                    voluntar = new Voluntar(rs.getString("username"), rs.getString("parola"));
                    voluntar.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit(voluntar);
        return voluntar;
    }

    @Override
    public Voluntar findOne(Integer id) {
        logger.traceEntry("Cautare voluntar dupa id {}", id);
        Connection conn = jdbcUtils.getConnection();
        Voluntar voluntar = null;

        try (PreparedStatement preSt = conn.prepareStatement("SELECT * FROM Voluntari WHERE id=?")) {
            preSt.setInt(1, id);
            try (ResultSet rs = preSt.executeQuery()) {
                if (rs.next()) {
                    voluntar = new Voluntar(rs.getString("username"), rs.getString("parola"));
                    voluntar.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit(voluntar);
        return voluntar;
    }

    @Override
    public Iterable<Voluntar> findAll() {
        logger.traceEntry("Afisare toti voluntarii");
        Connection conn = jdbcUtils.getConnection();
        List<Voluntar> voluntari = new ArrayList<>();

        try (PreparedStatement preSt = conn.prepareStatement("SELECT * FROM Voluntari")) {
            try (ResultSet rs = preSt.executeQuery()) {
                while (rs.next()) {
                    Voluntar v = new Voluntar(rs.getString("username"), rs.getString("parola"));
                    v.setId(rs.getInt("id"));
                    voluntari.add(v);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit(voluntari);
        return voluntari;
    }

    @Override
    public Voluntar save(Voluntar voluntar) {
        logger.traceEntry("Salvare voluntar {}", voluntar);
        Connection conn = jdbcUtils.getConnection();

        try (PreparedStatement preSt = conn.prepareStatement("INSERT INTO Voluntari (username,parola) VALUES(?,?)")) {
            preSt.setString(1, voluntar.getUsername());
            preSt.setString(2, voluntar.getPassword());
            int result = preSt.executeUpdate();
            logger.trace("Salvat {} instante", result);
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit(voluntar);
        return voluntar;
    }

    @Override
    public Voluntar delete(Integer id) {
        logger.traceEntry("Sterge voluntarul cu id {}", id);
        Connection conn = jdbcUtils.getConnection();
        Voluntar voluntar = findOne(id);

        if (voluntar != null) {
            try (PreparedStatement preSt = conn.prepareStatement("DELETE FROM Voluntari WHERE id=?")) {
                preSt.setInt(1, id);
                int result = preSt.executeUpdate();
                logger.trace("Sters {} instante", result);
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        logger.traceExit(voluntar);
        return voluntar;
    }

    @Override
    public Voluntar update(Voluntar voluntar) {
        logger.traceEntry("Modifica voluntar {}", voluntar);
        Connection conn = jdbcUtils.getConnection();

        try(PreparedStatement preSt = conn.prepareStatement("UPDATE Voluntari SET username=?,parola=? WHERE id=?")){
            preSt.setString(1, voluntar.getUsername());
            preSt.setString(2, voluntar.getPassword());
            preSt.setInt(3,voluntar.getId());
            int result = preSt.executeUpdate();
            logger.trace("Modificat {} instante", result);
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit(voluntar);
        return voluntar;
    }
}

