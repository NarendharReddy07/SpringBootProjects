package demo.journal.JournalApp.Service;

import demo.journal.JournalApp.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        demo.journal.JournalApp.Model.User userFromDb=userRepo.findByUsername(username);
        if(userFromDb!=null){
            return User.builder()
                    .username(userFromDb.getUsername())
                    .password(userFromDb.getPassword())
                    .roles(userFromDb.getRoles().toArray(new String[0]))
                    .build();
        }
        else
            throw new UsernameNotFoundException("user not found with name "+username);

    }
}
