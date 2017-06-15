package com.askAnything.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.askAnything.domain.Question;
import com.askAnything.domain.QuestionRepository;
import com.askAnything.domain.Result;
import com.askAnything.domain.User;

@Controller
@RequestMapping("/questions")
public class QuestionController {
  
  @Autowired
  private QuestionRepository questionRepository;
  
  @GetMapping("form")
  public String form(HttpSession session, Model model){
    if(!HttpSessionUtils.isLoginUser(session)){
      model.addAttribute("errorMessage","You need to Sign in.");
      return "/user/login";
    }
    return "/qna/form";
  }
  
  @PostMapping("")
  public String create(String title, String contents, Model model, HttpSession session){
    if(!HttpSessionUtils.isLoginUser(session)){
      model.addAttribute("errorMessage","You need to Sign in.");
      return "/user/login";
    }
    
    User sessionUser = HttpSessionUtils.getUserFromSession(session);
    Question newQuestion = new Question(sessionUser, title, contents);
    questionRepository.save(newQuestion);
    return "redirect:/";
  }
  
  @GetMapping("/{id}")
  public String show(@PathVariable Long id, Model model){
    model.addAttribute("question", questionRepository.findOne(id));
    return "/qna/show";
  }
  
  @GetMapping("/{id}/form")
  public String updateForm(@PathVariable Long id, Model model, HttpSession session){
    Question question = questionRepository.findOne(id);
    Result result = valid(session, question);
    
    if(!result.isValid()){
      model.addAttribute("errorMessage",result.getErrorMessage());
      return "/user/login";
    }
    
    model.addAttribute("question", question);
    return "/qna/updateForm";
  }
  
  private Result valid(HttpSession session, Question question){
    if(!HttpSessionUtils.isLoginUser(session)){
      return Result.fail("You need to Sign in.");
    }
    User loginUser = HttpSessionUtils.getUserFromSession(session);
    if(!question.isSameWriter(loginUser)){
      return Result.fail("You can only edit or delete your owns.");
    }
    return Result.ok();
  }
  
  @PutMapping("/{id}")
  public String update(@PathVariable Long id, String title, String contents, Model model, HttpSession session){
    Question question = questionRepository.findOne(id);
    Result result = valid(session, question);
    if(!result.isValid()){
      model.addAttribute("errorMessage",result.getErrorMessage());
      return "/user/login";
    }

    question.update(title, contents);
    questionRepository.save(question);
    return String.format("redirect:/questions/%d", id);
  }
  
  @DeleteMapping("/{id}")
  public String delete(@PathVariable Long id, Model model, HttpSession session){
    Question question = questionRepository.findOne(id);
    Result result = valid(session, question);
    if(!result.isValid()){
      model.addAttribute("errorMessage",result.getErrorMessage());
      return "/user/login";
    }
    questionRepository.delete(id);
    return "redirect:/";
  }
}
