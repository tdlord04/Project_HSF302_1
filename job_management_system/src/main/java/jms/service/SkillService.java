// SkillService.java
package jms.service;

import jms.entity.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillService {
    Skill createSkill(Skill skill);
    Optional<Skill> getSkillById(Long id);
    List<Skill> getAllSkills();
    Skill updateSkill(Long id, Skill skill);
    void deleteSkill(Long id);
    Optional<Skill> getSkillByName(String name);
    List<Skill> searchSkillsByName(String name);
    List<Skill> getSkillsByNames(List<String> skillNames);
    boolean skillExists(String name);
}