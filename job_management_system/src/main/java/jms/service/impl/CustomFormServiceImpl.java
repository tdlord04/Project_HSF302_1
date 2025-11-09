package jms.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jms.dto.CustomFormDto;
import jms.entity.CustomForm;
import jms.entity.JobPosting;
import jms.mapper.CustomFormMapper;
import jms.repository.CustomFormRepository;
import jms.repository.JobPostingRepository;
import jms.service.CustomFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomFormServiceImpl implements CustomFormService {
    private final JobPostingRepository jobPostingRepository;
    private final CustomFormRepository customFormRepository;
    private final CustomFormMapper customFormMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CustomFormDto create(CustomFormDto dto) {
        JobPosting job = jobPostingRepository.findById(dto.getJobPostingId())
                .orElseThrow(() -> new RuntimeException("Job not found: " + dto.getJobPostingId()));

        // validate JSON (phải là mảng item có title)
        validateStructure(dto.getFormStructureJson());

        CustomForm entity = customFormMapper.toEntity(dto);
        entity.setJobPosting(job);

        CustomForm saved = customFormRepository.save(entity);
        return customFormMapper.toDto(saved);
    }

    @Override
    public CustomFormDto update(Long id, CustomFormDto dto) {
        CustomForm existing = customFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CustomForm not found: " + id));

        // validate JSON
        validateStructure(dto.getFormStructureJson());

        existing.setFormName(dto.getFormName());
        existing.setDescription(dto.getDescription());
        existing.setFormStructureJson(dto.getFormStructureJson());
        existing.setActive(dto.isActive());

        CustomForm saved = customFormRepository.save(existing);
        return customFormMapper.toDto(saved);
    }

    @Override
    public CustomFormDto getById(Long id) {
        return customFormRepository.findById(id)
                .map(customFormMapper::toDto)
                .orElseThrow(() -> new RuntimeException("CustomForm not found: " + id));
    }

    @Override
    public List<CustomFormDto> getByJobPostingId(Long jobPostingId) {
        return customFormRepository.findByJobPosting_Id(jobPostingId)
                .stream()
                .map(customFormMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        CustomForm form = customFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CustomForm not found with ID: " + id));
        customFormRepository.delete(form);
    }

    private void validateStructure(String json) {
        try {
            if (!StringUtils.hasText(json)) return; // cho phép rỗng (không có mục nào)
            List<Map<String, Object>> arr = objectMapper.readValue(json, new TypeReference<>() {});
            for (Map<String, Object> item : arr) {
                Object title = item.get("title");
                if (!(title instanceof String) || !StringUtils.hasText((String) title)) {
                    throw new IllegalArgumentException("Each item must contain non-empty 'title'.");
                }
                // description optional
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid formStructureJson: " + e.getMessage());
        }
    }
}
