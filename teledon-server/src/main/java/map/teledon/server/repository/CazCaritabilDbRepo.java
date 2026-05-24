package map.teledon.server.repository;


import map.teledon.domain.CazCaritabil;
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

public class CazCaritabilDbRepo implements CazCaritabilRepository{
    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger(CazCaritabilDbRepo.class);

    public CazCaritabilDbRepo(Properties props) {
        logger.info("Initializare CazCaritabilDbRepo");
        jdbcUtils = new JdbcUtils(props);
    }

    @Override
    public CazCaritabil findOne(Integer id) {
        logger.traceEntry("Cautare caz caritabil dupa id {}", id);
        Connection conn = jdbcUtils.getConnection();
        CazCaritabil cazCaritabil = null;

        try (PreparedStatement preSt = conn.prepareStatement("SELECT * FROM CazuriCaritabile WHERE id=?")) {
            preSt.setInt(1, id);
            try (ResultSet rs = preSt.executeQuery()) {
                if (rs.next()) {
                    cazCaritabil = new CazCaritabil(rs.getString("numeCaz"), rs.getDouble("sumaTotala"));
                    cazCaritabil.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit(cazCaritabil);
        return cazCaritabil;
    }

    @Override
    public Iterable<CazCaritabil> findAll() {
        logger.traceEntry("Afisare toate cazurile caritabile");
        Connection conn = jdbcUtils.getConnection();
        List<CazCaritabil> cazuri = new ArrayList<>();

        try (PreparedStatement preSt = conn.prepareStatement("SELECT * FROM CazuriCaritabile")) {
            try (ResultSet rs = preSt.executeQuery()) {
                while (rs.next()) {
                    CazCaritabil c = new CazCaritabil(rs.getString("numeCaz"), rs.getDouble("sumaTotala"));
                    c.setId(rs.getInt("id"));
                    cazuri.add(c);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit(cazuri);
        return cazuri;
    }
    @Override
    public CazCaritabil save(CazCaritabil caz) {
        logger.traceEntry("Salvare Caz Caritabil {}", caz);
        Connection conn = jdbcUtils.getConnection();

        try (PreparedStatement preSt = conn.prepareStatement("INSERT INTO CazuriCaritabile (numeCaz,sumaTotala) VALUES(?,?)")) {
            preSt.setString(1, caz.getNumeCaz());
            preSt.setDouble(2, caz.getSumaTotala());
            int result = preSt.executeUpdate();
            logger.trace("Salvat {} instante", result);
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit(caz);
        return caz;
    }

    @Override
    public CazCaritabil delete(Integer id) {
        logger.traceEntry("Sterge cazul cu id {}", id);
        Connection conn = jdbcUtils.getConnection();
        CazCaritabil caz = findOne(id);

        if (caz != null) {
            try (PreparedStatement preSt = conn.prepareStatement("DELETE FROM CazuriCaritabile WHERE id=?")) {
                preSt.setInt(1, id);
                int result = preSt.executeUpdate();
                logger.trace("Sters {} instante", result);
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        logger.traceExit(caz);
        return caz;
    }

    @Override
    public CazCaritabil update(CazCaritabil caz) {
        logger.traceEntry("Modifica caz {}", caz);
        Connection conn = jdbcUtils.getConnection();

        try(PreparedStatement preSt = conn.prepareStatement("UPDATE CazuriCaritabile SET numeCaz=?,sumaTotala=? WHERE id=?")){
            preSt.setString(1, caz.getNumeCaz());
            preSt.setDouble(2, caz.getSumaTotala());
            preSt.setInt(3,caz.getId());
            int result = preSt.executeUpdate();
            logger.trace("Modificat {} instante", result);
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit(caz);
        return caz;
    }
}
