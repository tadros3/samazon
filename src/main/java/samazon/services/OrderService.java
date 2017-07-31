package samazon.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import samazon.models.Order;
import samazon.models.User;
import samazon.repositories.OrderRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository order;
	
	public Order findByOpenOrder(String open)
	{
		return order.findByOpenOrder(open);
	}
	
	public void saveOrder(Order ord)
	{
		order.save(ord);
	}
	
	public List<Order>  findByOpenOrderAndUser(String open, User user) {
		return order.findByOpenOrderAndUser(open, user);
	}
	
	public List<Order> findByUser(User user)
	{
		return order.findByUser(user);
	}
	
	

}
