package jms.repository;

import jms.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	List<Question> findByQuestionTextContainingIgnoreCase(String keyword);

	@Query(value = "SELECT TOP 1000 * FROM question ORDER BY NEWID()", nativeQuery = true)
	List<Question> findRandomQuestions();
}