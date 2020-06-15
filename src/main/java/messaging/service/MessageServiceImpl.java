package messaging.service;

import com.google.gson.Gson;
import messaging.dao.MessageRepository;
import messaging.dto.CreateUpdatePostDto;
import messaging.dto.MessageDto;
import messaging.dto.PostsPageableDto;
import messaging.exceptions.MessageException;
import messaging.model.Message;
import messaging.utils.LoginToken;
import messaging.utils.MessagingConstants;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Override
    public ResponseEntity<MessageDto> createPost(CreateUpdatePostDto createPostDto, String token) {
        Message message = messageFromCreateDto(createPostDto, token);
        messageRepository.save(message);
        LoginToken loginToken = getLoginAndNewTokenFromToken(token);
        return getResponseEntity(loginToken.getToken(), message);
    }

    @Override
    public ResponseEntity<MessageDto> updatePost(String idPost, CreateUpdatePostDto updatePostDto, String token) {
        Message message = messageFromUpdateDto(idPost, updatePostDto);
        if (!userIsOwner(message, token)) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } else return getResponseEntity(token, message);
    }


    @Override
    public ResponseEntity<MessageDto> deletePost(String idPost, String token) {
        Message message = getMessageById(idPost);
        if (!userIsOwner(message, token)) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        messageRepository.delete(message);
        return getResponseEntity(token, message);
    }

    @Override
    public ResponseEntity<MessageDto> getPostById(String idPost, String token) {
        Message message = getMessageById(idPost);
        return getResponseEntity(token, message);
    }

    @Override
    public ResponseEntity<String> viewPostsPageable(int itemsOnPage, int currentPage, String token) {
        Pageable pageable = PageRequest.of(currentPage, itemsOnPage);
       List<MessageDto> messageDto = messageRepository.findAll(pageable).stream()
                .map(message -> messageToMessageDto(message))
                .collect(Collectors.toList());
        PostsPageableDto postsPageableDto = PostsPageableDto.builder()
                .posts(messageDto)
                .currentPage(currentPage)
                .itemsOnPage(itemsOnPage)
                .itemsTotal(messageDto.size())
                .build();
        String json = new Gson().toJson(postsPageableDto);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @Override
    public void complainPostByPostId(String idPost, String token) {
        Message message = getMessageById(idPost);
        LoginToken loginToken = getLoginAndNewTokenFromToken(token);
        message.getComplain().add(loginToken.getLogin());
        messageRepository.save(message);
    }

    @Override
    public boolean hidePostFromFeed(String idPost, String token) {
        Message message = getMessageById(idPost);
//        Map<String, Set<String>> complains = message.getComplains();
//        if (complains.containsKey(idPost) && complains.containsValue(user)) {
//            return false;
//        }
//        message.addComplain(idPost, user);
        //TODO
        messageRepository.save(message);
        return true;
    }

    private Message messageFromCreateDto(CreateUpdatePostDto createPostDto, String token) {
        long id = messageRepository.findAll().size();
        LoginToken loginToken = getLoginAndNewTokenFromToken(token);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return Message.builder()
                .ownerId(loginToken.getLogin())
                .text(createPostDto.getText())
                .postDate(sdf.format(new Date()))
                .images(createPostDto.getImages())
                .complain(new HashSet<>())
                .id("Post:" + (id + 1))
//                .token(loginToken.getToken())
                .build();

    }


    private LoginToken getLoginAndNewTokenFromToken(String token) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create("", mediaType);
        Request request = new Request.Builder()
                .url(MessagingConstants.VALIDATE_URL)
                .method("PUT", body)
                .addHeader("X-Token", token)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        response.close();
        return LoginToken.builder()
                .login(response.header("Id"))
                .token(response.header("X-Token"))
                .build();
    }

    private Message messageFromUpdateDto(String idPos, CreateUpdatePostDto updatePostDto) {
        Message message = messageRepository.findById(idPos)
                .orElseThrow(() -> new MessageException("F"));
        return Message.builder()
                .ownerId(message.getOwnerId())
                .text(updatePostDto.getText())
                .postDate(message.getPostDate())
                .updateDate(LocalDateTime.now())
                .images(updatePostDto.getImages())
                .complain(message.getComplain())
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

    private ResponseEntity<MessageDto> getResponseEntity(String token, Message message) {
        LoginToken loginToken = getLoginAndNewTokenFromToken(token);
        HttpHeaders header = new HttpHeaders();
        header.set("X-Token", loginToken.getToken());
        MessageDto messageDto = messageToMessageDto(message);
        return new ResponseEntity<>(messageDto, header, HttpStatus.OK);
    }

    private Message getMessageById(String idPost) {
        return messageRepository.findById(idPost).orElse(new Message());
    }

    private boolean userIsOwner(Message message, String token) {
        if (message == null) {
            return false;
        }
        LoginToken loginToken = getLoginAndNewTokenFromToken(token);
        if (message.getOwnerId().equals(loginToken.getLogin())) {
            return true;
        } else return false;
    }

}
