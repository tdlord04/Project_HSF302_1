package jms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "candidate_profile", indexes = {
        @Index(name = "idx_candidate_name", columnList = "full_name"),
        @Index(name = "idx_candidate_email", columnList = "email")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class CandidateProfile extends BaseEntity {

    @Column(name = "full_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String fullName;

    @Column(name = "email", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String email;

    @Column(name = "phone", columnDefinition = "NVARCHAR(50)")
    private String phone;

    @Column(name = "career_goal", columnDefinition = "NVARCHAR(MAX)")
    private String careerGoal;

    @Column(name = "education", columnDefinition = "NVARCHAR(255)")
    private String education;

    @Column(name = "experience_years")
    private int experienceYears;

    @Column(name = "previous_company", columnDefinition = "NVARCHAR(255)")
    private String previousCompany;

    @Column(name = "certificates", columnDefinition = "NVARCHAR(MAX)")
    private String certificates;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "candidate_skill",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @Builder.Default // THÊM @Builder.Default ĐỂ ĐẢM BẢO KHỞI TẠO
    private Set<Skill> skills = new HashSet<>();

    // THÊM METHOD RIÊNG ĐỂ SET DOCUMENTS
    // Đảm bảo trong CandidateProfile đã có relationship với Document
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Document> documents = new HashSet<>();

}