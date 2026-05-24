package map.teledon.server.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private static final Logger logger = LogManager.getLogger(JdbcUtils.class);
    private Properties jdbcProps;
    private Connection instance = null;

    public JdbcUtils(Properties props) {
        jdbcProps = props;
    }

    public Connection getNewConnection(){
        logger.traceEntry();
        String url = jdbcProps.getProperty("jdbc.url");
        logger.info("Incercare de conectare la baza de date: {}", url);

        Connection con = null;
        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error("Eroare la stabilirea conexiunii", e);
        }
        return logger.traceExit(con);
    }
    public Connection getConnection(){
        logger.traceEntry();
        try {
            if (instance == null || instance.isClosed()) {
                instance = getNewConnection();
            }
        } catch (SQLException e) {
            logger.error("Eroare Db", e);
        }
        logger.traceExit();
        return instance;
    }
    public void closeConnection() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                logger.trace("Conexiunea la baza de date a fost inchisa.");
                instance = null;
            }
        } catch (SQLException e) {
            logger.error("Eroare la inchiderea conexiunii: ", e);
        }
    }
}
