// InterviewMapper.java
package jms.mapper;

import jms.entity.Interview;
import jms.dto.InterviewDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

    // Convert Entity to DTO
    @Mapping(target = "applicationId", expression = "java(interview.getApplication() != null ? interview.getApplication().getId() : null)")
    @Mapping(target = "candidateName", expression = "java(getCandidateName(interview))")
    @Mapping(target = "jobTitle", expression = "java(getJobTitle(interview))")
    @Mapping(target = "interviewerId", expression = "java(interview.getInterviewer() != null ? interview.getInterviewer().getId() : null)")
    @Mapping(target = "interviewerName", expression = "java(getInterviewerName(interview))")
    InterviewDTO toDTO(Interview interview);

    // Convert DTO to Entity - ignore relationships
    @Mapping(target = "application", ignore = true)
    @Mapping(target = "interviewer", ignore = true)
    Interview toEntity(InterviewDTO interviewDTO);

    // For update operations
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "application", ignore = true)
    @Mapping(target = "interviewer", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateInterviewFromDTO(InterviewDTO interviewDTO, @MappingTarget Interview interview);

    // Custom default methods
    default String getCandidateName(Interview interview) {
        if (interview.getApplication() != null &&
                interview.getApplication().getCandidate() != null) {
            return interview.getApplication().getCandidate().getFullName();
        }
        return null;
    }

    default String getJobTitle(Interview interview) {
        if (interview.getApplication() != null &&
                interview.getApplication().getJobPosting() != null) {
            // Giả sử JobPosting có phương thức getTitle()
            return interview.getApplication().getJobPosting().getJobTitle();
        }
        return null;
    }

    default String getInterviewerName(Interview interview) {
        if (interview.getInterviewer() != null) {
            return interview.getInterviewer().getFullName();
        }
        return null;
    }
}