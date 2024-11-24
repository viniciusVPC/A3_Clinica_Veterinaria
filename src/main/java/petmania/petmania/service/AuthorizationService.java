package petmania.petmania.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import petmania.petmania.repository.AdministradorRepository;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    AdministradorRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username == "") {
            throw new UsernameNotFoundException("No user found for " + username + ".");
        }
        return repo.findByEmail(username);
    }

}
