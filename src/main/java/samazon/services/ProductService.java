package samazon.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

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
    public void saveProduct(Product prod) {
        prodRepository.save(prod);
    }
    
    public void deleteProduct(Product prod) {
    	prodRepository.delete(prod);
    }
    
    /*public ArrayList<Product> listAll() {
    	ArrayList<Product> products = new ArrayList<>();
        ProductRepository.findAll().forEach(products::add);
        return products;
    }*/
    
    public ArrayList<Product> products() {
        return prodRepository.findByActive("active");
    }
}