// SkillRepository.java
package jms.repository;

import jms.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByName(String name);

    List<Skill> findByNameContainingIgnoreCase(String name);

    @Query("SELECT s FROM Skill s WHERE LOWER(s.name) IN :skillNames")
    List<Skill> findByNames(@Param("skillNames") List<String> skillNames);

    boolean existsByName(String name);
}