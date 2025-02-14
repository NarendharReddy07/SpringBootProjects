package demo.journal.JournalApp.Service;

import demo.journal.JournalApp.Model.User;
import demo.journal.JournalApp.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    @Autowired
    UserRepo userRepo;

    public void save(User user){
        userRepo.save(user);
    }

    public void saveNewUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepo.save(user);
    }

    public List<User> findAll(){
        return  userRepo.findAll();
    }

    public User findByUsername(String userName){
        return userRepo.findByUsername(userName);
    }

    public void deleteByUserName(String userName) {
         userRepo.deleteByUsername(userName);
    }

//    public void deleteByUserName(String name) {
//        userRepo.deleteByUserName(name);
//    }
}
