package samazon.repositories;

import org.springframework.data.repository.CrudRepository;

import samazon.models.LineItem;
import samazon.models.Order;
import samazon.models.Product;

public interface LineItemRepository  extends CrudRepository<LineItem, Long>{
		
	LineItem findByOrder(Order order);
	LineItem findByProductAndOrder(Product prod,Order order);
	
	
}
