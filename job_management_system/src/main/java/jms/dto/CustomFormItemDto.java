package jms.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomFormItemDto {
    private String title;        // bắt buộc
    private String description;  // optional
}