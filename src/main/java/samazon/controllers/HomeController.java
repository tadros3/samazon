package samazon.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import samazon.models.Product;
import samazon.models.User;
import samazon.services.UserService;
import samazon.validators.UserValidator;
@Controller
public class HomeController {
	
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private UserService userService;
	
    @RequestMapping("/")
    public String index(){
        return "homepage";
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    
    
    @RequestMapping(value="/addproduct", method = RequestMethod.GET)
    public String showaddproductPage(Model model){
        model.addAttribute("product", new Product());
        return "addproduct";
    }
    
    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }
    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        model.addAttribute("user", user);
        userValidator.validate(user, result);
        if (result.hasErrors()) {
            return "registration";
        } else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Successfully Created");
        }
        return "index";
    }
    
    @RequestMapping(value="/addproduct", method = RequestMethod.POST)
    public String processaddPage(@Valid @ModelAttribute("product") Product product, BindingResult result, Model model){
        model.addAttribute("product", product);
        //userValidator.validate(user, result);
        if (result.hasErrors()) {
            return "registration";
        } else {
            //userService.saveUser(product);
            model.addAttribute("message", "User Account Successfully Created");
        }
        return "addproduct";
    }
    public UserValidator getUserValidator() {
        return userValidator;
    }
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }
}