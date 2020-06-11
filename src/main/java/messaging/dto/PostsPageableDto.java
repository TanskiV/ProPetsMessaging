package messaging.dto;

import lombok.Setter;

import java.util.Set;

@Setter
public class PostsPageableDto {
    int itemsOnPage;
    int currentPage;
    int itemsTotal;
    Set<MessageDto> posts;
}
