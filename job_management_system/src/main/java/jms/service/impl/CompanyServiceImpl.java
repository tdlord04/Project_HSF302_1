package jms.service.impl;

import jms.dto.CompanyDto;
import jms.repository.CompanyRepository;
import jms.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Override
    public List<CompanyDto> getAll() {
        return companyRepository.findAll().stream()
                .map(c -> new CompanyDto(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }
}
