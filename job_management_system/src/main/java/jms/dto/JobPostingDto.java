package jms.dto;

import jakarta.validation.constraints.*;
import jms.entity.enums.JobStatus;
import lombok.*;

import java.util.List;

/**
 * DTO cho JobPosting (dùng cho Controller)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostingDto {

    private Long id;

    @NotBlank(message = "Tên vị trí không được để trống")
    @Size(min = 3, max = 255, message = "Tên vị trí phải từ 3–255 ký tự")
    private String jobTitle;

    @NotBlank(message = "Mô tả công việc không được để trống")
    @Size(min = 10, message = "Mô tả công việc không được để trống")
    private String description;

    @NotBlank(message = "Yêu cầu công việc không được để trống")
    @Size(min = 10, message = "Yêu cầu công việc không được để trống")
    private String requirements;

    @NotBlank(message = "Địa điểm làm việc không được để trống")
    @Size(max = 255, message = "Địa điểm tối đa 255 ký tự")
    private String location;

    @Size(max = 255, message = "Mức lương tối đa 255 ký tự")
    private String salaryRange;

    @NotNull(message = "Trạng thái không được để trống")
    private JobStatus status;

    @NotNull(message = "Công ty không được để trống")
    private Long companyId;

    private String companyName; // hiển thị

    List<Long> customFormIds;
}
