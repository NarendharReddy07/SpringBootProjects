package demo.journal.JournalApp.Controller;

import demo.journal.JournalApp.Model.JournalEntity;
import demo.journal.JournalApp.Service.JournalService;
import demo.journal.JournalApp.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalController {


    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;
   // Map<Integer, JournalEntity> journalEntries=new HashMap();

//    @PostMapping("/createEntry/{user}")
//    public ResponseEntity<JournalEntity> createEntry(@PathVariable String user,@RequestBody JournalEntity journal){
//        try{
//            journalService.save(journal,user);
//            return new ResponseEntity<>(journal,HttpStatus.OK);
//        }
//        catch(Exception e){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
    @PostMapping("/createEntry")
    public ResponseEntity<JournalEntity> createEntry(@RequestBody JournalEntity journal){
        try{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String user=authentication.getName();
            journalService.save(journal,user);
            return new ResponseEntity<>(journal,HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllJournalEntriesOfUser")
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String user=authentication.getName();
            List<JournalEntity> list= userService.findByUsername(user).getJournalEntityList();
            if(list!=null && !list.isEmpty())
                return new ResponseEntity<>(list,HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getJournalById/{id}")
    public ResponseEntity<?> findById(@PathVariable ObjectId id ){
        //ObjectId objectId=new ObjectId(id);
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String user=authentication.getName();
        List<JournalEntity> list= userService.findByUsername(user).getJournalEntityList();

        List<JournalEntity> journalEntity= list.stream().filter(x->x.getId().equals(id)).toList();
        if(!journalEntity.isEmpty()){
            Optional<JournalEntity> journal=journalService.findById(id);
            if(journal.isPresent()){
                return new ResponseEntity<>(journal.get(),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteJournalById/{id}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId id ){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String user=authentication.getName();
        boolean removed=journalService.deleteJournalById(id,user);
        if(removed)
            return  new ResponseEntity(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/updateJournalById/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId id, @RequestBody JournalEntity newEntry){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String user=authentication.getName();
        List<JournalEntity> list= userService.findByUsername(user).getJournalEntityList();

        List<JournalEntity> journalEntity= list.stream().filter(x->x.getId().equals(id)).toList();
        if(!journalEntity.isEmpty()){
            JournalEntity oldEntry=journalService.findById(id).orElse(null);
            if(oldEntry!=null){
                oldEntry.setContent(!newEntry.getContent().equals("") ?newEntry.getContent(): oldEntry.getContent());
                oldEntry.setTitle(!newEntry.getTitle().equals("") ?newEntry.getTitle(): oldEntry.getTitle());
                journalService.save(oldEntry);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
