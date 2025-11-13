package jms.controller.question;

import jakarta.validation.Valid;
import jms.entity.Question;
import jms.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('INTERVIEWER', 'ADMIN')")
public class QuestionController {

	private final QuestionService questionService;

	public static class TestResult {
		public String givenAnswer;
		public String expectedAnswer;
		public boolean correct;
	}

	@GetMapping
	public String list(
		@RequestParam(value = "keyword", required = false) String keyword,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "20") int size,
		Model model
	) {
		Page<Question> questionPage = questionService.listPaged(keyword, page, size);
		model.addAttribute("questionPage", questionPage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		return "question/question-list";
	}

	@GetMapping("/create")
	public String showCreateForm(Model model) {
		if (!model.containsAttribute("question")) {
			model.addAttribute("question", new Question());
		}
		return "question/question-form";
	}

	@PostMapping("/create")
	public String create(
		@Valid @ModelAttribute("question") Question question,
		BindingResult bindingResult,
		Model model
	) {
		if (bindingResult.hasErrors()) {
			return "question/question-form";
		}
		questionService.create(question);
		return "redirect:/questions?success";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Question question = questionService.getById(id);
		model.addAttribute("question", question);
		return "question/question-form";
	}

	@PostMapping("/edit/{id}")
	public String update(
		@PathVariable Long id,
		@Valid @ModelAttribute("question") Question question,
		BindingResult bindingResult,
		Model model
	) {
		if (bindingResult.hasErrors()) {
			return "question/question-form";
		}
		questionService.update(id, question);
		return "redirect:/questions?updated";
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		questionService.delete(id);
		return "redirect:/questions?deleted";
	}

	@GetMapping("/test/{id}")
	public String showTest(@PathVariable Long id, Model model) {
		Question question = questionService.getById(id);
		model.addAttribute("question", question);
		return "question/question-test";
	}

	@PostMapping("/test/{id}")
	public String doTest(
		@PathVariable Long id,
		@RequestParam("answer") String answer,
		Model model
	) {
		Question question = questionService.getById(id);
		TestResult result = new TestResult();
		result.givenAnswer = answer == null ? "" : answer;
		result.expectedAnswer = question.getCorrectAnswer();
		String expected = result.expectedAnswer == null ? "" : result.expectedAnswer;
		// So sánh đơn giản: trim + ignore case
		result.correct = !expected.isEmpty() &&
			(expected.trim().equalsIgnoreCase(result.givenAnswer.trim()));

		model.addAttribute("question", question);
		model.addAttribute("result", result);
		model.addAttribute("answer", result.givenAnswer);
		return "question/question-test";
	}

	// API endpoint để lấy 5 câu hỏi ngẫu nhiên
	@GetMapping("/api/random")
	@ResponseBody
	public ResponseEntity<List<Question>> getRandomQuestions(
		@RequestParam(value = "count", defaultValue = "5") int count
	) {
		List<Question> questions = questionService.getRandomQuestions(count);
		return ResponseEntity.ok(questions);
	}

	// API endpoint để submit kết quả test
	@PostMapping("/api/submit-test")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> submitTest(
		@RequestBody Map<String, Object> request
	) {
		@SuppressWarnings("unchecked")
		List<Map<String, String>> answers = (List<Map<String, String>>) request.get("answers");
		
		int correctCount = 0;
		int totalQuestions = answers != null ? answers.size() : 0;
		List<Map<String, Object>> results = new java.util.ArrayList<>();

		if (answers != null) {
			for (Map<String, String> answer : answers) {
				Long questionId = Long.parseLong(answer.get("questionId"));
				String givenAnswer = answer.get("answer") != null ? answer.get("answer") : "";
				
				Question question = questionService.getById(questionId);
				String expectedAnswer = question.getCorrectAnswer() != null ? question.getCorrectAnswer() : "";
				
				boolean isCorrect = !expectedAnswer.isEmpty() &&
					expectedAnswer.trim().equalsIgnoreCase(givenAnswer.trim());
				
				if (isCorrect) {
					correctCount++;
				}

				Map<String, Object> result = new HashMap<>();
				result.put("questionId", questionId);
				result.put("questionText", question.getQuestionText());
				result.put("givenAnswer", givenAnswer);
				result.put("correctAnswer", expectedAnswer);
				result.put("correct", isCorrect);
				results.add(result);
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("totalQuestions", totalQuestions);
		response.put("correctCount", correctCount);
		response.put("score", totalQuestions > 0 ? (correctCount * 100.0 / totalQuestions) : 0);
		response.put("results", results);

		return ResponseEntity.ok(response);
	}
}


