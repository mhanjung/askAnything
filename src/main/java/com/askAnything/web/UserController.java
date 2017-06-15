package com.askAnything.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.askAnything.domain.User;
import com.askAnything.domain.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserRepository userRepository;
  
  @PostMapping("")
  public String create(User user){
    System.out.println("user : " + user);
    userRepository.save(user);
    return "redirect:/users";
  }
  
  @GetMapping("")
  public String list(Model model){
    model.addAttribute("users", userRepository.findAll());
    return "/user/list";
  }
  
  @GetMapping("/form")
  public String form(){
    return "/user/form";
  }
  
  @GetMapping("/loginForm")
  public String loginForm(){
    return "/user/login";
  }
  
  @PostMapping("/login")
  public String login(String userId, String password, Model model, HttpSession session){
    User user = userRepository.findByUserId(userId);
    if(user == null){
      System.out.println("Login Failure!");
      model.addAttribute("errorMessage","Your ID doesn't exist.");
      return "/user/login";
    }
    
    if(!user.matchPassword(password)){
      System.out.println("Login Failure!");
      model.addAttribute("errorMessage","Check your password.");
      return "/user/login";
    }
    System.out.println("Login Success!");
    session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
    
    return "redirect:/";
  }
  
  @GetMapping("/logout")
  public String logout(HttpSession session){
    session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
    return "redirect:/";
  }
  
  @GetMapping("/{id}/form")
  public String updateForm(@PathVariable Long id, Model model, HttpSession session){
    if(!HttpSessionUtils.isLoginUser(session)){
      model.addAttribute("errorMessage","You need to Sign in.");
      return "/user/login";
    }
    
    User sessionUser = HttpSessionUtils.getUserFromSession(session);
    if(!sessionUser.matchId(id)){
      model.addAttribute("errorMessage","You can only edit or delete your owns.");
      return "/user/login";
    }
    
    User user = userRepository.findOne(id);
    model.addAttribute("user", user);
    return "/user/updateForm";
  }
  
  @PutMapping("/{id}")
  public String update(@PathVariable Long id, User updatedUser, Model model, HttpSession session){
    if(!HttpSessionUtils.isLoginUser(session)){
      model.addAttribute("errorMessage","You need to Sign in.");
      return "/user/login";
    }
    
    User sessionUser = HttpSessionUtils.getUserFromSession(session);
    if(!sessionUser.matchId(id)){
      model.addAttribute("errorMessage","You can only edit or delete your owns.");
      return "/user/login";
    }
    
    User user = userRepository.findOne(id);
    user.update(updatedUser);
    userRepository.save(user);
    return "redirect:/users";
  }
}
