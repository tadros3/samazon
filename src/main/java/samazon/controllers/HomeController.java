package samazon.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import samazon.models.LineItem;
import samazon.models.Order;
import samazon.models.Product;
import samazon.models.User;
import samazon.services.LineItemService;
import samazon.services.OrderService;
import samazon.services.ProductService;
import samazon.services.SSUserDetailsService;
import samazon.services.UserService;
import samazon.validators.ProductValidator;
import samazon.validators.UserValidator;
@Controller
public class HomeController {
	
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private ProductValidator prodValidator;
	@Autowired
	private UserService userService;
	@Autowired
	private ProductService prodService;
	@Autowired
	private OrderService ordService;
	@Autowired
	private LineItemService lService;
	//@Autowired
	//private SSUserDetailsService sservice;
	
    @RequestMapping("/")
    public String index(Model model){
    	model.addAttribute("prods", prodService.products());
        return "homepage";
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
   /* @RequestMapping("/productprofile")
    public String productprofile(){
        return "productprofile";
    }
    */
    
    @RequestMapping("/shoppingcart")
    public String shoppingcart(Principal principal,Model model,@Valid @ModelAttribute("pprof") Product product, BindingResult result){
    	System.out.println(principal.getName());
    	User user = userService.findByUsername(principal.getName());
    	System.out.println(product.getId());
    	//product=new Product();
    	//product.setId(1);
    	List<Order> orders= user.getOrders();
    	Order order = ordService.findByOpenOrder("true");
    	if(order==null)
    	{
    		order = new Order();
    		order.setOpenOrder("true");
    		//order.setUser(user);
    		user.getOrders().add(order);
    		user.setOrders(orders);
    		ordService.saveOrder(order);
    		userService.saveUser(user);
    	}
    	model.addAttribute("openorder",order);
    	LineItem litem = new LineItem();
    	litem.setOrder(order);
    	litem.setProduct(product);
    	order.addLineItem(litem);
        lService.saveLineItem(litem);
        ordService.saveOrder(order);
    	System.out.println(order.getLineItems());
        model.addAttribute("lineitems",order.getLineItems());
    	
        return "shoppingcart";
    }
    
    @RequestMapping(value="/productprofile/{pName}", method = RequestMethod.POST)
    public String showproductprofile(@Valid @ModelAttribute("prod") Product product, BindingResult result,Model model){
    	System.out.println(product.getPName());
    	System.out.println(product.getLDesc());
        System.out.println(product.getSDesc());
    	model.addAttribute("pprof", product);
        return "productprofile";
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
        System.out.println(product.getLDesc());
        System.out.println(product.getPName());
        System.out.println(product.getSDesc());
        prodValidator.validate(product, result);
        if (result.hasErrors()) {
            return "addproduct";
        } else {
            prodService.saveProduct(product);
            model.addAttribute("message", "Product Successfully Created");
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