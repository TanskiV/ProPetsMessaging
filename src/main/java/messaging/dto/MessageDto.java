package messaging.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MessageDto {
        String id;
        String ownerId;
        String postDate;
        String text;
        List<String> images;
}
