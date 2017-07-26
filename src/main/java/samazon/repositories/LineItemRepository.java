package samazon.repositories;

import org.springframework.data.repository.CrudRepository;

import samazon.models.LineItem;
import samazon.models.Order;
import samazon.models.Product;

public interface LineItemRepository  extends CrudRepository<LineItem, Long>{
		
	//LineItem findByOrderId(Long orderId);
	//LineItem findByProductIdAndOrderId(Long ,Long orderId);
	
	LineItem findByOrderAndProduct(Order order, Product product);
}
