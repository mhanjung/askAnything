package com.askAnything.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.askAnything.domain.Answer;
import com.askAnything.domain.AnswerRepository;
import com.askAnything.domain.Question;
import com.askAnything.domain.QuestionRepository;
import com.askAnything.domain.User;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {
  @Autowired
  private QuestionRepository questionRepository;
  
  @Autowired
  private AnswerRepository answerRepository;
  
  @PostMapping("")
  public Answer create(@PathVariable Long questionId, String contents, Model model, HttpSession session){
    if(!HttpSessionUtils.isLoginUser(session)){
      model.addAttribute("errorMessage","You need to Sign in.");
      return null;
    }
    
    User loginUser = HttpSessionUtils.getUserFromSession(session);
    Question question = questionRepository.findOne(questionId);
    Answer answer = new Answer(loginUser, question, contents);
    return answerRepository.save(answer);
  }
}
