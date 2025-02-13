package demo.journal.JournalApp.Service;

import demo.journal.JournalApp.Model.JournalEntity;
import demo.journal.JournalApp.Model.User;
import demo.journal.JournalApp.Repository.JournalRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalService {
    @Autowired
    private  JournalRepo journalRepo;

    @Autowired
    private UserService userService;

    @Transactional
    public void save(JournalEntity journal,String user){

        try{
            journal.setTime(LocalDateTime.now());
            JournalEntity savedJournal=journalRepo.save(journal);
            User savedUser=userService.findByUsername(user);
            savedUser.getJournalEntityList().add(savedJournal);
            //let us say username is set to be null
            //then exception raises and handled
            //but journal is saved and not added to userJournalEntityList
            //here it violates atomicity property
            //so we use @Transactional to handle
            /*
           savedUser.setUsername(null);//this line is intentionally set to get clarity on @Transactional
           */
            //so here if any exceptin raises then document should not be saved
            //to work with @Transactional database replication is must so needed to use mongoDbAtlas


            //to clearly understand about this run program in debug mode after uncommenting line 37  savedUser.setUsername(null)
            userService.save(savedUser);
        }catch(Exception e)
        {
            System.out.println(e);
            throw new RuntimeException("An error occured while saving the journal");
        }


    }

    public void save(JournalEntity journal){
        journalRepo.save(journal);
    }

    public List<JournalEntity> getALL(){
        return journalRepo.findAll();
    }

    public Optional<JournalEntity> getById(ObjectId id) {
        return journalRepo.findById(id);
    }

    public void deleteJournal(ObjectId id,String userName){
        User savedUser=userService.findByUsername(userName);
        savedUser.getJournalEntityList().removeIf(x->x.getId().equals(id));
        userService.save(savedUser);
        journalRepo.deleteById(id);
    }


}
