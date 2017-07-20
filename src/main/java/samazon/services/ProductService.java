package samazon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import samazon.models.Product;
import samazon.repositories.ProductRepository;
@Service
public class ProductService {
    @Autowired
    ProductRepository prodRepository;
    @Autowired
    public ProductService(ProductRepository prodRepository) {
        this.prodRepository = prodRepository;
    }
    public Product findByPName(String pName) {
        return prodRepository.findByPName(pName);
    }
    public Long countByPName(String pName) {
        return prodRepository.countByPName(pName);
    }
    public void saveUser(Product prod) {
        prodRepository.save(prod);
    }
}