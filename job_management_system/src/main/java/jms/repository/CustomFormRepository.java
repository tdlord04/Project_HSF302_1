package jms.repository;

import jms.entity.CustomForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomFormRepository extends JpaRepository<CustomForm, Long> {

    @Query("SELECT c FROM CustomForm c WHERE c.isActive =:isActive")
    List<CustomForm> findByActive(boolean isActive);

    List<CustomForm> findByJobPosting_Id(Long jobPostingId);
}
