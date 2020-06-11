package messaging.dao;

import messaging.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface MessageRepository extends MongoRepository<Message,String> {

}
