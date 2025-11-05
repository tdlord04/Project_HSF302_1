package jms.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Thông tin cá nhân của ứng viên
 */

@Entity
@Table(name = "candidate_profile", indexes = {
        @Index(name = "idx_candidate_name", columnList = "full_name"),
        @Index(name = "idx_candidate_email", columnList = "email")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CandidateProfile extends BaseEntity {

    @Column(name = "full_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String fullName; // Họ tên ứng viên

    @Column(name = "email", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String email; // Email ứng viên

    @Column(name = "phone", columnDefinition = "NVARCHAR(50)")
    private String phone; // Số điện thoại

    @Column(name = "career_goal", columnDefinition = "NVARCHAR(MAX)")
    private String careerGoal; // Mục tiêu nghề nghiệp

    @Column(name = "education", columnDefinition = "NVARCHAR(255)")
    private String education; // Trình độ học vấn

    @Column(name = "experience_years")
    private int experienceYears; // Số năm kinh nghiệm

    @Column(name = "previous_company", columnDefinition = "NVARCHAR(255)")
    private String previousCompany; // Công ty gần nhất

    @Column(name = "certificates", columnDefinition = "NVARCHAR(MAX)")
    private String certificates; // Các chứng chỉ

    @ManyToMany
    @JoinTable(name = "candidate_skill",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills; // Danh sách kỹ năng
}

