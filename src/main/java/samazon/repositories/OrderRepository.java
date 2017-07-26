package samazon.repositories;

import org.springframework.data.repository.CrudRepository;

import samazon.models.Order;
import samazon.models.Product;
import samazon.models.User;

public interface OrderRepository  extends CrudRepository<Order, Long>{
	
	Order findByOpenOrder(String open);
	Order findByOpenOrderAndUser(String open, User user);

}
