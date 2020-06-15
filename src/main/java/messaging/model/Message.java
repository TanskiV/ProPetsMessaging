package messaging.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "ProPetsMessages")
@EqualsAndHashCode(of = {"id"})
public class Message {
    @Id
    String id;
    String ownerId;
    String postDate;
    LocalDateTime updateDate;
    String text;
    List<String> images;
    Set<String> complain;
}
