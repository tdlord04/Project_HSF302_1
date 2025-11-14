package jms.service.impl;

import jms.dto.JobPostingDetailDto;
import jms.dto.JobPostingDto;
import jms.entity.Company;
import jms.entity.JobPosting;
import jms.entity.enums.JobStatus;
import jms.mapper.JobPostingMapper;
import jms.repository.CompanyRepository;
import jms.repository.JobPostingRepository;
import jms.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingServiceImpl implements JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final JobPostingMapper jobPostingMapper;
    private final CompanyRepository companyRepository;

    @Override
    public List<JobPostingDto> getAll() {
        return jobPostingRepository.findAll().stream()
                .map(jobPostingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public JobPostingDto getById(Long id) {
        JobPosting posting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobPosting not found with id: " + id));
        return jobPostingMapper.toDTO(posting);
    }

    @Override
    public JobPostingDto create(JobPostingDto dto) {
        JobPosting entity = jobPostingMapper.toJobPosting(dto);
        if (dto.getCompanyId() != null) {
            Company company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy công ty với ID: " + dto.getCompanyId()));
            entity.setCompany(company);
        }
        JobPosting saved = jobPostingRepository.save(entity);
        return jobPostingMapper.toDTO(saved);
    }

    @Override
    public JobPostingDto update(Long id, JobPostingDto dto) {
        JobPosting existing = jobPostingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobPosting not found with id: " + id));
        existing.setJobTitle(dto.getJobTitle());
        existing.setDescription(dto.getDescription());
        existing.setRequirements(dto.getRequirements());
        existing.setLocation(dto.getLocation());
        existing.setSalaryRange(dto.getSalaryRange());
        existing.setStatus(dto.getStatus());

        if (dto.getCompanyId() != null) {
            Company company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy công ty với ID: " + dto.getCompanyId()));
            existing.setCompany(company);
        }

        JobPosting updated = jobPostingRepository.save(existing);
        return jobPostingMapper.toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!jobPostingRepository.existsById(id)) {
            throw new RuntimeException("JobPosting not found with id: " + id);
        }
        JobPosting j = jobPostingRepository.findById(id).orElse(null);
        j.setDeletedAt(Instant.now());
        jobPostingRepository.save(j);
    }

    @Override
    public List<JobPostingDto> searchByTitle(String keyword) {
        return jobPostingRepository.findByJobTitleContainingIgnoreCase(keyword)
                .stream().map(jobPostingMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<JobPostingDto> filterByStatus(JobStatus status) {
        return jobPostingRepository.findByStatus(status)
                .stream().map(jobPostingMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<JobPostingDto> advancedFilterPaged(String title, String location, String salaryRange,
                                                   JobStatus status, String requirements, String companyName,
                                                   int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<JobPosting> jobs = jobPostingRepository.advancedFilterPaged(
                title, location, salaryRange, status, requirements, companyName, pageable
        );
        return jobs.map(jobPostingMapper::toDTO);
    }


    @Override
    public JobPostingDetailDto getDetailById(Long id) {
        JobPosting job = jobPostingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tin tuyển dụng ID = " + id));
        return jobPostingMapper.toDetailDTO(job);
    }

    // Hàm phụ để tránh truyền chuỗi rỗng xuống query
    private String isEmpty(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }
}
