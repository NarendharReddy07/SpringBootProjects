package demo.journal.JournalApp.Repository;

import demo.journal.JournalApp.Model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);

    void deleteByUsername(String username);

    // void deleteByUserName(String name);
}
