package demo.journal.JournalApp.Controller;

import demo.journal.JournalApp.Model.User;
import demo.journal.JournalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

//    @PostMapping
//    public ResponseEntity<?> createUser(@RequestBody User user){
//        try{
//            userService.saveNewUser(user);
//            return new ResponseEntity<>(HttpStatus.CREATED);
//        }
//        catch (Exception e){
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User userInDb=userService.findByUsername(userName);
        userInDb.setUsername(user.getUsername());
        userInDb.setPassword(user.getPassword());
        userService.save(userInDb);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

//    @DeleteMapping("/user")
//    public ResponseEntity<?> deleteUserById(){
//        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//        userService.deleteByUserName(authentication.getName());
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }


    @GetMapping
    public List<User> getAllUsers(){
        return userService.findAll();
    }
}
