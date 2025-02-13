package demo.journal.JournalApp.Repository;

import demo.journal.JournalApp.Model.JournalEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepo extends MongoRepository<JournalEntity, ObjectId> {

    JournalEntity findById(Integer id);

    void deleteById(Integer id);
}
