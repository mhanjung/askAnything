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
  public String login(String userId, String password, HttpSession session){
    User user = userRepository.findByUserId(userId);
    if(user == null){
      System.out.println("Login Failure!");
      return "redirect:/users/loginForm";
    }
    
    if(!password.equals(user.getPassword())){
      System.out.println("Login Failure!");
      return "redirect:/users/loginForm";
    }
    System.out.println("Login Success!");
    session.setAttribute("sessionUser", user);
    
    return "redirect:/";
  }
  
  @GetMapping("/logout")
  public String logout(HttpSession session){
    session.removeAttribute("sessionUser");
    return "redirect:/";
  }
  
  @GetMapping("/{id}/form")
  public String updateForm(@PathVariable Long id, Model model, HttpSession session){
    Object tempUser = session.getAttribute("sessionUser");
    if(tempUser == null){
      return "redirect:/users/loginForm";
    }
    
    User sessionUser = (User)tempUser;
    if(!id.equals(sessionUser.getId())){
      throw new IllegalStateException("You can only edit your profile.");
    }
    
    User user = userRepository.findOne(id);
    model.addAttribute("user", user);
    return "/user/updateForm";
  }
  
  @PutMapping("/{id}")
  public String update(@PathVariable Long id, User updatedUser, HttpSession session){
    Object tempUser = session.getAttribute("sessionUser");
    if(tempUser == null){
      return "redirect:/users/loginForm";
    }
    
    User sessionUser = (User)tempUser;
    if(!id.equals(sessionUser.getId())){
      throw new IllegalStateException("You can only edit your profile.");
    }
    
    User user = userRepository.findOne(id);
    user.update(updatedUser);
    userRepository.save(user);
    return "redirect:/users";
  }
}
