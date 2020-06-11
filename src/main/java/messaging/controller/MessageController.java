package messaging.controller;

import messaging.dto.CreateUpdatePostDto;
import messaging.dto.MessageDto;
import messaging.dto.PostsPageableDto;
import messaging.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageService messageService;

    @PostMapping("/{lang}/v1/owner/{ownerId}")
    public MessageDto createPost(@PathVariable("ownerId") String ownerId, @RequestBody CreateUpdatePostDto createMessageDto) {
        return messageService.createPost(ownerId, createMessageDto);
    }

    @PutMapping("/{lang}/v1/{id}")
    public MessageDto updatePost(@PathVariable("id") String id, @RequestBody CreateUpdatePostDto updateDto) {
        return messageService.updatePost(id, updateDto);
    }

    @DeleteMapping("/{lang}/v1/{id}")
    public MessageDto deleteMessage(@PathVariable("id") String postId) {
        return messageService.deletePost(postId);
    }

    @GetMapping("/{lang}/v1/{id}")
    public MessageDto getPostByPostId(@PathVariable("id") String id){
        return  messageService.getPostById(id);
    }

    @GetMapping("/{lang}/v1/view")
    public PostsPageableDto getPostPageable(@RequestParam("itemsOnPage") int itemsOnPage,
                                            @RequestParam("currentPage") int currentPage ){
        return messageService.viewPostsPageable(itemsOnPage, currentPage);
    }

    @PutMapping("/{lang}/v1/complain/{id}")
    public boolean complainPostByPostId (@PathVariable("id")String id){
        return messageService.complainPostByPostId(id);
    }

    @PutMapping("/{lang}/v1/hide/{id}")
    public boolean hidePostFromFeed(@PathVariable("id")String id){
        return messageService.hidePostFromFeed(id);
    }
}
