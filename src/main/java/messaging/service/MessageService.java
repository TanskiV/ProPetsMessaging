package messaging.service;

import messaging.dto.CreateUpdatePostDto;
import messaging.dto.MessageDto;
import messaging.dto.PostsPageableDto;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface MessageService {
    ResponseEntity<MessageDto> createPost( CreateUpdatePostDto createUpdatePostDto, String token);
    ResponseEntity<MessageDto> updatePost(String idPost , CreateUpdatePostDto createUpdatePostDto, String token) throws ChangeSetPersister.NotFoundException;
    ResponseEntity<MessageDto> deletePost(String idPost, String token);
    ResponseEntity<MessageDto> getPostById(String idPost, String token);
    ResponseEntity<String> viewPostsPageable(int itemsOnPage, int currentPage, String token);
    void complainPostByPostId(String id, String token);
    boolean hidePostFromFeed(String idPost, String token);


}
