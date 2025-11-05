package jms.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Kỹ năng của ứng viên
 */

@Entity
@Table(name = "skill", indexes = {
        @Index(name = "idx_skill_name", columnList = "name")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Skill extends BaseEntity {
    @Column(name = "name", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String name; // Tên kỹ năng
}
