package com.askAnything.web;

import java.util.ArrayList;
import java.util.List;

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
  
  @GetMapping("/login")
  public String login(){
    return "/user/login";
  }
  
  @GetMapping("/{id}/form")
  public String updateForm(@PathVariable Long id, Model model){
    User user = userRepository.findOne(id);
    model.addAttribute("user", user);
    return "/user/updateForm";
  }
  
  @PutMapping("/{id}")
  public String update(@PathVariable Long id, User newUser){
    User user = userRepository.findOne(id);
    user.update(newUser);
    userRepository.save(user);
    return "redirect:/users";
  }
}
