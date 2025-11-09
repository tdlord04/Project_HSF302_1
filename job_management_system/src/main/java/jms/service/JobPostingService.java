package jms.service;

import jms.dto.JobPostingDetailDto;
import jms.dto.JobPostingDto;
import jms.entity.enums.JobStatus;

import java.util.List;

public interface JobPostingService {
    List<JobPostingDto> getAll();
    JobPostingDto getById(Long id);
    JobPostingDto create(JobPostingDto dto);
    JobPostingDto update(Long id, JobPostingDto dto);
    void delete(Long id);
    List<JobPostingDto> searchByTitle(String keyword);
    List<JobPostingDto> filterByStatus(JobStatus status);
    List<JobPostingDto> advancedFilter(String title, String location, String salaryRange,
                                       JobStatus status, String requirements, String companyName);

    JobPostingDetailDto getDetailById(Long id);
}
