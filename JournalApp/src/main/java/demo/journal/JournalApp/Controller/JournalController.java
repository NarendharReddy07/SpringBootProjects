package demo.journal.JournalApp.Controller;

import demo.journal.JournalApp.Model.JournalEntity;
import demo.journal.JournalApp.Service.JournalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalController {


    @Autowired
    private JournalService journalService;
   // Map<Integer, JournalEntity> journalEntries=new HashMap();

    @PostMapping("/createEntry/{user}")
    public ResponseEntity<JournalEntity> createEntry(@PathVariable String user,@RequestBody JournalEntity journal){
        try{
            journalService.save(journal,user);
            return new ResponseEntity<>(journal,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllEntries")
    public ResponseEntity<?> getAllJournalEntries(){
        List<JournalEntity> list= journalService.getALL();
        if(!list.isEmpty())
            return new ResponseEntity<>(list,HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable ObjectId id ){
        Optional<JournalEntity> journalEntity= journalService.getById(id);
        if(journalEntity.isPresent()){
            return new ResponseEntity<>(journalEntity.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteById/{user}/{id}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId id,@PathVariable String user ){
         journalService.deleteJournal(id,user);
         return  new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/edit/{user}/{id}")
    public ResponseEntity<?> editById(@PathVariable ObjectId id, @RequestBody JournalEntity newEntry, @PathVariable String user){
        JournalEntity oldEntry=journalService.getById(id).orElse(null);
        if(oldEntry!=null){
            oldEntry.setContent(newEntry.getContent()!=null && !newEntry.getContent().equals("")?newEntry.getContent(): oldEntry.getContent());
            oldEntry.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().equals("")?newEntry.getTitle(): oldEntry.getTitle());
            journalService.save(oldEntry);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
