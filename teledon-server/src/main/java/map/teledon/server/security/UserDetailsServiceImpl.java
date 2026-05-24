package map.teledon.server.security;

import map.teledon.domain.Voluntar;
import map.teledon.server.repository.VoluntarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private VoluntarRepository voluntarRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Căutăm voluntarul în baza de date folosind repository-ul tău din laboratoarele trecute
        Voluntar voluntar = voluntarRepository.findByUsername(username);

        if (voluntar == null) {
            throw new UsernameNotFoundException("Voluntarul cu username-ul " + username + " nu a fost găsit!");
        }

        // 2. Transformăm Voluntarul tău într-un obiect de tip User pe care Spring Security îl înțelege
        // ATENȚIE: Dacă metoda de parolă din Voluntar se numește getParola(), schimbă mai jos!
        return new User(voluntar.getUsername(), voluntar.getPassword(), new ArrayList<>());
    }
}