// SkillServiceImpl.java
package jms.service.impl;

import jms.entity.Skill;
import jms.repository.SkillRepository;
import jms.service.SkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Override
    public Skill createSkill(Skill skill) {
        log.info("Creating skill: {}", skill.getName());

        if (skillRepository.existsByName(skill.getName())) {
            throw new RuntimeException("Skill with name '" + skill.getName() + "' already exists");
        }

        return skillRepository.save(skill);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Skill> getSkillById(Long id) {
        log.info("Fetching skill by id: {}", id);
        return skillRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Skill> getAllSkills() {
        log.info("Fetching all skills");
        return skillRepository.findAll();
    }

    @Override
    public Skill updateSkill(Long id, Skill skill) {
        log.info("Updating skill with id: {}", id);

        return skillRepository.findById(id)
                .map(existingSkill -> {
                    existingSkill.setName(skill.getName());
                    return skillRepository.save(existingSkill);
                })
                .orElseThrow(() -> new RuntimeException("Skill not found with id: " + id));
    }

    @Override
    public void deleteSkill(Long id) {
        log.info("Deleting skill with id: {}", id);
        skillRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Skill> getSkillByName(String name) {
        log.info("Fetching skill by name: {}", name);
        return skillRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Skill> searchSkillsByName(String name) {
        log.info("Searching skills by name: {}", name);
        return skillRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Skill> getSkillsByNames(List<String> skillNames) {
        log.info("Fetching skills by names: {}", skillNames);
        return skillRepository.findByNames(skillNames);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean skillExists(String name) {
        return skillRepository.existsByName(name);
    }
}