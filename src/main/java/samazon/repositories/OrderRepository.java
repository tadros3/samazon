package samazon.repositories;

import org.springframework.data.repository.CrudRepository;

import samazon.models.Order;
import samazon.models.Product;

public interface OrderRepository  extends CrudRepository<Order, Long>{
	
	Order findByOpenOrder(String open);

}
