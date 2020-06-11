package messaging.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "ProPetsMessage")
public class Message {
    @Id
    String id;
    String ownerId;
    LocalDateTime postDate;
    String text;
    Set<String> images;
    boolean complain;
    Map<String, String> complains;
}
