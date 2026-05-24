package map.teledon.server.repository;

import map.teledon.domain.CazCaritabil;
import map.teledon.domain.Donatie;
import map.teledon.domain.Donator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DonatieDbRepo implements DonatieRepository {
    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger(DonatieDbRepo.class);

    public DonatieDbRepo(Properties props) {
        logger.info("Initiere DonatieDbRepo");
        jdbcUtils = new JdbcUtils(props);
    }

    @Override
    public Donatie findOne(Integer id) {
        logger.traceEntry("Cautare donatie cu id {}",id);
        Connection con = jdbcUtils.getConnection();
        Donatie donatie = null;

        try (PreparedStatement preSt = con.prepareStatement("SELECT * FROM Donatii WHERE id=?")){
            preSt.setInt(1,id);
            try (ResultSet rs = preSt.executeQuery()){
                if(rs.next()){
                    Donator donator = new Donator("","","");
                    donator.setId(rs.getInt("idDonator"));

                    CazCaritabil caz = new CazCaritabil("",0.0);
                    caz.setId(rs.getInt("idCaz"));

                    donatie = new Donatie(donator,caz,rs.getDouble("sumaDonata"));
                    donatie.setId(rs.getInt("id"));
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        logger.traceExit(donatie);
        return donatie;
    }

    public Iterable<Donatie> findAll() {
        logger.traceEntry("Afisare toate donatiile");
        Connection con = jdbcUtils.getConnection();
        List<Donatie> donatii = new ArrayList<>();

        try (PreparedStatement preSt = con.prepareStatement("SELECT * FROM Donatii")){
            try (ResultSet rs = preSt.executeQuery()){
                while (rs.next()){
                    Donator donator = new Donator("","","");
                    donator.setId(rs.getInt("idDonator"));

                    CazCaritabil caz = new CazCaritabil("",0.0);
                    caz.setId(rs.getInt("idCaz"));

                    Donatie donatie = new Donatie(donator,caz,rs.getDouble("sumaDonata"));
                    donatie.setId(rs.getInt("id"));
                    donatii.add(donatie);
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        logger.traceExit(donatii);
        return donatii;
    }

    @Override
    public Donatie save(Donatie donatie) {
        logger.traceEntry("Adaugare donatie", donatie);
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement preSt = con.prepareStatement("INSERT INTO Donatii (idDonator, idCaz, sumaDonata) VALUES (?,?,?)")){
            preSt.setInt(1,donatie.getDonator().getId());
            preSt.setInt(2,donatie.getCaz().getId());
            preSt.setDouble(3,donatie.getSumaDonata());
            int result = preSt.executeUpdate();
            logger.trace("Salvare donator {}",result);
        } catch (Exception e) {
            logger.error(e);
        }
        logger.traceExit(donatie);
        return donatie;
    }

    @Override
    public Donatie update(Donatie donatie) {
        logger.traceEntry("Modificare donatie", donatie);
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement preSt = con.prepareStatement("UPDATE Donatii SET idDonator=?, idCaz=?, sumaDonata=? WHERE id=?")){
            preSt.setInt(1,donatie.getDonator().getId());
            preSt.setInt(2,donatie.getCaz().getId());
            preSt.setDouble(3,donatie.getSumaDonata());
            preSt.setInt(4,donatie.getId());
            int result = preSt.executeUpdate();
            logger.trace("Salvare modificare donatie {}",result);
        } catch (Exception e) {
            logger.error(e);
        }
        logger.traceExit(donatie);
        return donatie;
    }

    public Donatie delete(Integer id) {
        logger.traceEntry("Sterge donatie cu id {}",id);
        Connection con = jdbcUtils.getConnection();
        Donatie donatie = findOne(id);

        if (donatie != null) {
            try (PreparedStatement preSt = con.prepareStatement("DELETE FROM Donatii WHERE id=?")){
                preSt.setInt(1,id);
                int result = preSt.executeUpdate();
                logger.trace("Stergere donatie {}",result);
            } catch (Exception e) {
                logger.error(e);
            }
        }
        logger.traceExit(donatie);
        return donatie;
    }
}
