// DocumentDTO.java
package jms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private Long id;
    private Long candidateId;
    private String documentName;
    private String filePath;
    private String fileType;
    private Instant uploadedAt;
    private Instant updatedAt;
}