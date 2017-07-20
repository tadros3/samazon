package samazon.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import samazon.models.Product;
import samazon.repositories.ProductRepository;
@Component
public class ProductValidator implements Validator {
    @Autowired
    ProductRepository prodRepository;
    public boolean supports(Class<?> clazz){
        return Product.class.isAssignableFrom(clazz);
    }
    public void validate(Object target, Errors errors){
        Product prod = (Product) target;
        String pName = prod.getPName();
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pName", "prod.pName.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "prod.price.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "inStock", "prod.inStock.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sImage", "prod.sImage.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lImage", "prod.lImage.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sDesc", "prod.sDesc.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lDesc", "prod.lDesc.empty");
        if(prodRepository.countByPName(pName)>0){
            errors.rejectValue("pName", "prod.pName.duplicate");
        }
    }
}