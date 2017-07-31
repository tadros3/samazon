package samazon.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import samazon.models.Product;
import samazon.models.Review;

public interface ReviewRepository extends CrudRepository<Review,Long> {

	public List<Review> findByProduct(Product product);
}
