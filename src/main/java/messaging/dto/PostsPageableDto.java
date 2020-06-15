package messaging.dto;

import lombok.Builder;
import lombok.Setter;

import java.util.List;

@Setter
@Builder
public class PostsPageableDto {
    int itemsOnPage;
    int currentPage;
    int itemsTotal;
    List<MessageDto> posts;
}
