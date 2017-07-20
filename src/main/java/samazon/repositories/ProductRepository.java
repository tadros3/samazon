package samazon.repositories;

import org.springframework.data.repository.CrudRepository;
import samazon.models.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{
    Product findByPName(String pName);
    long countByPName(String pName);
}