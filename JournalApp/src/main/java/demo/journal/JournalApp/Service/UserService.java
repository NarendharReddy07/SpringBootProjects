package demo.journal.JournalApp.Service;

import demo.journal.JournalApp.Model.User;
import demo.journal.JournalApp.Repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
//simple logging facade for java
public class UserService {

    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    //no need to instantiate Logger if slf4j is used
    //private static final Logger logger= LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserRepo userRepo;

    public void save(User user){
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }


    public void saveNewUser(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            //logger.info("user saved {}",user.getUsername());
            log.info("user saved {}",user.getUsername());


            userRepo.save(user);
        }
        catch (Exception e){
            //System.out.println(e);
            //if used slf4j use log
            log.info("this is info");
            log.error("error in saving user {}",user.getUsername());

            //if not used slf4j
//            logger.info("this is info");
//            logger.error("error in saving user {}",user.getUsername());
//            logger.debug("this is debug(please enable logging level in applications.properties to use this)");
//            logger.trace("this is trace");
        }

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

    public void saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepo.save(user);
    }

//    public void deleteByUserName(String name) {
//        userRepo.deleteByUserName(name);
//    }
}
