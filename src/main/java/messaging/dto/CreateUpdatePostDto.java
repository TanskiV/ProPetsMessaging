package messaging.dto;

import lombok.Getter;

import java.util.Set;

@Getter
public class CreateUpdatePostDto {
    String text;
    Set<String> images;
}
