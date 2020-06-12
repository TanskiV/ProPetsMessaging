package messaging.dao;

import messaging.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;


public interface MessageRepository extends MongoRepository<Message,String> {

}
