package jms.service;

import jms.entity.Question;
import org.springframework.data.domain.Page;

public interface QuestionService {
	Page<Question> listPaged(String keyword, int page, int size);
	Question getById(Long id);
	Question create(Question question);
	Question update(Long id, Question question);
	void delete(Long id);
}


