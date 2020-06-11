package messaging.service;

import messaging.dao.MessageRepository;
import messaging.dto.CreateUpdatePostDto;
import messaging.dto.MessageDto;
import messaging.dto.PostsPageableDto;
import messaging.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;

@Service
public class MessageServiceImpl implements MessageService {
    static int id = 0;
    @Autowired
    MessageRepository messageRepository;

    @Override
    public MessageDto createPost(String ownerId, CreateUpdatePostDto createPostDto) {
        Message message = messageFromCreateDto(ownerId, createPostDto);
        messageRepository.save(message);
        return messageToMessageDto(message);
    }

    @Override
    public MessageDto updatePost(String idPost, CreateUpdatePostDto updatePostDto) {
       Message message;// = messageFromUpdateDto()
        return null;
    }

    @Override
    public MessageDto deletePost(String idPost) {
        Message message = getMessageById(idPost);
        return null;
    }

    @Override
    public MessageDto getPostById(String idPost) {
        return null;
    }

    @Override
    public PostsPageableDto viewPostsPageable(int itemsOnPage, int currentPage) {
        return null;
    }

    @Override
    public boolean complainPostByPostId(String idPost) {
        return false;
    }

    @Override
    public boolean hidePostFromFeed(String id) {
        return false;
    }

    private Message messageFromCreateDto(String ownerId, CreateUpdatePostDto createPostDto) {
        id++;
        return Message.builder()
                .ownerId(ownerId)
                .text(createPostDto.getText())
                .postDate(LocalDateTime.now())
                .images(createPostDto.getImages())
                .complain(false)
                .complains(new HashMap<>())
                .id("Post:" + id)
                .build();
    }

//    private Message messageFromUpdateDto(String idPos, CreateUpdatePostDto UpdatePostDto) {
//
//        return Message.builder()
//                .ownerId(ownerId)
//                .text(UpdatePostDto.getText())
//                .postDate(LocalDateTime.now())
//                .images(UpdatePostDto.getImages())
//                .complain(false)
//                .complains(new HashMap<>())
//                .id("Post:" + id)
//                .build();
//    }

    private MessageDto messageToMessageDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .images(message.getImages())
                .ownerId(message.getOwnerId())
                .postDate(message.getPostDate())
                .text(message.getText())
                .build();
    }

    private Message getMessageById(String idPost) {
        return messageRepository.findById(idPost).orElse(new Message());
    }

}
