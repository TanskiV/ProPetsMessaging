package messaging.controller;

import messaging.dto.CreateUpdatePostDto;
import messaging.dto.MessageDto;
import messaging.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageService messageService;
//TODO add in all methods variable ownerId
    @PostMapping("/{lang}/v1/")
    public ResponseEntity<MessageDto> createPost(@RequestHeader("X-Token") String token,
                                                 @RequestBody CreateUpdatePostDto createMessageDto) {
        return messageService.createPost( createMessageDto, token);
    }

    @PutMapping("/{lang}/v1/update/")
    public ResponseEntity<MessageDto> updatePost(@RequestParam("postId") String idPost, @RequestBody CreateUpdatePostDto updateDto,
                                 @RequestHeader("X-Token") String token) throws ChangeSetPersister.NotFoundException {
        return messageService.updatePost(idPost, updateDto, token);
    }

    @DeleteMapping("/{lang}/v1/delete/")
    public ResponseEntity<MessageDto> deleteMessage(@RequestParam("postId") String postId, @RequestHeader("X-Token") String token) {
        return messageService.deletePost(postId, token);
    }

    @GetMapping("/{lang}/v1/")
    public ResponseEntity<MessageDto> getPostByPostId(@RequestParam("postId") String idPost, @RequestHeader("X-Token") String token) {
        return messageService.getPostById(idPost, token);
    }

    @GetMapping("/{lang}/v1/view")
    public ResponseEntity<String> getPostPageable(@RequestParam("itemsOnPage") int itemsOnPage,
                                            @RequestParam("currentPage") int currentPage,
                                            @RequestHeader("X-Token") String token) {
        return messageService.viewPostsPageable(itemsOnPage, currentPage, token);
    }

    @PutMapping("/{lang}/v1/complain/{id}")
    public void complainPostByPostId(@PathVariable("id") String idPost, @RequestHeader("X-Token") String token) {
        messageService.complainPostByPostId(idPost, token);
    }

    @PutMapping("/{lang}/v1/hide/{id}")
    public boolean hidePostFromFeed(@RequestHeader("X-Token") String token,
                                    @PathVariable("id") String id) {
        return messageService.hidePostFromFeed(id, token);
    }
}
