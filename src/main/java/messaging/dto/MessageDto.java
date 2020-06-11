package messaging.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class MessageDto {
        String id;
        String ownerId;
        LocalDateTime postDate;
        String text;
        Set<String> images;
}
