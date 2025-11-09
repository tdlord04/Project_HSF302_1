// jms/dto/CustomFormDto.java
package jms.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomFormDto {
    private Long id;

    @NotNull(message = "Phải thuộc về 1 tin tuyển dụng")
    private Long jobPostingId;

    // Hiển thị
    private String jobTitle;

    @NotBlank(message = "Tên form không được để trống")
    @Size(max = 200, message = "Tên form tối đa 200 ký tự")
    private String formName;

    @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
    private String description;

    // JSON mảng [{title, description}]
    private String formStructureJson;

    private boolean active = true;
}
