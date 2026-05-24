package map.teledon.server.repository;

import map.teledon.domain.Donator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DonatorDbRepository implements DonatorRepository {
    private JdbcUtils jdbcUtils;
    private static final Logger logger =  LogManager.getLogger(DonatorDbRepository.class);

    public DonatorDbRepository(Properties props) {
        logger.info("Initiere repo Donator");
        jdbcUtils = new JdbcUtils(props);
    }

    @Override
    public Donator findOne(Integer id) {
        logger.traceEntry("Cautare Donator dupa id {}", id);
        Connection con = jdbcUtils.getConnection();
        Donator donator = null;

        try(PreparedStatement prepSt = con.prepareStatement("SELECT * FROM Donatori WHERE id=?")){
            prepSt.setInt(1,id);
            try (ResultSet rs = prepSt.executeQuery()){
                if(rs.next()){
                    donator = new Donator(rs.getString("nume"), rs.getString("adresa"), rs.getString("numarTelefon"));
                    donator.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e){
            logger.error(e);
        }
        logger.traceExit(donator);
        return donator;
    }

    @Override
    public Iterable<Donator> findAll() {
        logger.traceEntry("afisare toti donatorii");
        Connection con = jdbcUtils.getConnection();
        List<Donator> donatori = new ArrayList<>();

        try(PreparedStatement preSt = con.prepareStatement("SELECT * FROM Donatori")){
            try(ResultSet rs = preSt.executeQuery()){
                while(rs.next()){
                    Donator donator = new Donator(rs.getString("nume"), rs.getString("adresa"), rs.getString("numarTelefon"));
                    donator.setId(rs.getInt("id"));
                    donatori.add(donator);
                }
            }
        } catch (SQLException e){
            logger.error(e);
        }
        logger.traceExit(donatori);
        return donatori;
    }

    @Override
    public Donator save(Donator donator) {
        logger.traceEntry("Salvare donator", donator);
        Connection con = jdbcUtils.getConnection();

        try(PreparedStatement preSt = con.prepareStatement("INSERT INTO Donatori (nume,adresa,numarTelefon) VALUES (?,?,?)")){
            preSt.setString(1, donator.getNume());
            preSt.setString(2, donator.getAdresa());
            preSt.setString(3, donator.getNumarTelefon());
            int result = preSt.executeUpdate();
            logger.trace("Salvare donator {}", result);
        } catch (SQLException e){
            logger.error(e);
        }
        logger.traceExit(donator);
        return donator;
    }

    @Override
    public Donator delete(Integer id) {
        logger.traceEntry("Stergere donator dupa id {}", id);
        Connection con = jdbcUtils.getConnection();
        Donator donator = findOne(id);

        if(donator != null){
            try (PreparedStatement preSt = con.prepareStatement("DELETE FROM Donatori WHERE id=?")){
                preSt.setInt(1,id);
                int result = preSt.executeUpdate();
                logger.trace("Stergere donator {}", result);
            }   catch (SQLException e){
                logger.error(e);
            }
        }
        logger.traceExit(donator);
        return donator;
    }

    @Override
    public Donator update(Donator donator) {
        logger.traceEntry("Modificare donator", donator);
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement preSt = con.prepareStatement("UPDATE Donatori SET nume=?, adresa=?, numarTelefon=? WHERE id=?")){
            preSt.setString(1, donator.getNume());
            preSt.setString(2, donator.getAdresa());
            preSt.setString(3, donator.getNumarTelefon());
            preSt.setInt(4,donator.getId());
            int result = preSt.executeUpdate();
            logger.trace("Modificare donator {}", result);
        } catch (SQLException e){
            logger.error(e);
        }
        logger.traceExit(donator);
        return donator;
    }

    @Override
    public Iterable<Donator> filterByName(String name){
        logger.traceEntry("filtrare dupa nume {}", name);
        Connection con = jdbcUtils.getConnection();
        List<Donator> donatori = new ArrayList<>();

        try (PreparedStatement preSt = con.prepareStatement("SELECT * FROM Donatori WHERE nume LIKE ?")){
            preSt.setString(1,"%" + name + "%");
            try (ResultSet rs = preSt.executeQuery()){
                while (rs.next()){
                    Donator donator = new Donator(rs.getString("nume"), rs.getString("adresa"), rs.getString("numarTelefon"));
                    donator.setId(rs.getInt("id"));
                    donatori.add(donator);
                }
            }
        } catch (SQLException e){
            logger.error(e);
        }
        logger.traceExit(donatori);
        return donatori;
    }
}

