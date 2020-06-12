package messaging.dto;

import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class CreateUpdatePostDto {
    String text;
    List<String> images;
}
