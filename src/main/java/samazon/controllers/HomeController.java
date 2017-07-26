package samazon.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;

import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
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
	@Autowired
	public EmailService emailService;
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
    
    @RequestMapping("/orderconfirmation")
    public String orderconfirmation(@Valid @ModelAttribute("lineitems") ArrayList<LineItem> litem,Model model){
    	model.addAttribute("lineitems",litem);
        return "orderconfirmation";
    }
    
    @RequestMapping("/ordersuccess")
    public String ordersuccess(Principal principal,@RequestParam("input_address") String address,@RequestParam("input_email") String email) throws UnsupportedEncodingException{
    	User user = userService.findByUsername(principal.getName());
    	System.out.println(user.getUsername());
    	int order_num=0;
    	if(user.getOrders()!=null)
    	{
	    	List<Order> ord = user.getOrders(); 
	    	int i=0;
	    	for(;i<ord.size();i++)
	    	{
	    		if(ord.get(i).getOpenOrder().equals("true"))
	    		{
	    			System.out.println(ord.get(i).getOpenOrder());
	    			break;
	    		}
	    	}
	    	Order order = ord.get(i);
	    	order.setOpenOrder("false");
	    	order.setPaymentMethod("Credit Card");
	    	order.setShippingAddress(address);
	    	order_num=ord.get(i).getLineItems().size();
	    	ordService.saveOrder(order);
    	}
    	System.out.println(address);
    	sendEmailWithoutTemplating(user.getUsername(),email,order_num); 
        return "ordersuccess";
    }
    
    @RequestMapping("/shoppingcart")
    public String shoppingcart(Principal principal,Model model,@Valid @ModelAttribute("pprof") Product product, @RequestParam("quantity") long quantity, BindingResult result){
    	System.out.println(principal.getName());
    	User user = userService.findByUsername(principal.getName());
    	System.out.println(product.getId());
    	List<Order> orders= user.getOrders();
    	Order order = ordService.findByOpenOrder("true");
    	if(order==null)
    	{
    		order = new Order();
    		order.setOpenOrder("true");
    		order.setUser(user);
    		user.addOrder(order);
    		ordService.saveOrder(order);
    		userService.saveUser(user);
    	}
    	model.addAttribute("openorder",order);
    	LineItem litem = new LineItem();
    	litem.setOrder(order);
    	litem.setProduct(product);
    	litem.setQuantity(quantity);
    	order.addLineItem(litem);
    	product.addLineItem(litem);
        lService.saveLineItem(litem);
        prodService.saveProduct(product);
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
    
    public void sendEmailWithoutTemplating(String username,String useremail,int order_num) throws UnsupportedEncodingException{

		  final Email email = DefaultEmail.builder()

		        .from(new InternetAddress("meensat3@gmail.com", "Samazon Admin"))

		        .to(Lists.newArrayList(new InternetAddress(useremail, username)))

		        .subject("Your Order in Samazon")

		        .body("Hello "+username+", Thanks for ordering with Samazon.You have ordered "+order_num+" item")

		        .encoding("UTF-8").build();

		  emailService.send(email);

		}
	
}