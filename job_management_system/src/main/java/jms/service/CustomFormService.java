package jms.service;

import jms.dto.CustomFormDto;

import java.util.List;

public interface CustomFormService {
    CustomFormDto create(CustomFormDto dto);
    CustomFormDto update(Long id, CustomFormDto dto);
    CustomFormDto getById(Long id);
    List<CustomFormDto> getByJobPostingId(Long jobPostingId);
    void deleteById(Long id);
}
