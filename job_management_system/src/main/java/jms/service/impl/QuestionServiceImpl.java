package jms.service.impl;

import jms.entity.Question;
import jms.repository.QuestionRepository;
import jms.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

	private final QuestionRepository questionRepository;

	@Override
	public Page<Question> listPaged(String keyword, int page, int size) {
		Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
		if (keyword == null || keyword.isBlank()) {
			return questionRepository.findAll(pageable);
		}
		// Manual filter with keyword then page in memory for simplicity when custom query missing
		// But better approach is to add a repository method with Pageable; keep simple here:
		var all = questionRepository.findByQuestionTextContainingIgnoreCase(keyword);
		int from = Math.min(page * size, all.size());
		int to = Math.min(from + size, all.size());
		return new org.springframework.data.domain.PageImpl<>(all.subList(from, to), pageable, all.size());
	}

	@Override
	public Question getById(Long id) {
		return questionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
	}

	@Override
	public Question create(Question question) {
		return questionRepository.save(question);
	}

	@Override
	public Question update(Long id, Question question) {
		Question existing = getById(id);
		existing.setQuestionText(question.getQuestionText());
		existing.setCorrectAnswer(question.getCorrectAnswer());
		existing.setQuestionType(question.getQuestionType());
		return questionRepository.save(existing);
	}

	@Override
	public void delete(Long id) {
		if (!questionRepository.existsById(id)) {
			throw new RuntimeException("Question not found with id: " + id);
		}
		questionRepository.deleteById(id);
	}

	@Override
	public List<Question> getRandomQuestions(int count) {
		List<Question> allQuestions = questionRepository.findRandomQuestions();
		if (allQuestions.size() <= count) {
			return allQuestions;
		}
		return allQuestions.subList(0, Math.min(count, allQuestions.size()));
	}
}