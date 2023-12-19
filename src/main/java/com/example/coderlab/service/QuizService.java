package com.example.coderlab.service;

import com.example.coderlab.entity.Language;
import com.example.coderlab.entity.Level;
import com.example.coderlab.entity.Question;
import com.example.coderlab.repository.LanguageRepository;
import com.example.coderlab.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    public void save(Question question) {
        quizRepository.save(question);
    }

    public List<Question> getAllQuizzs() {
        return quizRepository.findAll();
    }

    public List<Question> getRandomQuizs(Integer numberOfQuiz, Language language, Level level) {
        if (language == null) {
            return quizRepository.getRandomProblemSolvingQuiz(level.getId(), numberOfQuiz);
        }
        return quizRepository.getRandomQuizs(language.getId(), level.getId(), numberOfQuiz);
    }

    public Question findQuestionById(String quizId) {
        Optional<Question> foundQuestion = quizRepository.findById(Long.valueOf(quizId));
        if (foundQuestion.isPresent()) {
            return foundQuestion.get();
        }else {return null;}
    }

    public List<Question> getRandomQuizs(Integer number) {
        return quizRepository.getRandomQuizs(number);
    }
}
