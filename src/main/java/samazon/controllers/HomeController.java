package samazon.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
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
    
    @RequestMapping("/userprofile")
    public String userprofile(Principal principal,Model model){
    	User user = userService.findByUsername(principal.getName());
    	//System.out.println(user.getUsername());
    	model.addAttribute("user", user);
        return "userprofile";
    }
    
    @RequestMapping("/orderhistory")
    public String orderhistory(Principal principal,Model model){
    	User user = userService.findByUsername(principal.getName());
    	//System.out.println(user.getUsername());
    	//List<Order> ord = ordService.findByUser(user);
    	List<Order> ord = ordService.findByOpenOrderAndUser("false",user);
    	model.addAttribute("orders", ord);
        return "orderhistory";
    }
   /* @RequestMapping("/productprofile")
    public String productprofile(){
        return "productprofile";
    }
    */
    
    @RequestMapping("/orderconfirmation")
    public String orderconfirmation(@Valid @ModelAttribute("litem") LineItem litem,Model model){
    	Order order = litem.getOrder();
    	List<LineItem> litems = order.getLineItems();
    	model.addAttribute("total", calcTotalPrice(litems));
    	model.addAttribute("lineitems",litems);
        return "orderconfirmation";
    }
    
    @RequestMapping(value="/shoppingcart", method = RequestMethod.GET)
    public String showshoppingcart(Principal principal, Model model){
    	User user = userService.findByUsername(principal.getName());
    	Order order = ordService.findByOpenOrderAndUser("true", user).get(0);
    	double total = 0.0;
    	if (order != null && order.getLineItems() != null) {
    		model.addAttribute("lineitems",order.getLineItems());
    		total = calcTotalPrice(order.getLineItems());
    	}
    	model.addAttribute("total",total);
    	model.addAttribute("litem", order.getLineItems().get(0));
        return "shoppingcart";
    }
    
    @RequestMapping("/deletefromcart")
    public String deletefromcart(Principal principal, Model model, @Valid @ModelAttribute("lineitem") LineItem litem, BindingResult result) {
    	litem.setDeleted("true");
    	lService.saveLineItem(litem);
    	String sc = showshoppingcart(principal,model);
    	return "redirect:/" + sc;
    }
    
    private double calcTotalPrice(List<LineItem> litems) {
    	double totalPrice = 0.0;
    	for (LineItem litem: litems) {
    		if (litem.getDeleted().equals("false")) {
    			totalPrice += litem.getQuantity() * litem.getProduct().getPrice();
    		}
    	}
    	return totalPrice;
    }
    @RequestMapping("/ordersuccess")
    public String ordersuccess(Principal principal,@RequestParam("input_address") String address,@RequestParam("input_email") String email,Model model) throws UnsupportedEncodingException, CannotSendEmailException{
    	User user = userService.findByUsername(principal.getName());
    	System.out.println(user.getUsername());
    	int order_num=0;
    	Order order=new Order();
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
	    	 order = ord.get(i);
	    	order.setOpenOrder("false");
	    	order.setPaymentMethod("Credit Card");
	    	order.setShippingAddress(address);
	    	order_num=ord.get(i).getLineItems().size();
	    	for (LineItem litem: order.getLineItems()) {
	    		Product prod = litem.getProduct();
	    		prod.setInStock(prod.getInStock() - litem.getQuantity());
	    		prodService.saveProduct(prod);
	    	}
	    	ordService.saveOrder(order);
	    	//model.addAttribute("eorder",order);
    	}
    	System.out.println(address);
    	//sendEmailWithoutTemplating(user.getUsername(),email,order_num); 
    	
    	sendEmailWithTemplating(user.getUsername(),email,order); 
        return "ordersuccess";
    }
    
    @RequestMapping("/shoppingcart")
    public String shoppingcart(Principal principal,Model model,@Valid @ModelAttribute("pprof") Product product, @RequestParam("quantity") long quantity, BindingResult result){
    	System.out.println(principal.getName());
    	User user = userService.findByUsername(principal.getName());
    	System.out.println(product.getId());
    	//List<Order> orders= user.getOrders();
    	//Order order = ordService.findByOpenOrder("true");
    	List <Order> orders = ordService.findByOpenOrderAndUser("true", user);
    	Order order;
    	if(orders.size()==0)
    	{
    		order = new Order();
    		order.setOpenOrder("true");
    		order.setUser(user);
    		/*if(order==null)
    			System.out.println("order null");
    		if(user==null)
    			System.out.println("user null");*/
    		user.addOrder(order);
    		ordService.saveOrder(order);
    		userService.saveUser(user);
    	}
    	else
    	{
    		order=orders.get(0);
    	}
    	model.addAttribute("openorder",order);
    	LineItem litem = lService.findByOrderAndProduct(order, product);
    	if (litem == null) {
    		litem = new LineItem();
    		litem.setOrder(order);
        	litem.setProduct(product);
        	litem.setQuantity(quantity);
        	order.addLineItem(litem);
        	product.addLineItem(litem);
    	}
    	else {
    		litem.setQuantity(quantity + litem.getQuantity());
    		litem.setDeleted("false");
    	}
    	System.out.println(order.getLineItems().get(0));
        lService.saveLineItem(litem);
        prodService.saveProduct(product);
        ordService.saveOrder(order);
        
    	System.out.println(order.getLineItems());
        model.addAttribute("lineitems",order.getLineItems());
    	double total = calcTotalPrice(order.getLineItems());
    	model.addAttribute("total",total);
    	
    	model.addAttribute("litem", order.getLineItems().get(0));
        return "shoppingcart";
    }
    
    @RequestMapping(value="/productprofile/{pName}", method = RequestMethod.POST)
    public String showproductprofile(@Valid @ModelAttribute("prod") Product product, BindingResult result,Model model){
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
    public String processaddPage(@Valid @ModelAttribute("product") Product product, BindingResult result, Model model, @RequestParam("ProductMethod") String num){
    	Product prod = prodService.findByPName(product.getPName());
    	if (num.equals("1")) {
        	model.addAttribute("product", product);
            prodValidator.validate(product, result);
            if (result.hasErrors()) {
                return "addproduct";
            } else {
                prodService.saveProduct(product);
                model.addAttribute("message", "Product Successfully Created");
            }
        } else if (num.equals("2")) {
        	if (prod != null && prod.getActive().equals("active")) {
        		System.out.println(prod.getPName() + ": " + prod.getSDesc());
        		System.out.println(product.getInStock());
        		if (product.getInStock() != 0) {
            		long stock = product.getInStock() == -1 ? 0 : product.getInStock();
            		prod.setInStock(stock);
            	}
            	if (!product.getSDesc().equals("")) {
            		prod.setSDesc(product.getSDesc());
            	}
            	if (!product.getLDesc().equals("")) {
            		prod.setLDesc(product.getLDesc());
            	}
            	if (!product.getSImage().equals("")) {
            		prod.setSImage(product.getSImage());
            	}
            	if (!product.getLImage().equals("")) {
            		prod.setLImage(product.getLImage());
            	}
            	if (product.getPrice() != 0) {
            		double price = product.getPrice() == -1 ? 0 : product.getPrice();
            		prod.setPrice(price);
            	}
            	prodService.saveProduct(prod);
            	model.addAttribute("message", "Product successfully updated!");
        	} else if (prod != null && prod.getActive().equals("inactive")) {
        		model.addAttribute("message","Product has been deleted. Please select 'Add Product' to re-add the product to your database.");
        		return "addproduct";
        	} else {
        		model.addAttribute("message", "Product does not exist. Please select 'Add Product' to add the product to your datablase.");
        		return "addproduct";
        	}
        } else {
        	if (prod != null && prod.getActive().equals("active")) {
        		prod.setActive("inactive");
        		prodService.saveProduct(prod);
        		model.addAttribute("message", "Product successfully deleted!");
        	} else if (prod != null && prod.getActive().equals("inactive")) {
        		model.addAttribute("message", "You have already deleted this product. You'll have to add it again if you really want to delete it right now.");
        		return "addproduct";
        	} else {
        		model.addAttribute("message", "This product does not exist. Please remember to add products before you try to delete them.");
        		return "addproduct";
        	}
        }
    	return "productsuccess";
    }
    public UserValidator getUserValidator() {
        return userValidator;
    }
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }
    
    public void sendEmailWithoutTemplating(String username,String useremail,int order_num) throws UnsupportedEncodingException{

		  final Email email = DefaultEmail.builder()

		        .from(new InternetAddress("samazon.infosys@gmail.com", "Samazon Admin"))

		        .to(Lists.newArrayList(new InternetAddress(useremail, username)))

		        .subject("Your Order in Samazon")

		        .body("Hello "+username+", Thanks for ordering with Samazon.You have ordered "+order_num+" item")

		        .encoding("UTF-8").build();

		  emailService.send(email);

		}
    
    public void sendEmailWithTemplating(String username,String useremail,Order ord) throws UnsupportedEncodingException, CannotSendEmailException{
 	   final Email email = DefaultEmail.builder()
 	        .from(new InternetAddress("samazon.infosys@gmail.com", "Samazon Admin"))
 	        .to(Lists.newArrayList(new InternetAddress(useremail, username)))
 	        .subject("Samazon Order")
 	        .body("Hello "+username+", Thanks for ordering with Samazon.You have ordered "+" item")
 	        .encoding("UTF-8").build();
 	   final Map<String, Object> modelObject = new HashMap<>();
 	   modelObject.put("eorder", ord);
 	   modelObject.put("recipient",username);
 	 
 	   emailService.send(email, "emailtemp", modelObject);
 	}
	
}