package messaging.service;

import messaging.dao.MessageRepository;
import messaging.dto.CreateUpdatePostDto;
import messaging.dto.MessageDto;
import messaging.dto.PostsPageableDto;
import messaging.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Message message = messageFromUpdateDto(idPost, updatePostDto);
        return messageToMessageDto(message);
    }

    @Override
    public MessageDto deletePost(String idPost) {
        Message message = getMessageById(idPost);
        messageRepository.delete(message);
        return messageToMessageDto(message);
    }

    @Override
    public MessageDto getPostById(String idPost) {
        Message message = getMessageById(idPost);
        return messageToMessageDto(message);
    }

    @Override
    public PostsPageableDto viewPostsPageable(int itemsOnPage, int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, itemsOnPage);
        Page<Message> messages = messageRepository.findAll(pageable);
        List<MessageDto> messagesDto = messages.getContent().stream()
                .map(m -> messageToMessageDto(m))
                .collect(Collectors.toList());
        return PostsPageableDto.builder()
                .posts(messagesDto)
                .currentPage(currentPage)
                .itemsOnPage(itemsOnPage)
                .itemsTotal(messagesDto.size())
                .build();

    }

    @Override
    public void complainPostByPostId(String idPost) {
        Message message = getMessageById(idPost);
        message.setComplain(message.getComplain() + 1);
        messageRepository.save(message);
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
                .complain(0)
                .complains(new HashMap<>())
                .id("Post:" + id)
                .build();
    }

    private Message messageFromUpdateDto(String idPos, CreateUpdatePostDto updatePostDto) {
        Message message = null;
        try {
            message = messageRepository.findById(idPos).orElseThrow(Exception::new);
        } catch (Exception e) {

        }
        return Message.builder()
                .ownerId(message.getOwnerId())
                .text(updatePostDto.getText())
                .postDate(message.getPostDate())
                .updateDate(LocalDateTime.now())
                .images(updatePostDto.getImages())
                .complain(message.getComplain())
                .complains(message.getComplains())
                .id(message.getId())
                .build();
    }

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
