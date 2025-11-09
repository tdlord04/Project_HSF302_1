package jms.service;

import jms.dto.CompanyDto;

import java.util.List;

public interface CompanyService {
    List<CompanyDto> getAll();
}
