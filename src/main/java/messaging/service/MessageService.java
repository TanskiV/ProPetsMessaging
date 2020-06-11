package messaging.service;

import messaging.dto.CreateUpdatePostDto;
import messaging.dto.MessageDto;
import messaging.dto.PostsPageableDto;
import org.springframework.data.crossstore.ChangeSetPersister;

public interface MessageService {
    MessageDto createPost(String ownerId,CreateUpdatePostDto createUpdatePostDto);
    MessageDto updatePost(String id , CreateUpdatePostDto createUpdatePostDto) throws ChangeSetPersister.NotFoundException;
    MessageDto deletePost(String idPost);
    MessageDto getPostById(String idPost);
    PostsPageableDto viewPostsPageable(int itemsOnPage, int currentPage);
    boolean complainPostByPostId(String id);
    boolean hidePostFromFeed(String id);


}
